package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BookService;
import com.example.MyBookShopApp.data.BooksPageDto;
import com.example.MyBookShopApp.data.book.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/books/")
public class BooksController extends DefaultController {

    private final BookService bookService;

    @Autowired
    public BooksController(BookService bookService) {
        this.bookService = bookService;
    }

    @ModelAttribute("booksList")
    public List<Book> bookList(){
        return bookService.getBooksData();
    }

    @ModelAttribute("recentBooks")
    public List<Book> recentBooks() {
        return bookService.getPageOfRecentBooks(0, 100).getContent();
    }

    @GetMapping("recent")
    public String recent(){
        return "books/recent";
    }

    @GetMapping("popular")
    public String popular(){
        return "books/popular";
    }



}
