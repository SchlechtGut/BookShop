package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.data.book.review.BookReviewLikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface BookReviewRateRepository extends JpaRepository<BookReviewLikeEntity, Integer> {
    boolean existsByReviewIdAndUserId(Integer reviewId, Integer userId);

    @Transactional
    void deleteByReviewIdAndUserId(Integer reviewId, Integer userId);
}
