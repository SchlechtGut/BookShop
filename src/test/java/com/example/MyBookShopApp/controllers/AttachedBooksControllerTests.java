package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.book.links.Book2UserEntity;
import com.example.MyBookShopApp.data.enums.Book2UserType;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.repository.Book2UserRepository;
import com.example.MyBookShopApp.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
class AttachedBooksControllerTests {

    private final MockMvc mockMvc;
    private final Book2UserRepository book2UserRepository;
    private final UserRepository userRepository;

    private User emptyUser;

    @Autowired
    AttachedBooksControllerTests(MockMvc mockMvc, Book2UserRepository book2UserRepository, UserRepository userRepository) {
        this.mockMvc = mockMvc;
        this.book2UserRepository = book2UserRepository;
        this.userRepository = userRepository;
    }

    @BeforeEach
    void setUp() {
        emptyUser = new User();
        userRepository.save(emptyUser);

        Book2UserEntity book2User1 = new Book2UserEntity();
        book2User1.setBookId(1);
        book2User1.setType(Book2UserType.CART);
        book2User1.setUser(emptyUser);

        Book2UserEntity book2User2 = new Book2UserEntity();
        book2User2.setBookId(2);
        book2User2.setType(Book2UserType.CART);
        book2User2.setUser(emptyUser);

        Book2UserEntity book2User3 = new Book2UserEntity();
        book2User3.setBookId(3);
        book2User3.setType(Book2UserType.KEPT);
        book2User3.setUser(emptyUser);

        book2UserRepository.saveAll(List.of(book2User1, book2User2, book2User3));
    }

    @AfterEach
    void tearDown() {
        book2UserRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void handleRemoveBookFromCartRequest() throws Exception {
        Integer expectedCount = 1;

        mockMvc.perform(post("/api/changeBookStatus")
                        .param("status", "UNLINK")
                        .param("booksIds", "1")
                        .sessionAttr("empty_user", emptyUser)
                )
                .andDo(print())
                .andExpect(status().isOk());

        assertEquals(expectedCount, book2UserRepository.findByUserAndType(emptyUser, Book2UserType.CART).size());
    }

    @Test
    void addBookToPostponedNotInCart() throws Exception {
        Integer expectedCount = 3;

        mockMvc.perform(post("/api/changeBookStatus")
                        .param("status", "KEPT")
                        .param("booksIds", "4,5")
                        .sessionAttr("empty_user", emptyUser)
                )
                .andDo(print())
                .andExpect(status().isOk());

        assertEquals(expectedCount, book2UserRepository.findByUserAndType(emptyUser, Book2UserType.KEPT).size());
    }

    @Test
    void addBookToFilledCartNotInPostponed() throws Exception {
        Integer expectedCount = 4;

        mockMvc.perform(post("/api/changeBookStatus")
                            .param("status", "CART")
                        .param("booksIds", "4,5")
                        .sessionAttr("empty_user", emptyUser))
                .andDo(print())
                .andExpect(status().isOk());

        assertEquals(expectedCount, book2UserRepository.findByUserAndType(emptyUser, Book2UserType.CART).size());
    }

    @Test
    void addAlreadyPostponedBookToCart() throws Exception {
        Integer expectedPostCount = 0;
        Integer expectedCartCount = 3;

        mockMvc.perform(post("/api/changeBookStatus")
                        .param("status", "CART")
                        .param("booksIds", "3")
                        .sessionAttr("empty_user", emptyUser))
                .andDo(print())
                .andExpect(status().isOk());

        assertEquals(expectedCartCount, book2UserRepository.findByUserAndType(emptyUser, Book2UserType.CART).size());
        assertEquals(expectedPostCount, book2UserRepository.findByUserAndType(emptyUser, Book2UserType.KEPT).size());
    }


}