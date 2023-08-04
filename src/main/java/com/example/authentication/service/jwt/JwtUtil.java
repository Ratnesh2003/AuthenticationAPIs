package com.example.authentication.service.jwt;

import com.example.authentication.exceptions.TokenDoesNotExistException;
import com.example.authentication.model.User;
import com.example.authentication.repository.TokenRepo;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

//@Component
@RequiredArgsConstructor
@Service
public class JwtUtil {

    private final TokenRepo tokenRepo;


    //retrieve username from jwt token
    public String getEmailFromToken(String token) {
        if (!tokenRepo.existsByToken(token)) {
            throw new TokenDoesNotExistException("Token does not exist");
        }
        return tokenRepo.findByToken(token).getEmail();
    }


    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        return !tokenRepo.existsByToken(token);
    }

    //generate token for user
    public String generateToken(User userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getEmail());
    }


    //   compaction of the JWT to a URL-safe string
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        String secret = "AuthenticationUsingTheABCDSpringSecurityIamWritingThisJustToExtendTheSizeOfTheSecurityKeyIThinkItShouldWorkNow";
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(key, SignatureAlgorithm.HS512).compact();
    }

    //validate token
    public Boolean validateToken(String token, User userDetails) {
        final String email = getEmailFromToken(token);
        return (email.equals(userDetails.getEmail()) && !isTokenExpired(token));
    }



}

