package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.api.request.FeedbackRequest;
import com.example.MyBookShopApp.data.user.MessageEntity;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ToolsService {

    private final MessageRepository messageRepository;

    public ToolsService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void saveFeedBack(User currentUser, FeedbackRequest feedbackRequest) {
        MessageEntity message = new MessageEntity();
        if (currentUser != null) {
            message.setEmail(currentUser.getEmail());
            message.setName(currentUser.getName());
            message.setUser(currentUser);
        }

        message.setTime(LocalDateTime.now());
        message.setText(feedbackRequest.getMessage());
        message.setSubject(feedbackRequest.getTopic());

        messageRepository.save(message);
    }
}
