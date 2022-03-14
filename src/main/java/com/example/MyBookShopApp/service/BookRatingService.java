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

    public boolean hasBookId(Integer id) {
        return bookRatingRepository.existsBookRatingByBookId(id);
    }

    public boolean hasUserId(Integer id) {
        return bookRatingRepository.existsBookRatingByUserId(id);
    }

    public void deleteBookRatingByBookId(Integer id) {
        bookRatingRepository.deleteByBookId(id);
    }

    public List<BookRating> getRatesDistribution(Integer bookId) {
        return bookRatingRepository.findByBookId(bookId);
    }
}
