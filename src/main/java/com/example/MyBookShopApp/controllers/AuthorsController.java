package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.service.AuthorService;
import com.example.MyBookShopApp.data.author.Author;
import com.example.MyBookShopApp.service.BookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

@Controller
@Api(description = "authors data")
public class AuthorsController extends DefaultController {

    private final AuthorService authorService;
    private final BookService bookService;

    @Autowired
    public AuthorsController(AuthorService authorService, BookService bookService) {
        this.authorService = authorService;
        this.bookService = bookService;
    }

    @GetMapping("/authors")
    public String authorsPage(Model model) {
        model.addAttribute("authorsMap", authorService.getAuthorsMap());

        return "/authors/index";
    }

    @GetMapping("/authors/{slug}")
    public String particularAuthorPage(@PathVariable("slug") String slug, Model model) {
        Author author = authorService.getAuthorBySlug(slug);

        model.addAttribute("author", author);
        model.addAttribute("authorBooksCount", bookService.getBooksCountByAuthorSlug(slug));
        model.addAttribute("severalBooks", bookService.getSeveralBooksByAuthorID(author.getId()));

        return "/authors/slug";
    }

    @GetMapping("/books/author/{slug}")
    public String allAuthorBooksPage(@PathVariable("slug") String slug, Model model) {
        model.addAttribute("authorBooks", bookService.getPageOfAuthorBooksBySlug(slug, 0, 20).getContent());
        model.addAttribute("authorId", authorService.getAuthorBySlug(slug).getId());
        return "/books/author";
    }





    @GetMapping("/api/authors")
    @ApiOperation("method to get map of authors")
    @ResponseBody
    public Map<String, List<Author>> authors() {
        return authorService.getAuthorsMap();
    }
}
