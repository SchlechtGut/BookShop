package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BooksPageDto;
import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.service.ApiService;
import com.example.MyBookShopApp.service.BookService;
import com.example.MyBookShopApp.service.BooksRatingAndPopularityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {
    private final BookService bookService;
    private final ApiService apiService;
    private final BooksRatingAndPopularityService booksRatingAndPopularityService;

    @Autowired
    public ApiController(BookService bookService, ApiService apiService, BooksRatingAndPopularityService booksRatingAndPopularityService) {
        this.bookService = bookService;
        this.apiService = apiService;
        this.booksRatingAndPopularityService = booksRatingAndPopularityService;
    }

    @GetMapping("/api/books/recent")
    public BooksPageDto recent(@RequestParam(required = false) String from, @RequestParam(required = false) String to,
                               @RequestParam Integer offset, @RequestParam Integer limit) {

        return bookService.getRightPageOfRecentBooks(from, to, offset, limit);
    }

    @GetMapping("/api/books/recommended")
    public BooksPageDto recommended(@RequestParam("offset") Integer offset,
                                    @RequestParam("limit") Integer limit) {
        Page<Book> page = bookService.getPageOfRecommendedBooks(offset, limit);

        return new BooksPageDto(page.getTotalElements(), page.getContent());
    }

    @GetMapping("/api/books/popular")
    public BooksPageDto popular(@RequestParam("offset") Integer offset,
                                @RequestParam("limit") Integer limit,
                                Model model) {
        Page<Book> page = booksRatingAndPopularityService.getPageOfPopularBooks(offset, limit);
        model.addAttribute("newPage" , page.getContent().size());

        return new BooksPageDto(page.getTotalElements(), page.getContent());
    }

    @GetMapping("/api/books/tag/{id}")
    public BooksPageDto tagBooks(@RequestParam("offset") Integer offset,
                                 @RequestParam("limit") Integer limit,
                                 @PathVariable Integer id) {
        Page<Book> page = bookService.getPageOfBooksByTag(id, offset, limit);

        return new BooksPageDto(page.getTotalElements(), page.getContent());
    }

    @GetMapping("/api/books/genre/{id}")
    public BooksPageDto genreBooks(@RequestParam("offset") Integer offset,
                                 @RequestParam("limit") Integer limit,
                                 @PathVariable Integer id) {
        Page<Book> page = bookService.getPageOfBooksByGenre(id, offset, limit);

        return new BooksPageDto(page.getTotalElements(), page.getContent());
    }

    @GetMapping("/api/books/author/{id}")
    public BooksPageDto authorBooks(@RequestParam("offset") Integer offset,
                                    @RequestParam("limit") Integer limit,
                                    @PathVariable Integer id) {
        Page<Book> page = bookService.getPageOfAuthorBooks(id, offset, limit);

        return new BooksPageDto(page.getTotalElements(), page.getContent());
    }


}
