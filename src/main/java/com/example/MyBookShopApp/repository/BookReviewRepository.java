package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.book.review.BookReviewEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookReviewRepository extends JpaRepository<BookReviewEntity, Integer> {

//    @Query(value = "SELECT b FROM BookReviewEntity b, b.likeSet.size '-' b.likeSet.size as rating WHERE b.bookId=?1")
//    Page<BookReviewEntity> findById(Integer bookId, Pageable pageable);

//    @Query(value = "select * , book_review.getRating as \"rating\" from book_review WHERE book_review.book_id=?1", nativeQuery = true)
//    Page<BookReviewEntity> findByID(Integer bookId, Pageable nextPage);

    Page<BookReviewEntity> findByBookId(Integer bookId, Pageable pageable);
}
