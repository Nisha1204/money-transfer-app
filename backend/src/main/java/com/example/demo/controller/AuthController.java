package com.example.demo.controller;

import com.example.demo.dto.UserRegistrationDto;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

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

    // LOGIN ENDPOINT
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRegistrationDto loginDto) {
        // 1. Fetch the user to check roles and verify password
        Optional<User> userOptional = userService.getUserByUsername(loginDto.getUsername());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // 2. Validate password
            if (userService.validateUser(loginDto.getUsername(), loginDto.getPassword())) {
                // 3. Return user details including role for frontend routing
                Map<String, Object> response = new HashMap<>();
                response.put("id", user.getId());
                response.put("username", user.getUsername());
                response.put("role", user.getRole());
                response.put("message", "Login successful");

                return ResponseEntity.ok(response);
            }
        }

        return ResponseEntity.status(401).body(Collections.singletonMap("message", "Invalid username or password"));
    }
}