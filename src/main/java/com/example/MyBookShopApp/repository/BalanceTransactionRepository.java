package com.example.MyBookShopApp.repository;

import com.example.MyBookShopApp.data.payments.BalanceTransactionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceTransactionRepository extends JpaRepository<BalanceTransactionEntity, Integer> {
}
