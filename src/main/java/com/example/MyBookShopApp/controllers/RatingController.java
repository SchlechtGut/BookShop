package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.book.rating.BookRating;
import com.example.MyBookShopApp.service.BookRatingService;
import com.example.MyBookShopApp.service.BookService;
import com.example.MyBookShopApp.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RatingController {

    private final BookService bookService;
    private final BookRatingService bookRatingService;

    public RatingController(BookService bookService, BookRatingService bookRatingService) {
        this.bookService = bookService;
        this.bookRatingService = bookRatingService;
    }

    @PostMapping("/api/rateBook")
    public String rateBook(@RequestParam Integer value, @RequestParam Integer bookId, Authentication authentication) {
        Book book = bookService.getBookById(bookId);

        System.out.println("before");

        if (authentication == null) {
            System.out.println("null");
            return ("redirect:/books/" + book.getSlug());
        } else if (bookRatingService.hasBookId(bookId)){
            bookRatingService.deleteBookRatingByBookId(bookId);
            System.out.println("else if");
        }

        System.out.println("after");

        BookRating bookRating = new BookRating(value, book.getId());
        bookRatingService.addRating(bookRating);

        return ("redirect:/books/" + book.getSlug());
    }
}
