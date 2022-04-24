package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.book.rating.BookRating;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.security.UserRegister;
import com.example.MyBookShopApp.service.BookRatingService;
import com.example.MyBookShopApp.service.BookService;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RatingController {

    private final BookService bookService;
    private final BookRatingService bookRatingService;
    private final UserRegister bookstoreUserRegister;

    public RatingController(BookService bookService, BookRatingService bookRatingService, UserRegister bookstoreUserRegister) {
        this.bookService = bookService;
        this.bookRatingService = bookRatingService;
        this.bookstoreUserRegister = bookstoreUserRegister;
    }

    @Secured("ROLE_USER")
    @PostMapping("/api/rateBook")
    public String rateBook(@RequestParam Integer value, @RequestParam Integer bookId, Authentication authentication) {
        Book book = bookService.getBookById(bookId);
        User user = bookstoreUserRegister.getCurrentUser(authentication);

        if (authentication == null) {
            return ("redirect:/books/" + book.getSlug());
        } else if (bookRatingService.userRatedBook(user.getId(), bookId)) {
            bookRatingService.deleteBookRating(user.getId(), bookId);
        }

        BookRating bookRating = new BookRating(value, book.getId(), user.getId());
        bookRatingService.addRating(bookRating);

        return ("redirect:/books/" + book.getSlug());
    }
}
