package com.example.demo.controller;

import com.example.demo.dto.AccountResponse;
import com.example.demo.entity.Account;
import com.example.demo.entity.TransactionLog;
import com.example.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
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
    public ResponseEntity<Map<String, String>> createAccount(@RequestParam String holderName) {
        accountService.createAccount(holderName);

        return ResponseEntity.ok(Collections.singletonMap(
                "message", "Account created successfully for " + holderName
        ));
    }

    @GetMapping("/accounts/my-accounts")
    public ResponseEntity<List<AccountResponse>> getMyAccounts(Authentication authentication) {
        String username = authentication.getName(); // Extracts username from the Security Context
        List<AccountResponse> accounts = accountService.getAccountsByUsername(username);
        return ResponseEntity.ok(accounts);
    }

}