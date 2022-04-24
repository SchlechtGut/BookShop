package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.data.book.rating.BookRating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface BookRatingRepository extends JpaRepository<BookRating, Integer> {
    boolean existsBookRatingByBookId(Integer bookId);

    boolean existsBookRatingByUserId(Integer userId);

    boolean existsByUserIdAndBookId(Integer userId, Integer bookId);

    @Transactional
    void deleteByUserIdAndBookId(Integer userId, Integer bookId);

    List<BookRating> findByBookId(Integer bookId);
}
