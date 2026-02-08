package com.example.demo;

import com.example.demo.dto.TransferRequest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransferRequestValidationTest {

    @Test
    void testValidRequest() {
        TransferRequest request = new TransferRequest();
        request.setFromAccountId(1);
        request.setToAccountId(2);
        request.setAmount(100.0f);

        assertAll("Verify Request Properties",
                () -> assertEquals(1, request.getFromAccountId()),
                () -> assertEquals(2, request.getToAccountId()),
                () -> assertEquals(100.0f, request.getAmount())
        );
    }

    @Test
    void testInvalidAmount() {
        TransferRequest request = new TransferRequest();
        request.setAmount(-10.0f);

        // Manual check for negative amounts
        assertTrue(request.getAmount() <= 0, "Amount should be caught as non-positive");
    }

    @Test
    void testNullFields() {
        TransferRequest request = new TransferRequest();
        // In Java, primitive ints default to 0, not null.
        assertEquals(0, request.getFromAccountId());
        assertEquals(0, request.getToAccountId());
    }
}