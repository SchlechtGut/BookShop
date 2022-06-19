package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.data.ProfileResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileResetTokenRepository extends JpaRepository<ProfileResetToken, Integer> {
    ProfileResetToken findByToken(String token);
}
