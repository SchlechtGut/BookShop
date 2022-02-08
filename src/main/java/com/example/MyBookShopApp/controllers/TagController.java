package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.book.tag.Tag;
import com.example.MyBookShopApp.service.BookService;
import com.example.MyBookShopApp.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class TagController extends DefaultController {

    private final TagService tagService;
    private final BookService bookService;

    @Autowired
    public TagController(TagService tagService, BookService bookService) {
        this.tagService = tagService;
        this.bookService = bookService;
    }

    @GetMapping("/tags/{slug}")
    public String getBooksByTag(@PathVariable String slug, Model model) {
        Tag tag = tagService.getTagBySlug(slug);

        List<Book> booksByTag = bookService.getPageOfBooksByTag(tag.getId(), 0, 6).getContent();

        model.addAttribute("tagName", tag.getName());
        model.addAttribute("booksByTag", booksByTag);
        model.addAttribute("tagId", tag.getId());

        return "tags/index";
    }
}
