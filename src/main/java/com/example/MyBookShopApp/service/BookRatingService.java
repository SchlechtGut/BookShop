package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.book.rating.BookRating;
import com.example.MyBookShopApp.repository.BookRatingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookRatingService {

    private final BookRatingRepository bookRatingRepository;

    public BookRatingService(BookRatingRepository bookRatingRepository) {
        this.bookRatingRepository = bookRatingRepository;
    }

    public void addRating(BookRating rating) {
        bookRatingRepository.save(rating);
    }

    public boolean userRatedBook(Integer userId, Integer bookId) {
        return bookRatingRepository.existsByUserIdAndBookId(userId, bookId);
    }

    public void deleteBookRating(Integer userId, Integer bookId) {
        bookRatingRepository.deleteByUserIdAndBookId(userId, bookId);
    }

    public List<BookRating> getRatesDistribution(Integer bookId) {
        return bookRatingRepository.findByBookId(bookId);
    }
}
