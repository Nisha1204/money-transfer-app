package com.example.demo.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private String errorCode;
    private int status;
    private String message;
    private LocalDateTime timestamp = LocalDateTime.now();



}