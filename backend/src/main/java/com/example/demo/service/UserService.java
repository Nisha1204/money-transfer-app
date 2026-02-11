package com.example.demo.service;

import com.example.demo.dto.UserRegistrationDto;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(UserRegistrationDto dto) {
        User user = new User();
        user.setUsername(dto.getUsername());

        // Encrypting the password using BCrypt
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Assign a default role if none provided
        user.setRole(dto.getRole() != null ? dto.getRole() : "USER");

        userRepository.save(user);
    }
    public boolean validateUser(String username, String password) {
        // 1. Find user by username
        Optional<User> userOptional = userRepository.findByUsername(username);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            // 2. Check if password matches
            // NOTE: In a real app, use BCryptPasswordEncoder here!
            return passwordEncoder.matches(password, user.getPassword());
        }

        return false; // User not found
    }
}
