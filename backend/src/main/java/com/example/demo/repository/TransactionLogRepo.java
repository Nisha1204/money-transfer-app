package com.example.demo.repository;

import com.example.demo.entity.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionLogRepo extends JpaRepository<TransactionLog, Integer> {
    List<TransactionLog> findByFromAccountIdOrToAccountId(int fromId, int toId);

    boolean existsByIdempotencyKey(int idempotencyKey);
}