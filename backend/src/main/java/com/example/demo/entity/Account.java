package com.example.demo.entity;

import java.math.BigDecimal;
import java.util.*;
import com.example.demo.enums.AccountStatus;
import com.example.demo.exception.AccountNotActiveException;
import com.example.demo.exception.InsufficientBalanceException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@Table (name="accounts")
@Setter // Generates all setters
@NoArgsConstructor // Generates the empty constructor
@AllArgsConstructor // Generates constructor with all fields
@ToString(exclude = "owner") // Generates toString (and prevents infinite loops with relationships)
@EqualsAndHashCode(onlyExplicitlyIncluded = true) // Generates equals/hashcode
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @Column(name="id")
    private Long id;
    @Column(name="holder_name")
    private String holderName;
    @Column(name="balance")
    private BigDecimal balance;
    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private AccountStatus status;

    @Column(name="version")
    private int version;
    @Column(name = "last_updated")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "user_id", columnDefinition="INT")
    @JsonIgnore
    private User owner;

    //include foreign key mapping
    // @OneToMany(mappedBy="users(id)")

    /*
    public Account() {
    }
    */

    @PrePersist
    public void onPrePersist() {
        if (this.updatedAt == null) {
            this.updatedAt = LocalDateTime.now();  // Automatically set the current timestamp
        }
    }

    @PreUpdate
    public void onPreUpdate() {
        this.updatedAt = LocalDateTime.now();
    }


    // Debit method: subtracts amount from balance
    public void debit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        if (!isActive()){
            throw new AccountNotActiveException(id);
        }
        if (balance.compareTo(amount) < 0) {
            throw new InsufficientBalanceException();
        }
        this.balance=this.balance.subtract(amount);
    }

    // Credit method: adds amount to balance
    public void credit(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        if(!isActive()){
            throw new AccountNotActiveException(id);
        }
        this.balance=this.balance.add(amount);
    }

    public boolean isActive() {
        return AccountStatus.ACTIVE.equals(this.status);
    }


}
