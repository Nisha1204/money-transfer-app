package com.example.demo.dto;

import com.example.demo.entity.User;
import com.example.demo.enums.AccountStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private int id;

    private String holderName;

    private float balance;

    private AccountStatus status;

    private LocalDateTime updatedAt;

    private User owner;
}
