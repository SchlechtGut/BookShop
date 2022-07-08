package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.data.book.ViewedBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ViewedBookRepository extends JpaRepository<ViewedBook, Integer> {


    ViewedBook findByBookIdAndUserId(Integer bookId, Integer userId);

    @Query(value = "select * from viewed_book where time + interval '?1 hours' <= CURRENT_TIMESTAMP ", nativeQuery = true)
    List<ViewedBook> findExpired(Integer hoursExpiring);

    List<ViewedBook> findByUserId(Integer userId);
}
