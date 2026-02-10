package com.example.demo.service;

import com.example.demo.dto.TransferRequest;
import com.example.demo.entity.TransactionLog;
import com.example.demo.enums.TransactionStatus;
import com.example.demo.repository.TransactionLogRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final TransactionLogRepo transactionRepo;
    private static final Logger logger = org.slf4j.LoggerFactory.getLogger("audit-service");

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logTransaction(String transactionId, TransferRequest req, TransactionStatus status, String reason) {
        try {
            TransactionLog log = new TransactionLog();
            log.setId(transactionId);
            log.setFromAccountId(req.getFromAccountId());
            log.setToAccountId(req.getToAccountId());
            log.setAmount(req.getAmount());
            log.setStatus(status);
            log.setFailureReason(reason);
            log.setIdempotencyKey(req.getIdempotencyKey());

            // saveAndFlush ensures the SQL is executed within this try block
            transactionRepo.saveAndFlush(log);
        } catch (Exception e) {
            // Since this is Propagation.REQUIRES_NEW, this failure
            // won't roll back the main money transfer transaction.
            logger.error("CRITICAL: Audit log failed for Transaction ID: {}. Reason: {}",
                    transactionId, e.getMessage());
        }
    }
}