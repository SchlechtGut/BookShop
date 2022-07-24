package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.data.book.links.Book2UserTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Book2UserTypeRepository extends JpaRepository<Book2UserTypeEntity, Integer> {
}
