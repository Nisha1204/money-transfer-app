package com.example.demo.service;


import com.example.demo.dto.AccountResponse;
import com.example.demo.entity.Account;
import com.example.demo.entity.TransactionLog;
import com.example.demo.entity.User;
import com.example.demo.exception.AccountNotFoundException;
import com.example.demo.repository.AccountRepo;
import com.example.demo.repository.TransactionLogRepo;
import com.example.demo.repository.UserRepo;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

//@Component("accountService")
@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger logger = org.slf4j.LoggerFactory.getLogger("accounts-service");
    private final AccountRepo accountRepository ;
    private final TransactionLogRepo transactionRepo;
    private final UserRepo userRepo;

    @Autowired
    public AccountServiceImpl(AccountRepo accountRepository, TransactionLogRepo transactionRepo, UserRepo userRepo){
        this.accountRepository = accountRepository;
        this.transactionRepo = transactionRepo;
        this.userRepo = userRepo;
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
    public void deleteAccount(Long id) {
        logger.info("Deleting Account with ID: {}", id);
        accountRepository.deleteById(id);
    }

    @Override
    public void updateAccount(Long id, BigDecimal balance) {
        logger.info("Updating Account with ID: {} to balance: {} ", id, balance);
        // Changed to use your custom Exception for consistency
        Account acc = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        acc.setBalance(balance);
        accountRepository.save(acc);
    }

    @Override
    public AccountResponse getAccount(Long id) {
        validateOwnership(id);

        logger.info("Getting Account with ID: {}", id);
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(id));

        return mapToResponse(account);
    }

    private AccountResponse mapToResponse(Account account) {
        AccountResponse response = new AccountResponse();
        response.setId(account.getId());
        response.setHolderName(account.getHolderName());
        response.setBalance(account.getBalance());
        response.setStatus(account.getStatus()); // Assumes Account entity has status
        response.setUpdatedAt(account.getUpdatedAt()); // Assumes Account entity has updatedAt
        response.setOwner(account.getOwner());
        return response;
    }

    public BigDecimal getBalance(Long id) {
        // We can call getAccount(id) which now returns a DTO
        return getAccount(id).getBalance();
    }

    public List<TransactionLog> getTransactions(Long id) {
        validateOwnership(id);
        return transactionRepo.findByFromAccountIdOrToAccountId(id, id);
    }

    public void validateOwnership(Long accountId) {
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

    public List<AccountResponse> getAccountsByUsername(String username) {
        // 1. Find the user in DB
        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // 2. Map their accounts to DTOs
        return user.getAccounts().stream()
                .map(account -> new AccountResponse(
                        account.getId(),
                        account.getHolderName(),
                        account.getBalance(),
                        account.getStatus(),
                        account.getUpdatedAt(),
                        account.getOwner() // This maps the User entity to the 'owner' field
                ))
                .collect(Collectors.toList());
    }


}