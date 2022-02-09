package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.genre.Genre;
import com.example.MyBookShopApp.service.BookService;
import com.example.MyBookShopApp.service.GenreService;
import com.sun.tools.javac.jvm.Gen;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GenresController  extends DefaultController {

    private final GenreService genreService;
    private final BookService bookService;

    @Autowired
    public GenresController(GenreService genreService, BookService bookService) {
        this.genreService = genreService;
        this.bookService = bookService;
    }

    @ModelAttribute("genreList")
    public List<Genre> genres() {
        return genreService.getAllGenres();
    }

    @GetMapping("/genres")
    public String genresPage() {
        return "/genres/index";
    }

    @GetMapping("/genres/{slug}")
    public String particularGenre(@PathVariable String slug, Model model) {
        Genre genre = genreService.getGenreBySlug(slug);

        model.addAttribute("booksByGenre", bookService.getPageOfBooksByGenre(genre.getId(), 0, 20).getContent());
        model.addAttribute("genreId", genre.getId());
        model.addAttribute("genreName", genre.getName());

        return "/genres/slug";
    }



}
