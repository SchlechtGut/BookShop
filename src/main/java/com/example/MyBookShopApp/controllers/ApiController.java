package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BooksPageDto;
import com.example.MyBookShopApp.service.ApiService;
import com.example.MyBookShopApp.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
public class ApiController {
    private final BookService bookService;
    private final ApiService apiService;

    @Autowired
    public ApiController(BookService bookService, ApiService apiService) {
        this.bookService = bookService;
        this.apiService = apiService;
    }

    @GetMapping("/api/books/recent")
    public BooksPageDto recent(@RequestParam(required = false) String from, @RequestParam(required = false) String to,
                               @RequestParam Integer offset, @RequestParam Integer limit) {

        return bookService.getRightPageOfRecentBooks(from, to, offset, limit);
    }

    @GetMapping("/api/books/recommended")
    public BooksPageDto recommended(@RequestParam("offset") Integer offset,
                                    @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfRecommendedBooks(offset, limit).getContent());
    }

    @GetMapping("/api/books/popular")
    public BooksPageDto popular(@RequestParam("offset") Integer offset,
                                @RequestParam("limit") Integer limit) {
        return new BooksPageDto(bookService.getPageOfPopularBooks(offset, limit).getContent());
    }
}
