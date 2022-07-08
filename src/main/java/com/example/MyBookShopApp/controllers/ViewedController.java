package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.security.UserRegister;
import com.example.MyBookShopApp.service.BookPopularityService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class ViewedController extends DefaultController {

    private final BookPopularityService bookPopularityService;
    private final UserRegister userRegister;

    public ViewedController(BookPopularityService bookPopularityService, UserRegister userRegister) {
        this.bookPopularityService = bookPopularityService;
        this.userRegister = userRegister;
    }

    @ModelAttribute("viewedBooks")
    public List<Book> viewedBooks(Authentication authentication) {
        return bookPopularityService.getViewedBooks(userRegister.getCurrentUser(authentication));
    }

    @ModelAttribute("curUser")
    public User curUser(Authentication authentication) {
        return userRegister.getCurrentUser(authentication);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/books/viewed")
    public String viewed(){
        return "books/viewed";
    }
}
