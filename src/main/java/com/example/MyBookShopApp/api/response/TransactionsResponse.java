package com.example.MyBookShopApp.api.response;

import com.example.MyBookShopApp.data.DTO.TransactionDTO;

import java.util.List;

public class TransactionsResponse {
    private Integer count;
    private List<TransactionDTO> transactions;

    public TransactionsResponse(Integer count, List<TransactionDTO> transactions) {
        this.count = count;
        this.transactions = transactions;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDTO> transactions) {
        this.transactions = transactions;
    }
}
