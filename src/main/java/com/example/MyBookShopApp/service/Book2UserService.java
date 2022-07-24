package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.book.links.Book2UserEntity;
import com.example.MyBookShopApp.data.enums.Book2UserType;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.repository.Book2UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Book2UserService {

    private final Book2UserRepository book2UserRepository;

    public Book2UserService(Book2UserRepository book2UserRepository) {
        this.book2UserRepository = book2UserRepository;
    }

    public Integer countBooks2User(User user, Book2UserType type) {
        return book2UserRepository.countByUserAndType(user, type);
    }
}
