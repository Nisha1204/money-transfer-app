package com.example.demo.repository;

import com.example.demo.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface TransactionLogRepo extends JpaRepository<TransactionLog, String> {
    List<TransactionLog> findByFromAccountIdOrToAccountId(Long fromId, Long toId);

    Optional<TransactionLog> findByIdempotencyKey(String idempotencyKey);
    boolean existsByIdempotencyKey(String idempotencyKey);
}