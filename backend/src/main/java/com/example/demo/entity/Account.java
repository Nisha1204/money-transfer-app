package com.example.demo.entity;

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
    private int id;
    @Column(name="holder_name")
    private String holderName;
    @Column(name="balance")
    private float balance;
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
    public boolean debit(float amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        if (!isActive()){
            throw new AccountNotActiveException(id);
        }
        if (balance <= amount) {
            throw new InsufficientBalanceException();
        }
        balance -= amount;
        return true;
    }

    // Credit method: adds amount to balance
    public void credit(float amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }
        if(!isActive()){
            throw new AccountNotActiveException(id);
        }
        balance += amount;
    }

    // Is account active? Assuming "ACTIVE" status means the account is active
    public boolean isActive() {
        return this.status.equals(AccountStatus.ACTIVE);

    }


}
