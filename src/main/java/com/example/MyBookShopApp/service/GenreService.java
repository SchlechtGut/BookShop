package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.genre.Genre;
import com.example.MyBookShopApp.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> getAllGenres() {
        List<Genre> allGenres = genreRepository.findAll();
//        List<Genre> genreTree = new ArrayList<>();
//
//        for (Genre genre : allGenres) {
//            if (genre.getParentId() != null) {
//                int parentId = genre.getParentId();
//                allGenres.stream().filter(x -> x.getId() == parentId).findFirst().get().getChildren().add(genre);
//            } else {
//                genreTree.add(genre);
//            }
//        }

        return allGenres;
    }

    public Genre getGenreBySlug(String slug) {
        return genreRepository.findBySlug(slug);
    }
}
