package com.example.demo.service;

import com.example.demo.entity.Account;
import com.example.demo.entity.TransactionLog;

import java.util.List;

public interface AccountService {
    void createAccount(String holderName);

    void deleteAccount(int id);

    void updateAccount(int id, float balance);

    Account getAccount(int id);

    void transfer(int fromId, int toId, float amount);

    float getBalance(int id);

    List<TransactionLog> getTransactions(int id);
}
