package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.BooksPageDto;
import com.example.MyBookShopApp.data.book.Book;
import com.example.MyBookShopApp.data.book.tag.Tag;
import com.example.MyBookShopApp.data.genre.Genre;
import com.example.MyBookShopApp.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GenresController  extends DefaultController {

    private final GenreService genreService;

    @Autowired
    public GenresController(GenreService genreService) {
        this.genreService = genreService;
    }

    @ModelAttribute("genreList")
    public List<Genre> genres() {
        return genreService.getAllGenres();
    }

    @GetMapping("/genres")
    public String mainPage() {
        return "/genres/index";
    }


}
