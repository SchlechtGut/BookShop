package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.data.book.ViewedBook;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ViewedBookRepository extends JpaRepository<ViewedBook, Integer> {


    ViewedBook findByBookIdAndUserId(Integer bookId, Integer userId);
}
