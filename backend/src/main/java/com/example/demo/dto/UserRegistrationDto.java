package com.example.demo.dto;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto {
    private String username;
    private String password;
    private String role; // Optional: Default to "USER" in the service

    // Getters and Setters
}