package com.example.demo.entity;

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
    private int id;

    @Column(name = "fromAccountId", nullable = false)
    private int fromAccountId;

    @Column(name = "toAccountId", nullable = false)
    private int toAccountId;

    private float amount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "failureReason")
    private String failureReason;

    private int idempotencyKey;

    //@Column(name = "createdOn", insertable = false, updatable = false)
    @Column(name = "createdOn", nullable = false)
    private LocalDateTime createdOn;

    public enum TransactionStatus {
        SUCCESS, FAILED
    }

    @PrePersist
    protected void onCreate() {
        this.createdOn = LocalDateTime.now();
    }

    /*
    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public int getFromAccountId() { return fromAccountId; }
    public void setFromAccountId(int fromAccountId) { this.fromAccountId = fromAccountId; }
    public int getToAccountId() { return toAccountId; }
    public void setToAccountId(int toAccountId) { this.toAccountId = toAccountId; }
    public float getAmount() { return amount; }
    public void setAmount(float amount) { this.amount = amount; }
    public TransactionStatus getStatus() { return status; }
    public void setStatus(TransactionStatus status) { this.status = status; }
    public String getFailureReason() { return failureReason; }
    public void setFailureReason(String failureReason) { this.failureReason = failureReason; }
    public int getIdempotencyKey() { return idempotencyKey; }
    public void setIdempotencyKey(int idempotencyKey) { this.idempotencyKey = idempotencyKey; }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

     */
}