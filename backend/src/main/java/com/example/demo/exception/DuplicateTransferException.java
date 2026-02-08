package com.example.demo.exception;

public class DuplicateTransferException extends RuntimeException {
    public DuplicateTransferException() { super("Duplicate transfer detected (Idempotency Key)."); }
}