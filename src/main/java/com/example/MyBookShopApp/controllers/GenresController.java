package com.example.MyBookShopApp.controllers;

import com.example.MyBookShopApp.data.DTO.GenreDto;
import com.example.MyBookShopApp.data.genre.Genre;
import com.example.MyBookShopApp.security.UserRegister;
import com.example.MyBookShopApp.service.Book2UserService;
import com.example.MyBookShopApp.service.BookService;
import com.example.MyBookShopApp.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GenresController extends DefaultController {

    private final GenreService genreService;
    private final BookService bookService;

    @Autowired
    public GenresController(UserRegister userRegister, GenreService genreService, BookService bookService) {
        super(userRegister);
        this.genreService = genreService;
        this.bookService = bookService;
    }

    @GetMapping("/genres")
    public String genresPage(Model model) {
        List<Genre> list = genreService.getAllGenres();

        List<GenreDto> nodes = new ArrayList<>();

        model.addAttribute("genreList", genreService.getAllGenres());
        //make
        for (Genre genre : list) {
            GenreDto node = new GenreDto();
            node.setId(genre.getId());
            node.setParentId(genre.getParentId());
            node.setSlug(genre.getSlug());
            node.setName(genre.getName());
            node.setBooksCount(genre.getBooks().size());

            nodes.add(node);
        }
        //set children

        for (GenreDto node : nodes) {
            Integer id = node.getParentId();
            if (id != null) {
                GenreDto parent = nodes.stream().filter(x -> x.getId().equals(id)).findFirst().get();
                parent.getChildren().add(node);
            }
        }

        for (GenreDto node : nodes) {
            boolean childrenWithChildren = false;
            boolean hasChildren = !node.getChildren().isEmpty();

            if (hasChildren) {
                for (GenreDto child : node.getChildren()) {
                    if (!child.getChildren().isEmpty()) {
                        childrenWithChildren = true;
                    }
                }
            }

            if (hasChildren && childrenWithChildren) {
                node.setEmbedded(true);
            }
        }

        model.addAttribute("nodes", nodes.stream().filter(x -> x.getParentId() == null).collect(Collectors.toList()));

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
