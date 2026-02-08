package com.example.demo.service;

import com.example.demo.dto.TransferRequest;
import com.example.demo.entity.Account;
import com.example.demo.entity.TransactionLog;
import com.example.demo.exception.AccountNotActiveException;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.exception.DuplicateTransferException;
import com.example.demo.exception.InsufficientBalanceException;
import com.example.demo.repository.AccountRepo;
import com.example.demo.repository.TransactionLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/*
@Service
public class TransferService {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private TransactionLogRepo transactionRepo;

    @Transactional
    public void transfer(TransferRequest request) {
        // 1. Check for Duplicate Transfer (Idempotency)
        if (transactionRepo.existsByIdempotencyKey(request.getIdempotencyKey())) {
            throw new DuplicateTransferException();
        }
        validateTransfer(request);
        executeTransfer(request);
    }

    private void validateTransfer(TransferRequest request) {
        if (request.getAmount() <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        if (request.getFromAccountId() == request.getToAccountId()) {
            throw new IllegalArgumentException("Source and destination accounts cannot be the same");
        }
    }

    private void executeTransfer(TransferRequest request) {
        Account from = accountRepo.findById(request.getFromAccountId())
                .orElseThrow(() -> new RuntimeException("Source account not found"));
        Account to = accountRepo.findById(request.getToAccountId())
                .orElseThrow(() -> new RuntimeException("Destination account not found"));

        if (!from.debit(request.getAmount())) {
            // Log Failure
            logTransaction(request, TransactionLog.TransactionStatus.FAILED, "Insufficient funds");
            throw new RuntimeException("Insufficient funds");
        }

        to.credit(request.getAmount());

        accountRepo.save(from);
        accountRepo.save(to);

        logTransaction(request, TransactionLog.TransactionStatus.SUCCESS, null);
    }

    private void logTransaction(TransferRequest req, TransactionLog.TransactionStatus status, String reason) {
        TransactionLog log = new TransactionLog();
        log.setFromAccountId(req.getFromAccountId());
        log.setToAccountId(req.getToAccountId());
        log.setAmount(req.getAmount());
        log.setStatus(status);
        log.setFailureReason(reason);
        transactionRepo.save(log);
    }
}
*/

@Service
public class TransferService {

    @Autowired
    private AccountRepo accountRepo;

    @Autowired
    private TransactionLogRepo transactionRepo;

    @Transactional
    public void transfer(TransferRequest request) {

        if (request.getFromAccountId() == request.getToAccountId()) {
            throw new IllegalArgumentException("Source and destination accounts cannot be the same.");
        }

        // 1. Check for Duplicate Transfer (Idempotency)
        if (transactionRepo.existsByIdempotencyKey(request.getIdempotencyKey())) {
            throw new DuplicateTransferException();
        }

        // 2. Fetch the source account
        Account from = accountRepo.findById(request.getFromAccountId())
                .orElseThrow(() -> new AccountNotFoundException(request.getFromAccountId()));

        // 3. SECURITY CHECK: Does the logged-in user own the source account?
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!from.getOwner().getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("You are not authorized to transfer from this account.");
        }

        //4.
        validateTransfer(request);
        executeTransfer(request);
    }

    private void validateTransfer(TransferRequest request) {
        Account from = accountRepo.findById(request.getFromAccountId())
                .orElseThrow(() -> new AccountNotFoundException(request.getFromAccountId()));

        Account to = accountRepo.findById(request.getToAccountId())
                .orElseThrow(() -> new AccountNotFoundException(request.getToAccountId()));

        if (!from.isActive()) throw new AccountNotActiveException(from.getId());
        if (!to.isActive()) throw new AccountNotActiveException(to.getId());

        if (request.getAmount() <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
    }

    private void executeTransfer(TransferRequest request) {
        Account from = accountRepo.findById(request.getFromAccountId()).get();
        Account to = accountRepo.findById(request.getToAccountId()).get();

        if (!from.debit(request.getAmount())) {
            logTransaction(request, TransactionLog.TransactionStatus.FAILED, "Insufficient funds");
            throw new InsufficientBalanceException();
        }

        to.credit(request.getAmount());
        accountRepo.save(from);
        accountRepo.save(to);

        logTransaction(request, TransactionLog.TransactionStatus.SUCCESS, null);
    }

    private void logTransaction(TransferRequest req, TransactionLog.TransactionStatus status, String reason) {
        TransactionLog log = new TransactionLog();
        log.setFromAccountId(req.getFromAccountId());
        log.setToAccountId(req.getToAccountId());
        log.setAmount(req.getAmount());
        log.setStatus(status);
        log.setFailureReason(reason);
        log.setIdempotencyKey(req.getIdempotencyKey()); // Save the key
        transactionRepo.save(log);
    }
}