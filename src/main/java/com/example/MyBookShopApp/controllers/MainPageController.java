package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.aop.SearchSection;
import com.example.MyBookShopApp.data.DTO.BooksPageDto;
import com.example.MyBookShopApp.data.DTO.SearchWordDto;
import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.book.tag.Tag;
import com.example.MyBookShopApp.errs.EmptySearchException;
import com.example.MyBookShopApp.security.UserRegister;
import com.example.MyBookShopApp.service.Book2UserService;
import com.example.MyBookShopApp.service.BookService;
import com.example.MyBookShopApp.service.BookPopularityService;
import com.example.MyBookShopApp.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping
public class MainPageController extends DefaultController {

    private final BookService bookService;
    private final BookPopularityService bookPopularityService;
    private final TagService tagService;

    @Autowired
    public MainPageController(UserRegister userRegister, BookService bookService, BookPopularityService bookPopularityService, TagService tagService) {
        super(userRegister);
        this.bookService = bookService;
        this.bookPopularityService = bookPopularityService;
        this.tagService = tagService;
    }

    @ModelAttribute("searchResults")
    public List<Book> searchResults() {
        return new ArrayList<>();
    }

    @ModelAttribute("recommendedBooks")
    public List<Book> recommendedBooks() {
        return bookService.getPageOfRecommendedBooks(0, 6).getContent();
    }

    @ModelAttribute("recentBooks")
    public List<Book> recentBooks() {
        return bookService.getRightPageOfRecentBooks(null, null,0, 6).getBooks();
    }

    @ModelAttribute("popularBooks")
    public List<Book> popularBooks() {
        return bookPopularityService.getPageOfPopularBooks(0, 6).getContent();
    }

    @ModelAttribute("tagList")
    public List<Tag> tags() {
        return tagService.getTags();
    }

    @GetMapping()
    public String mainPage() {
        return "index";
    }

    @SearchSection
    @GetMapping(value = {"/search", "/search/{searchWord}"})
    public String getSearchResult(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
                                  Model model) throws EmptySearchException {
        if (searchWordDto != null) {
            model.addAttribute("searchWordDto", searchWordDto);
            model.addAttribute("searchResults", bookService.getPageOfGoogleBooksApiSearchResult(searchWordDto.getExample(), 0, 5));
            return "/search/index";
        } else {
            throw new EmptySearchException("Поиск по null невозможен");
        }
    }

    @GetMapping("/search/page/{searchWord}")
    @ResponseBody
    public BooksPageDto getNextSearchPage(@RequestParam("offset") Integer offset,
                                          @RequestParam("limit") Integer limit,
                                          @PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto) {
        List<Book> books = bookService.getPageOfGoogleBooksApiSearchResult(searchWordDto.getExample(), offset, limit);
        return new BooksPageDto(books);
    }

}
