package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.book.review.BookReviewEntity;
import com.example.MyBookShopApp.data.book.review.BookReviewLikeEntity;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;

@SpringBootTest
class BookReviewServiceTests {

    private final BookReviewService reviewService;

    private List<BookReviewEntity> sortedList;
    private List<BookReviewEntity> list;

    @Autowired
    BookReviewServiceTests(BookReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @BeforeEach
    void setUp() {
        list = new ArrayList<>();
        sortedList = new ArrayList<>();
        BookReviewEntity review1 = new BookReviewEntity();
        BookReviewEntity review2 = new BookReviewEntity();
        BookReviewEntity review3 = new BookReviewEntity();

        Set<BookReviewLikeEntity> likeSet1 = new HashSet<>();
        likeSet1.add(new BookReviewLikeEntity());

        Set<BookReviewLikeEntity> likeSet2 = new HashSet<>();
        likeSet2.add(new BookReviewLikeEntity());
        likeSet2.add(new BookReviewLikeEntity());

        Set<BookReviewLikeEntity> likeSet3 = new HashSet<>();
        likeSet3.add(new BookReviewLikeEntity());
        likeSet3.add(new BookReviewLikeEntity());
        likeSet3.add(new BookReviewLikeEntity());

        Set<BookReviewLikeEntity> disLikeSet1 = new HashSet<>();
        disLikeSet1.add(new BookReviewLikeEntity());
        disLikeSet1.add(new BookReviewLikeEntity());
        disLikeSet1.add(new BookReviewLikeEntity());

        Set<BookReviewLikeEntity> disLikeSet2 = new HashSet<>();
        disLikeSet2.add(new BookReviewLikeEntity());
        disLikeSet2.add(new BookReviewLikeEntity());

        Set<BookReviewLikeEntity> disLikeSet3 = new HashSet<>();
        disLikeSet3.add(new BookReviewLikeEntity());

        review1.setLikeSet(likeSet1);
        review1.setDislikeSet(disLikeSet1);

        review2.setLikeSet(likeSet2);
        review2.setDislikeSet(disLikeSet2);

        review3.setLikeSet(likeSet3);
        review3.setDislikeSet(disLikeSet3);

        sortedList.add(review3); // 2
        sortedList.add(review2); // 0
        sortedList.add(review1); // -2

        list.add(review2);
        list.add(review1);
        list.add(review3);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void sortReviewsByRating() {
        reviewService.sortReviewsByRating(list);
        assertIterableEquals(list, sortedList);
    }
}