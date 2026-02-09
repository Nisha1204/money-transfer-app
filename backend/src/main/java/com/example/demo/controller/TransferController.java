package com.example.demo.controller;

import com.example.demo.dto.TransferRequest;
import com.example.demo.dto.TransferResponse;
import com.example.demo.entity.Account;
import com.example.demo.entity.TransactionLog;
import com.example.demo.service.AccountService;
import com.example.demo.service.TransferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @Autowired
    private AccountController accountService;

    @PostMapping("/transfers")
    public TransferResponse executeTransfer(@RequestBody TransferRequest request) {
        transferService.transfer(request);

        float newBalance = accountService.getBalance(request.getFromAccountId());

        return new TransferResponse(
                "Transfer processed successfully",
                "SUCCESS",
                request.getAmount(),
                newBalance,
                LocalDateTime.now()
        );
    }
}