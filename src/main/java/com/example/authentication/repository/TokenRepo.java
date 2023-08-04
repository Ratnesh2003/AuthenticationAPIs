package com.example.authentication.repository;

import com.example.authentication.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TokenRepo extends JpaRepository<Token, Long> {
    Boolean existsByToken(String token);
    Token findByToken(String token);
    void deleteByToken(String token);
    Boolean existsByEmail(String email);
    void deleteByEmail(String email);

    // Tokens will be deleted after 5 days of their creation
    @Modifying
    @Query(value = "DELETE FROM tokens t WHERE t.date_created < NOW() - INTERVAL 5 DAY", nativeQuery = true)
    void deleteExpiredTokens();
}
