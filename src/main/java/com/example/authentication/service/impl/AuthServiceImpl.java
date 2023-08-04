package com.example.authentication.service.impl;

import com.example.authentication.exceptions.AlreadyExistsException;
import com.example.authentication.exceptions.RoleDoesNotExistException;
import com.example.authentication.exceptions.TokenDoesNotExistException;
import com.example.authentication.model.Role;
import com.example.authentication.model.Token;
import com.example.authentication.model.User;
import com.example.authentication.payload.request.LoginReq;
import com.example.authentication.payload.request.RegisterReq;
import com.example.authentication.payload.response.UserLoginRes;
import com.example.authentication.repository.RoleRepo;
import com.example.authentication.repository.TokenRepo;
import com.example.authentication.repository.UserRepo;
import com.example.authentication.service.AuthService;
import com.example.authentication.service.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final TokenRepo tokenRepo;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;

    @Override
    public void signup(RegisterReq registerReq) throws RoleDoesNotExistException, AlreadyExistsException{
        // Checking if the user already exists
        if (userRepo.existsByEmailIgnoreCase(registerReq.getEmail().trim())) {
            throw new AlreadyExistsException("Email already exists");
        }
        if (userRepo.existsByUsernameIgnoreCase(registerReq.getUsername().trim())) {
            throw new AlreadyExistsException("Username already exists");
        }
        registerReq.setPassword(passwordEncoder.encode(registerReq.getPassword()));
        // Getting the roles from the request and storing them in a set for the user
        Set<Role> roles = new HashSet<>();
        for (String role : registerReq.getRoles()) {
            if (!roleRepo.existsRoleByNameIgnoreCase(role)) {
                throw new RoleDoesNotExistException("Role does not exist");
            } else {
                Role userRole = roleRepo.findByNameIgnoreCase(role);
                roles.add(userRole);
            }
        }

        // Adding the default role as USER if no role is provided
        if (roles.isEmpty()) {
            roles.add(roleRepo.findByNameIgnoreCase("ROLE_USER"));
        }
        User user = new User(
                registerReq.getUsername(),
                registerReq.getEmail().trim(),
                registerReq.getPassword(),
                roles
        );

        // Saving the user in the database
        userRepo.save(user);
    }

    @Override
    @Transactional
    public ResponseEntity<?> login(LoginReq loginReq) {
        // Loading the user from database
        User userDetails = userDetailsService.loadUserByUsername(loginReq.email());
        try {
            if (passwordEncoder.matches(loginReq.password(), userDetails.getPassword())) {
                String accessToken = jwtUtil.generateToken(userDetails);

                // Getting the roles of the user and storing them in a list for response
                List<String> roles = new ArrayList<>();
                for (Role role : userDetails.getRoles()) {
                    roles.add(role.getName());
                }

                // Creating login response for the user
                UserLoginRes userLoginRes = new UserLoginRes(
                        userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail().trim(),
                        accessToken,
                        roles
                );

                // saving token in db
                saveToken(userDetails.getEmail().trim(), accessToken);


                return ResponseEntity.status(HttpStatus.OK).header(HttpHeaders.SET_COOKIE, accessToken).body(
                        userLoginRes
                );
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password incorrect");
            }
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
        }
    }

    public void saveToken(String email, String accessToken) {
        // deleting the previous token if exists
        if (tokenRepo.existsByEmail(email.trim())) {
            tokenRepo.deleteByEmail(email.trim());
        }

        // saving the token in database
        Token token = new Token(email, accessToken, new Date(System.currentTimeMillis()));
        tokenRepo.save(token);
    }

    @Override
    @Transactional
    public void logout(String token) {
        if (!tokenRepo.existsByToken(token)) {
            throw new TokenDoesNotExistException("Token does not exist");
        }
        tokenRepo.deleteByToken(token);
    }

    // Initializing the roles in the database
    @Bean
    public void init() {
        if (roleRepo.existsRoleByNameIgnoreCase("ROLE_ADMIN")) {
            return;
        }
        roleRepo.save(new Role("ROLE_ADMIN"));
        roleRepo.save(new Role("ROLE_USER"));
    }

    // Deleting expired tokens from database using a scheduler after every hour
    @Bean
    @Scheduled(fixedRate = 1000 * 60 * 60)
    @Transactional
    public void deleteExpiredTokens() {
        tokenRepo.deleteExpiredTokens();
    }



}
