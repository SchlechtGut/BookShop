package com.example.MyBookShopApp.service;

import com.example.MyBookShopApp.api.response.SuccessResponse;
import com.example.MyBookShopApp.api.response.TransactionsResponse;
import com.example.MyBookShopApp.data.DTO.TransactionDTO;
import com.example.MyBookShopApp.data.payments.BalanceTransactionEntity;
import com.example.MyBookShopApp.data.user.User;
import com.example.MyBookShopApp.repository.BalanceTransactionRepository;
import com.example.MyBookShopApp.repository.UserRepository;
import com.example.MyBookShopApp.security.UserRegister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
public class ApiService {
    private final BalanceTransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final UserRegister userRegister;

    public ApiService(BalanceTransactionRepository transactionRepository, UserRepository userRepository, UserRegister userRegister) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.userRegister = userRegister;
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

    public TransactionsResponse getUserTransactions(Integer offset, Integer limit, String sort, Authentication authentication) {
        User user = userRegister.getCurrentUser(authentication);
        Sort sortBy = sort.equals("asc") ? Sort.by(Sort.Direction.ASC, "time") : Sort.by(Sort.Direction.DESC, "time");

        Pageable nextPage = PageRequest.of(offset, limit, sortBy);

        Page<BalanceTransactionEntity> page = transactionRepository.findByUserId(user.getId(), nextPage);

        List<TransactionDTO> transactionDTOs = new ArrayList<>();

        for (BalanceTransactionEntity transaction : page.getContent()) {
            TransactionDTO transactionDTO = new TransactionDTO();
            transactionDTO.setDescription(transaction.getDescription());
            transactionDTO.setTime(transaction.getTime().toEpochSecond(ZoneOffset.UTC));
            transactionDTO.setValue((double) transaction.getValue());
            transactionDTOs.add(transactionDTO);
        }

        return new TransactionsResponse((int) page.getTotalElements(), transactionDTOs);
    }

}
