package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.api.response.BookReviewsResponse;
import com.example.MyBookShopApp.api.response.SuccessResponse;
import com.example.MyBookShopApp.data.book.review.BookReviewEntity;
import com.example.MyBookShopApp.data.book.review.BookReviewLikeEntity;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.security.UserRegister;
import com.example.MyBookShopApp.service.BookReviewService;
import com.example.MyBookShopApp.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
public class ReviewController {

    private final BookReviewService bookReviewService;
    private final UserService userService;
    private final UserRegister bookstoreUserRegister;

    public ReviewController(BookReviewService bookReviewService, UserService userService, UserRegister bookstoreUserRegister) {
        this.bookReviewService = bookReviewService;
        this.userService = userService;
        this.bookstoreUserRegister = bookstoreUserRegister;
    }

    @Secured("ROLE_USER")
    @PostMapping("/api/bookReview")
    @ResponseBody
    public SuccessResponse bookReview(@RequestParam(required = false) Integer bookId,
                                      @RequestParam(required = false) String text,
                                      Authentication authentication) {
        User user = bookstoreUserRegister.getCurrentUser(authentication);

        if (bookId == null || text == null || text.isBlank() ) {
            return new SuccessResponse(false, "some error");
        }

        bookReviewService.addReview(bookId, text, user);

        return new SuccessResponse(true);
    }

    @Secured("ROLE_USER")
    @PostMapping("/api/rateBookReview")
    @ResponseBody
    public SuccessResponse rateBookReview(@RequestParam(required = false) Integer value,
                                          @RequestParam(required = false) Integer reviewId,
                                          Authentication authentication) {
        User user = bookstoreUserRegister.getCurrentUser(authentication);

        if (value == null || reviewId == null) {
            return new SuccessResponse(false);
        }

        BookReviewLikeEntity bookReviewLike = new BookReviewLikeEntity(reviewId, user.getId(), LocalDateTime.now(), value);
        bookReviewService.saveLike(bookReviewLike);

        return new SuccessResponse(true);
    }

    @GetMapping("/api/book/{id}/reviews")
    @ResponseBody
    public BookReviewsResponse getReviewsByBookId(@RequestParam Integer offset, @RequestParam Integer limit, @PathVariable Integer id) {
        Page<BookReviewEntity> page = bookReviewService.getPageOfBookReviews(id, offset, limit);
        return new BookReviewsResponse(page.getTotalElements(), page.getContent());
    }
}
