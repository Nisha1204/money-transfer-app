package com.example.demo.service;


import com.example.demo.entity.Account;
import com.example.demo.entity.TransactionLog;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.exception.InsufficientBalanceException;
import com.example.demo.repository.AccountRepo;
import com.example.demo.repository.TransactionLogRepo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

//@Component("accountService")
@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger("accounts-service");
    private final AccountRepo accountRepository ;
    private final TransactionLogRepo transactionRepo;

    @Autowired
    public AccountServiceImpl(AccountRepo accountRepository, TransactionLogRepo transactionRepo){
        this.accountRepository = accountRepository;
        this.transactionRepo = transactionRepo;
    }

    @Override
    @Transactional
    public void createAccount(String holderName) {
        logger.info("Creating Account with holderName: {}", holderName);
        Account acc = new Account();
        acc.setHolderName(holderName);
        accountRepository.save(acc);
    }

    @Override
    public void deleteAccount(int id) {
        logger.info("Deleting Account with ID: {}", id);
        accountRepository.deleteById(id);
    }

    @Override
    public void updateAccount(int id, float balance ) {
        logger.info("Updating Account with ID: {} to balance: {} ", id, balance);
        Account acc = accountRepository.findById(id).orElseThrow(()-> new RuntimeException("Account not found"));
        if (acc != null) {
            acc.setBalance(balance);
            accountRepository.save(acc);
        }
    }

    @Override
    public Account getAccount (int id) {
        validateOwnership(id); // If this fails, the code stops here

        logger.info("Getting Account with ID: {}", id);
        return accountRepository.findById(id).orElseThrow(()-> new RuntimeException("Account not found"));
    }

    /*
    @Override
    @Transactional
    public void transfer(int fromId, int toId, float amount) {
        // MOVE LOG INITIALIZATION TO THE TOP
        TransactionLog log = new TransactionLog();
        log.setFromAccountId(fromId);
        log.setToAccountId(toId);
        log.setAmount(amount);

        try {
            Account fromAcc = accountRepository.findById(fromId)
                    .orElseThrow(() -> new RuntimeException("Sender account not found"));
            Account toAcc = accountRepository.findById(toId)
                    .orElseThrow(() -> new RuntimeException("Receiver account not found"));

            if (fromAcc.debit(amount)) {
                toAcc.credit(amount);
                accountRepository.save(fromAcc);
                accountRepository.save(toAcc);
                log.setStatus(TransactionLog.TransactionStatus.SUCCESS);
            } else {
                throw new RuntimeException("Insufficient balance");
            }
        } catch (Exception e) {
            log.setStatus(TransactionLog.TransactionStatus.FAILED);
            log.setFailureReason(e.getMessage());
            throw e;
        } finally {
            // Now 'fromAccountId' is guaranteed to be set before saving
            transactionRepo.save(log);
        }
    }*/

    @Override
    @Transactional
    public void transfer(int fromId, int toId, float amount) {
        if (fromId == toId) {
            throw new IllegalArgumentException("Source and destination accounts must be different.");
        }

        TransactionLog log = new TransactionLog();
        log.setFromAccountId(fromId);
        log.setToAccountId(toId);
        log.setAmount(amount);

        try {
            Account fromAcc = accountRepository.findById(fromId)
                    .orElseThrow(() -> new AccountNotFoundException(fromId));
            Account toAcc = accountRepository.findById(toId)
                    .orElseThrow(() -> new AccountNotFoundException(toId));

            // Ownership Check (Security)
            String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
            if (!fromAcc.getOwner().getUsername().equals(currentUsername)) {
                throw new AccessDeniedException("Unauthorized: You do not own the source account.");
            }

            if (fromAcc.debit(amount)) {
                toAcc.credit(amount);
                accountRepository.save(fromAcc);
                accountRepository.save(toAcc);
                log.setStatus(TransactionLog.TransactionStatus.SUCCESS);
            } else {
                throw new InsufficientBalanceException();
            }
        } catch (Exception e) {
            log.setStatus(TransactionLog.TransactionStatus.FAILED);
            log.setFailureReason(e.getMessage());
            throw e; // This triggers the rollback of the money movement
        } finally {
            // Use the separate service to ensure the log is saved regardless of rollback
            //Object loggingService;
            //loggingService.saveLog(log);
        }
    }

    // In AccountServiceImpl.java

    public float getBalance(int id) {
        return getAccount(id).getBalance();
    }

    public List<TransactionLog> getTransactions(int id) {
        validateOwnership(id);
        return transactionRepo.findByFromAccountIdOrToAccountId(id, id);
    }

    public void validateOwnership(int accountId) {
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();

        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Check for Null Owner first!
        if (account.getOwner() == null) {
            logger.error("Account {} has no associated owner in the database!", accountId);
            throw new RuntimeException("Account configuration error: No owner found.");
        }

        String ownerName = account.getOwner().getUsername();

        // LOG THESE: This is the only way to know why .equals() is failing
        logger.info("Comparing Logged-in: [{}] with Owner: [{}]", currentUsername, ownerName);

        if (!ownerName.equalsIgnoreCase(currentUsername)) {
            throw new AccessDeniedException("Access Denied: You do not own this account!");
        }
    }


}