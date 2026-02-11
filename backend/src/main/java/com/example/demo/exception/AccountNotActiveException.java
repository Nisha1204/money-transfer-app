package com.example.demo.exception;

public class AccountNotActiveException extends RuntimeException {
    public AccountNotActiveException(Long id) { super("Account " + id + " is not active."); }
}
