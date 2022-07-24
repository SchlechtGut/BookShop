package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.security.UserRegister;
import com.example.MyBookShopApp.service.Book2UserService;
import com.example.MyBookShopApp.service.BookService;
import com.example.MyBookShopApp.data.book.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Controller
public class RecentController extends DefaultController {

    private final BookService bookService;

    @Autowired
    public RecentController(UserRegister userRegister, BookService bookService) {
        super(userRegister);
        this.bookService = bookService;
    }

    @ModelAttribute("recentBooks")
    public List<Book> recentBooks() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

        String from = LocalDate.now().minusMonths(1).format(formatter);
        String to = LocalDate.now().format(formatter);

        return bookService.getRightPageOfRecentBooks(from, to,0, 20).getBooks();
    }

    @GetMapping("/books/recent")
    public String recent(){
        return "books/recent";
    }
}
