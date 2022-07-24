package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.data.book.links.Book2UserEntity;
import com.example.MyBookShopApp.data.enums.Book2UserType;
import com.example.MyBookShopApp.data.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public interface Book2UserRepository extends JpaRepository<Book2UserEntity, Integer> {

    Book2UserEntity findByBookIdAndUserId(int bookId, int userId);

    Book2UserEntity findByBookIdAndUserIdAndTypeIn(int bookId, int userId, Collection<Book2UserType> type);

    Integer countByUserAndType(User user, Book2UserType type);

    List<Book2UserEntity> findByUserAndType(User user, Book2UserType type);

    @Transactional
    void deleteByUserAndBookIdAndType(User user, int bookId, Book2UserType type);
}
