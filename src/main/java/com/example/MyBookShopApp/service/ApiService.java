package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.api.response.SuccessResponse;
import com.example.MyBookShopApp.data.payments.BalanceTransactionEntity;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.repository.BalanceTransactionRepository;
import com.example.MyBookShopApp.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
public class ApiService {
    private final BalanceTransactionRepository transactionRepository;
    private final UserRepository userRepository;


    public ApiService(BalanceTransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    public SuccessResponse putMoney(String hash, Integer sum, Long time) {
        LocalDateTime utcDate =
                Instant.ofEpochMilli(time).atZone(ZoneId.of("UTC")).toLocalDateTime();

        User user = userRepository.findByHash(hash);

        BalanceTransactionEntity transaction = new BalanceTransactionEntity();
        transaction.setDescription("money was put");
        transaction.setTime(utcDate);
        transaction.setValue(sum);
        transaction.setUserId(user.getId());
        transaction.setBookId(0);
        transactionRepository.save(transaction);

        return new SuccessResponse(true);
    }
}
