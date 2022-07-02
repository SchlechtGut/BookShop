package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.service.BookPopularityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class PopularController extends DefaultController {

    private final BookPopularityService bookPopularityService;

    @Autowired
    public PopularController(BookPopularityService bookPopularityService) {
        this.bookPopularityService = bookPopularityService;

    }

    @ModelAttribute("popularBooks")
    public List<Book> bookList(){
        return bookPopularityService.getPageOfPopularBooks(0, 20).getContent();
    }

    @GetMapping("/books/popular")
    public String popular(){
        return "books/popular";
    }
}
