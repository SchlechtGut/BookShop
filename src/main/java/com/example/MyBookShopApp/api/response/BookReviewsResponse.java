package com.example.MyBookShopApp.api.response;

import com.example.MyBookShopApp.data.book.review.BookReviewEntity;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookReviewsResponse {

    private Long count;
    private List<BookReviewEntity> reviews;

    public BookReviewsResponse(Long count, List<BookReviewEntity> reviews) {
        this.count = count;
        this.reviews = reviews;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public List<BookReviewEntity> getReviews() {
        return reviews;
    }

    public void setReviews(List<BookReviewEntity> reviews) {
        this.reviews = reviews;
    }
}
