package com.example.demo.controller;

import com.example.demo.dto.AccountResponse;
import com.example.demo.entity.Account;
import com.example.demo.entity.TransactionLog;
import com.example.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "http://localhost:4200")
public class AccountController {


    @Autowired
    private AccountService accountService;

    @GetMapping("/accounts/{id}")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccount(id));
    }

    @GetMapping("/accounts/{id}/balance")
    public ResponseEntity<BigDecimal> getBalance(@PathVariable Long id) {
        return ResponseEntity.ok(accountService.getAccount(id).getBalance());
    }

    @GetMapping("/accounts/{id}/transactions")
    public ResponseEntity<List<TransactionLog>> getHistory(@PathVariable Long id) {
        List<TransactionLog> transactions=accountService.getTransactions(id);
        return ResponseEntity.ok(transactions);
    }

    @PostMapping("/accounts/create")
    public String createAccount(@RequestParam String holderName) {
        accountService.createAccount(holderName);
        return "Account created successfully for " + holderName;
    }

}