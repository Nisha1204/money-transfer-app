package com.example.demo.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferResponse {
    private String message;
    private String transactionStatus;
    private float amountTransferred;
    private float remainingBalance;
    private LocalDateTime timestamp;
}