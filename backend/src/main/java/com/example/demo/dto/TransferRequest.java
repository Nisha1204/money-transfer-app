package com.example.demo.dto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {
    private int fromAccountId;
    private int toAccountId;
    private float amount;
    private int idempotencyKey;
}