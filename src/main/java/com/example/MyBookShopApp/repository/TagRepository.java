package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.data.book.tag.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TagRepository extends JpaRepository<Tag, Integer> {

    Tag findBySlug(String slug);
}
