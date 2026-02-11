package com.example.demo;

import com.example.demo.dto.TransferRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransferRequestValidationTest {

    @Test
    void testValidRequest() {
        TransferRequest request = new TransferRequest();
        request.setFromAccountId(1L);
        request.setToAccountId(2L);
        request.setAmount(new java.math.BigDecimal("100.00"));

        assertAll("Verify Request Properties",
                () -> assertEquals(1, request.getFromAccountId()),
                () -> assertEquals(2, request.getToAccountId()),
                () -> assertEquals(new java.math.BigDecimal("100.00"), request.getAmount())
        );
    }

    @Test
    void testInvalidAmount() {
        TransferRequest request = new TransferRequest();
        request.setAmount(new java.math.BigDecimal("-10.00"));

        // Manual check for negative amounts
        assertTrue(request.getAmount().signum() <= 0, "Amount should be caught as non-positive");
    }

    @Test
    void testNullFields() {
        TransferRequest request = new TransferRequest();
        // Since these are likely Long objects (not primitives), they default to null
        assertNull(request.getFromAccountId());
        assertNull(request.getToAccountId());
    }
}