package com.example.authentication.repository;

import com.example.authentication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepo extends JpaRepository<User, Long> {
    Boolean existsByEmailIgnoreCase(String email);
    User findByEmail(String email);
    Boolean existsByUsernameIgnoreCase(String username);

}
