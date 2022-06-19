package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.data.payments.BalanceTransactionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BalanceTransactionRepository extends JpaRepository<BalanceTransactionEntity, Integer> {

    List<BalanceTransactionEntity> findByUserId(Integer userId);

    Page<BalanceTransactionEntity> findByUserId(Integer userId, Pageable pageable);
}
