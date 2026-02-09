package com.example.demo.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountResponse {
    private int id;
    private String holderName;
    private float balance;
    private String status;
    private String ownerUsername; // Flattened from User entity
}