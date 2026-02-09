package com.example.demo.entity;

import com.example.demo.enums.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name = "fromAccountId", nullable = false)
    private int fromAccountId;

    @Column(name = "toAccountId", nullable = false)
    private int toAccountId;

    @Column(nullable = false)
    private float amount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "failureReason")
    private String failureReason;

    private int idempotencyKey;

    //@Column(name = "createdOn", insertable = false, updatable = false)
    @Column(name = "createdOn", nullable = false)
    private LocalDateTime createdOn;

    @PrePersist
    protected void onCreate() {
        this.createdOn = LocalDateTime.now();
    }

}