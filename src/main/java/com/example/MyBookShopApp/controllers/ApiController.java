package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.api.response.SuccessResponse;
import com.example.MyBookShopApp.api.response.TransactionsResponse;
import com.example.MyBookShopApp.data.DTO.BooksPageDto;
import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.service.ApiService;
import com.example.MyBookShopApp.service.BookService;
import com.example.MyBookShopApp.service.BookPopularityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiController {
    private final BookService bookService;
    private final ApiService apiService;
    private final BookPopularityService bookPopularityService;


    @Autowired
    public ApiController(BookService bookService, ApiService apiService, BookPopularityService bookPopularityService) {
        this.bookService = bookService;
        this.apiService = apiService;
        this.bookPopularityService = bookPopularityService;
    }

    @GetMapping("/books/recent")
    public BooksPageDto recent(@RequestParam(required = false) String from, @RequestParam(required = false) String to,
                               @RequestParam Integer offset, @RequestParam Integer limit) {

        return bookService.getRightPageOfRecentBooks(from, to, offset, limit);
    }

    @GetMapping("/books/recommended")
    public BooksPageDto recommended(@RequestParam Integer offset,
                                    @RequestParam Integer limit) {
        Page<Book> page = bookService.getPageOfRecommendedBooks(offset, limit);

        return new BooksPageDto(page.getTotalElements(), page.getContent());
    }

    @GetMapping("/books/popular")
    public BooksPageDto popular(@RequestParam Integer offset,
                                @RequestParam Integer limit,
                                Model model) {
        Page<Book> page = bookPopularityService.getPageOfPopularBooks(offset, limit);
        model.addAttribute("newPage" , page.getContent().size());

        return new BooksPageDto(page.getTotalElements(), page.getContent());
    }

    @GetMapping("/books/tag/{id}")
    public BooksPageDto tagBooks(@RequestParam Integer offset,
                                 @RequestParam Integer limit,
                                 @PathVariable Integer id) {
        Page<Book> page = bookService.getPageOfBooksByTag(id, offset, limit);

        return new BooksPageDto(page.getTotalElements(), page.getContent());
    }

    @GetMapping("/books/genre/{id}")
    public BooksPageDto genreBooks(@RequestParam Integer offset,
                                 @RequestParam Integer limit,
                                 @PathVariable Integer id) {
        Page<Book> page = bookService.getPageOfBooksByGenre(id, offset, limit);

        return new BooksPageDto(page.getTotalElements(), page.getContent());
    }

    @GetMapping("/books/author/{id}")
    public BooksPageDto authorBooks(@RequestParam Integer offset,
                                    @RequestParam Integer limit,
                                    @PathVariable Integer id) {

        Page<Book> page = bookService.getPageOfAuthorBooks(id, offset, limit);

        return new BooksPageDto(page.getTotalElements(), page.getContent());
    }

    @GetMapping("/transactions")
    @PreAuthorize("isAuthenticated()")
    public TransactionsResponse getUserTransactions(@RequestParam Integer offset,
                                                    @RequestParam Integer limit,
                                                    @RequestParam(defaultValue = "asc") String sort,
                                                    Authentication authentication) {
        return apiService.getUserTransactions(offset, limit, sort, authentication);
    }

    @PostMapping("/payment")
    public SuccessResponse putMoneyIntoAccount(@RequestParam String hash,
                                               @RequestParam Integer sum,
                                               @RequestParam Long time) {

        return apiService.putMoney(hash, sum, time);
    }
}
