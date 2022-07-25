package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.data.user.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends JpaRepository<MessageEntity, Integer> {


}
