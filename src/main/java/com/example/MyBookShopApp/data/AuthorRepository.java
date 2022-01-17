package com.example.MyBookShopApp.data;

import com.example.MyBookShopApp.data.author.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author, Integer> {
}
