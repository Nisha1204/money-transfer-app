package com.example.demo.service;

import com.example.demo.dto.TransferRequest;
import com.example.demo.dto.TransferResponse;
import com.example.demo.entity.Account;
import com.example.demo.enums.TransactionStatus;
import com.example.demo.exception.*;
import com.example.demo.repository.AccountRepo;
import com.example.demo.repository.TransactionLogRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class TransferServiceImpl implements TransferService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger("transfer-service");
    private final AccountRepo accountRepo;
    private final TransactionLogRepo transactionRepo;
    private final AuditService auditService;

    @Override
    @Transactional(
            // These exceptions will NOT automatically mark the transaction as "rollback-only"
            noRollbackFor = {
                    InsufficientBalanceException.class,
                    AccountNotActiveException.class,
                    AccountNotFoundException.class,
                    DuplicateTransferException.class,
                    IllegalArgumentException.class,  // Important: for validateRequest
                    AccessDeniedException.class      // Important: for security check
            }
    )
    public TransferResponse transfer(TransferRequest request) {
        String transactionId = "TRX-" + java.util.UUID.randomUUID();

        try {
            validateRequest(request);
            return executeSecureTransfer(transactionId, request);

        } catch (Exception ex) {
            log.error("Transfer {} failed: {}", transactionId, ex.getMessage());

            // Because of noRollbackFor, this call can now succeed
            // without the "silent rollback" error.
            auditService.logTransaction(transactionId, request, TransactionStatus.FAILED, ex.getMessage());

            // We still throw the exception so the Controller knows to return an error
            // and any partial DB changes in THIS method are rolled back.
            throw ex;
        }
    }

    private void validateRequest(TransferRequest request) {
        if (request.getFromAccountId().equals(request.getToAccountId())) {
            throw new IllegalArgumentException("Source and destination accounts cannot be the same.");
        }
        // Corrected BigDecimal comparison
        if (request.getAmount().signum() <= 0) {
            throw new IllegalArgumentException("Transfer amount must be positive");
        }
        if (transactionRepo.existsByIdempotencyKey(request.getIdempotencyKey())) {
            throw new DuplicateTransferException(request.getIdempotencyKey());
        }
    }

    private TransferResponse executeSecureTransfer(String transactionId, TransferRequest request) {
        Long firstId = Math.min(request.getFromAccountId(), request.getToAccountId());
        Long secondId = Math.max(request.getFromAccountId(), request.getToAccountId());

        Account firstAccount = accountRepo.findByIdWithLock(firstId)
                .orElseThrow(() -> new AccountNotFoundException(firstId));
        Account secondAccount = accountRepo.findByIdWithLock(secondId)
                .orElseThrow(() -> new AccountNotFoundException(secondId));

        Account from = firstId.equals(request.getFromAccountId()) ? firstAccount : secondAccount;
        Account to = firstId.equals(request.getToAccountId()) ? firstAccount : secondAccount;

        // Security Check
        String currentUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!from.getOwner().getUsername().equals(currentUsername)) {
            throw new AccessDeniedException("Unauthorized to transfer from this account.");
        }

        if (!from.isActive()) throw new AccountNotActiveException(from.getId());
        if (!to.isActive()) throw new AccountNotActiveException(to.getId());

        from.debit(request.getAmount());
        to.credit(request.getAmount());
        accountRepo.save(from);
        accountRepo.save(to);

        // Updated call: Passing the transactionId
        auditService.logTransaction(transactionId, request, TransactionStatus.SUCCESS, null);

        // Build Response
        TransferResponse response = new TransferResponse();
        response.setId(transactionId);
        response.setFromAccountId(request.getFromAccountId());
        response.setToAccountId(request.getToAccountId());
        response.setAmount(request.getAmount());
        response.setStatus(TransactionStatus.SUCCESS);
        response.setMessage("Transfer completed successfully");

        return response;
    }
}