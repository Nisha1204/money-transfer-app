package com.example.demo.service;

import com.example.demo.dto.AccountResponse;
import com.example.demo.entity.TransactionLog;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {
    void createAccount(String holderName);

    void deleteAccount(Long id);

    void updateAccount(Long id, BigDecimal balance);

    AccountResponse getAccount(Long id);

    // void transfer(int fromId, int toId, float amount);

    BigDecimal getBalance(Long id);

    List<TransactionLog> getTransactions(Long id);
}
