package com.example.demo.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(Long id) { super("Account ID " + id + " not found."); }
}