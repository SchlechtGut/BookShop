package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.data.genre.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, Integer> {
}
