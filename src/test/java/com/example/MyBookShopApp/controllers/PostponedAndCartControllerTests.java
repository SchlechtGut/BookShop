package com.example.MyBookShopApp.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class PostponedAndCartControllerTests {

    private final MockMvc mockMvc;

    @Autowired
    PostponedAndCartControllerTests(MockMvc mockMvc) {
        this.mockMvc = mockMvc;
    }

    @Test
    void handleRemoveBookFromCartRequest() throws Exception {
        String slug = "bookSlug";
        String oldCartContents = "book1q2e3/bookSlug";

        Cookie cookie = new Cookie("cartContents", oldCartContents);
        String expectedCardContents = "book1q2e3";

        mockMvc.perform(post("/books/changeBookStatus/cart/remove/" + slug)
                        .cookie(cookie))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/cart"))
                .andExpect(cookie().value("cartContents", expectedCardContents));

    }

    @Test
    void addBookToEmptyCartNotInPostponed() throws Exception {
        String status = "CART";
        String slug = "bookSlug";

        mockMvc.perform(post("/books/changeBookStatus/" + slug).param("status", status))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/" + slug))
                .andExpect(cookie().value("cartContents", slug));
    }

    @Test
    void addBookToFilledCartNotInPostponed() throws Exception {
        String status = "CART";
        String slug = "bookSlug";
        String oldCartContents = "book1q2e3";

        Cookie cookie = new Cookie("cartContents", oldCartContents);

        String expectedCartContents = "book1q2e3/bookSlug";

        mockMvc.perform(post("/books/changeBookStatus/" + slug)
                            .param("status", status)
                            .cookie(cookie))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/" + slug))
                .andExpect(cookie().value("cartContents", expectedCartContents));
    }

    @Test
    void addAlreadyPostponedBookToCart() throws Exception {
        String status = "CART";
        String slug = "bookSlug";
        String oldCartContents = "book1q2e3";
        String oldPostponedBooks = "bookSlug";

        Cookie cartContentsCookie = new Cookie("cartContents", oldCartContents);
        Cookie postponedBooksCookie = new Cookie("postponedBooks", oldPostponedBooks);


        String expectedCartContents = "book1q2e3/bookSlug";
        String expectedPostponedBooks = "";

        mockMvc.perform(post("/books/changeBookStatus/" + slug)
                        .param("status", status)
                        .cookie(cartContentsCookie, postponedBooksCookie))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books/" + slug))
                .andExpect(cookie().value("cartContents", expectedCartContents))
                .andExpect(cookie().value("postponedBooks", expectedPostponedBooks));
    }


}