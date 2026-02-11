package com.example.demo;

import com.example.demo.entity.Account;
import com.example.demo.enums.AccountStatus;
import com.example.demo.exception.InsufficientBalanceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AccountTest {

    private Account account;

    @BeforeEach
    void setUp() {
        account = new Account();
        account.setBalance(new java.math.BigDecimal("100.00"));
        account.setStatus(AccountStatus.ACTIVE);
    }

    @Test
    @DisplayName("Should decrease balance when funds are sufficient")
    void testDebit_Success() {
        account.debit(new java.math.BigDecimal("40.00"));
        assertEquals(new java.math.BigDecimal("60.00"), account.getBalance(), "Balance should be 60.0");
    }

    @Test
    @DisplayName("Should throw exception when funds are insufficient")
    void testDebit_InsufficientBalance() {
        // Wrap the call in assertThrows because your Account.java throws an exception
        assertThrows(InsufficientBalanceException.class, () -> {
            account.debit(new java.math.BigDecimal("150.00"));
        });

        // Verify balance remained 100.00
        assertEquals(new java.math.BigDecimal("100.00"), account.getBalance(), "Balance should remain unchanged");
    }

    @Test
    @DisplayName("Should increase balance on credit")
    void testCredit_Success() {
        account.credit(new java.math.BigDecimal("50.00"));
        assertEquals(new java.math.BigDecimal("150.00"), account.getBalance());
    }
}