package com.example.demo.exception;

public class AccountNotActiveException extends RuntimeException {
    public AccountNotActiveException(int id) { super("Account " + id + " is not active."); }
}
