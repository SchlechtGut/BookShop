package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.data.user.UserEntity;
import com.example.MyBookShopApp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserEntity> getUserById(int id) {
        return userRepository.findById(id);
    }
}
