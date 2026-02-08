package com.example.demo.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(int id) { super("Account ID " + id + " not found."); }
}