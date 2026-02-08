package com.example.demo.repository;

import com.example.demo.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<Account, Integer> {
    // jpa repository has crud operations like add delete etc.
    // Account is the data type, int is the type of primary key 
}

