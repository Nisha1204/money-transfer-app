package com.example.demo.dto;
import lombok.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequest {
    @NotNull(message = "From account ID is required")
    private Long fromAccountId;
    @NotNull(message = "To account ID is required")
    private Long toAccountId;
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    private BigDecimal amount;
    @NotNull(message = "Idempotency key is required")
    private String idempotencyKey;
}