package com.example.demo.controller;

import com.example.demo.dto.AccountResponse;
import com.example.demo.dto.TransferRequest;
import com.example.demo.entity.Account;
import com.example.demo.entity.TransactionLog;
import com.example.demo.entity.User;
import com.example.demo.repository.AccountRepo;
import com.example.demo.repository.UserRepo;
import com.example.demo.service.AccountService;
import com.example.demo.service.TransferService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class AccountController {


    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepo accountRepository;
    @Autowired
    private UserRepo userRepository;


    /*
    @GetMapping("/accounts/{id}")
    public Account getAccount(@PathVariable int id) {
        return accountService.getAccount(id);
    }

     */

    @GetMapping("/accounts/{id}")
    public AccountResponse getAccount(@PathVariable int id) {
        Account acc = accountService.getAccount(id);
        return new AccountResponse(
                acc.getId(),
                acc.getHolderName(),
                acc.getBalance(),
                acc.getStatus(),
                acc.getOwner().getUsername()
        );
    }


    @GetMapping("/accounts/{id}/balance")
    public float getBalance(@PathVariable int id) {
        return accountService.getAccount(id).getBalance();
    }

    @GetMapping("/accounts/{id}/transactions")
    public List<TransactionLog> getHistory(@PathVariable int id) {
        return accountService.getTransactions(id);
    }

    /*
    @PostMapping("/accounts/create")
    public String createAccount(@RequestParam String holderName) {
        accountService.createAccount(holderName);
        return "Account created successfully for " + holderName;
    }

     */

    //@Override
    @Transactional
    public void createAccount(String holderName) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByUsername(currentUsername)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account acc = new Account();
        acc.setHolderName(holderName);
        acc.setOwner(currentUser); // Link the account to the logged-in user
        acc.setBalance(0.0f);
        acc.setStatus("ACTIVE");

        accountRepository.save(acc);
    }

}