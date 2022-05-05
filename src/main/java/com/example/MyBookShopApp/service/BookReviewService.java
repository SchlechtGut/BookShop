package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.book.review.BookReviewEntity;
import com.example.MyBookShopApp.data.book.review.BookReviewLikeEntity;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.repository.BookReviewRateRepository;
import com.example.MyBookShopApp.repository.BookReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
public class BookReviewService {

    private final BookReviewRepository bookReviewRepository;
    private final BookReviewRateRepository bookReviewRateRepository;

    public BookReviewService(BookReviewRepository bookReviewRepository, BookReviewRateRepository bookReviewRateRepository) {
        this.bookReviewRepository = bookReviewRepository;
        this.bookReviewRateRepository = bookReviewRateRepository;
    }

    public BookReviewEntity getReviewById(Integer reviewId) {
        return bookReviewRepository.findById(reviewId).get();
    }

    public void saveLike(BookReviewLikeEntity bookReviewLike) {
        if (bookReviewRateRepository.existsByReviewIdAndUserId(bookReviewLike.getReviewId(), bookReviewLike.getUserId())) {
            bookReviewRateRepository.deleteByReviewIdAndUserId(bookReviewLike.getReviewId(), bookReviewLike.getUserId());
        }
        bookReviewRateRepository.save(bookReviewLike);
    }

    public void addReview(Integer bookId, String text, User user) {
        BookReviewEntity bookReviewEntity = new BookReviewEntity(LocalDateTime.now(), text, bookId, user);
        bookReviewRepository.save(bookReviewEntity);

    }

    public Page<BookReviewEntity> getPageOfBookReviews(Integer id, Integer offset, Integer limit) {
        Pageable nextPage = PageRequest.of(offset, limit);

        return bookReviewRepository.findByBookId(id, nextPage);
    }

    public List<BookReviewEntity> sortReviewsByRating(List<BookReviewEntity> reviews) {
        reviews.sort((o1, o2) -> Long.compare(o2.getRating(), o1.getRating()));
        return reviews;
    }
}
