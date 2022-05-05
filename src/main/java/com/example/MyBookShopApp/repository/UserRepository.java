package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.data.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface UserRepository extends JpaRepository<User, Integer> {

    User findByEmail(String email);

    boolean existsByEmail(String email);

    @Transactional
    void deleteUserByEmail(String email);
}
