package com.example.demo.controller;

import com.example.demo.dto.TransferRequest;
import com.example.demo.entity.Account;
import com.example.demo.entity.TransactionLog;
import com.example.demo.service.AccountService;
import com.example.demo.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class AccountController {


    @Autowired
    private AccountService accountService;

    @GetMapping("/accounts/{id}")
    public Account getAccount(@PathVariable int id) {
        return accountService.getAccount(id);
    }

    @GetMapping("/accounts/{id}/balance")
    public float getBalance(@PathVariable int id) {
        return accountService.getAccount(id).getBalance();
    }

    @GetMapping("/accounts/{id}/transactions")
    public List<TransactionLog> getHistory(@PathVariable int id) {
        return accountService.getTransactions(id);
    }

    @PostMapping("/accounts/create")
    public String createAccount(@RequestParam String holderName) {
        accountService.createAccount(holderName);
        return "Account created successfully for " + holderName;
    }

}