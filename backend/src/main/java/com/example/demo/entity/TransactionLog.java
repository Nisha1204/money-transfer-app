package com.example.demo.entity;

import com.example.demo.enums.TransactionStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Persistable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TransactionLog implements Persistable<String> {

    @Id
    // REMOVE @GeneratedValue(strategy = GenerationType.IDENTITY)
    // IDENTITY is for auto-incrementing integers, not UUID strings.
    private String id;

    @Column(name = "fromAccountId", nullable = false)
    private Long fromAccountId;

    @Column(name = "toAccountId", nullable = false)
    private Long toAccountId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "failureReason")
    private String failureReason;

    private String idempotencyKey;

    @Column(name = "createdOn", nullable = false)
    private LocalDateTime createdOn;

    @PrePersist
    protected void onCreate() {
        if (this.createdOn == null) {
            this.createdOn = LocalDateTime.now();
        }
    }

    @Override
    public String getId() {
        return id;
    }

    /**
     * Because we manually set the ID, we must tell Hibernate
     * that the entity is new if the createdOn timestamp hasn't been set yet.
     */
    @Override
    @Transient // Ensure this isn't persisted as a column
    public boolean isNew() {
        return createdOn == null;
    }
}