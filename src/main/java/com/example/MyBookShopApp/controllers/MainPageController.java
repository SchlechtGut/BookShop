package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.book.tag.Tag;
import com.example.MyBookShopApp.errs.EmptySearchException;
import com.example.MyBookShopApp.service.BookService;
import com.example.MyBookShopApp.data.BooksPageDto;
import com.example.MyBookShopApp.data.SearchWordDto;
import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.service.BooksRatingAndPopularityService;
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
    private final BooksRatingAndPopularityService booksRatingAndPopularityService;
    private final TagService tagService;

    @Autowired
    public MainPageController(BookService bookService, BooksRatingAndPopularityService booksRatingAndPopularityService, TagService tagService) {
        this.bookService = bookService;
        this.booksRatingAndPopularityService = booksRatingAndPopularityService;
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
        return booksRatingAndPopularityService.getPageOfPopularBooks(0, 6).getContent();
    }

    @ModelAttribute("tagList")
    public List<Tag> tags() {
        return tagService.getTags();
    }

    @GetMapping()
    public String mainPage(){
        return "index";
    }

    @GetMapping(value = {"/search", "/search/{searchWord}"})
    public String getSearchResult(@PathVariable(value = "searchWord", required = false) SearchWordDto searchWordDto,
                                  Model model) throws EmptySearchException {
        if (searchWordDto != null) {
            model.addAttribute("searchWordDto", searchWordDto);
            model.addAttribute("searchResults", bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), 0, 5).getContent());
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
        return new BooksPageDto(bookService.getPageOfSearchResultBooks(searchWordDto.getExample(), offset, limit).getContent());
    }

}
