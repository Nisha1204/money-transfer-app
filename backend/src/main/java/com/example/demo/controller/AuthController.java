package com.example.demo.controller;

import com.example.demo.dto.UserRegistrationDto;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth") // Matches your Frontend URL
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private UserService userService;

    // ✅ REGISTER ENDPOINT
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody UserRegistrationDto registrationDto) {
        try {
            userService.registerUser(registrationDto);
            return ResponseEntity.ok(Collections.singletonMap("message", "User registered successfully!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Collections.singletonMap("message", "Registration failed: " + e.getMessage()));
        }
    }

    // ✅ LOGIN ENDPOINT
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserRegistrationDto loginDto) {
        // Check if user exists and password matches
        boolean isValid = userService.validateUser(loginDto.getUsername(), loginDto.getPassword());

        if (isValid) {
            return ResponseEntity.ok(Collections.singletonMap("message", "Login successful"));
        } else {
            return ResponseEntity.status(401).body(Collections.singletonMap("message", "Invalid username or password"));
        }
    }
}