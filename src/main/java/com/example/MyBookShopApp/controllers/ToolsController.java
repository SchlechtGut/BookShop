package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.genre.Genre;
import com.example.MyBookShopApp.service.GenreService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ToolsController extends DefaultController {

    private final GenreService genreService;

    public ToolsController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping("/documents")
    public String documents(){
        return "/documents/index";
    }

    @GetMapping("/about")
    public String about(){
        return "about";
    }

    @GetMapping("/faq")
    public String faq(){
        return "faq";
    }

    @GetMapping("/contacts")
    public String contacts(){
        return "contacts";
    }

    @GetMapping("/api/genre")
    @ResponseBody
    public List<Genre> allGenres() {
        return genreService.getAllGenres();
    }
}
