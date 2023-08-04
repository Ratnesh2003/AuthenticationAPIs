package com.example.authentication.controller;

import com.example.authentication.exceptions.AlreadyExistsException;
import com.example.authentication.exceptions.RoleDoesNotExistException;
import com.example.authentication.payload.request.*;
import com.example.authentication.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody RegisterReq registerReq) {
        try {
            this.authService.signup(registerReq);
            return ResponseEntity.status(HttpStatus.OK).body("Registered successfully");
        } catch (AlreadyExistsException | RoleDoesNotExistException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginReq loginReq) {
        return this.authService.login(loginReq);
    }

    @PostMapping("/logout/{token}")
    public ResponseEntity<?> logout(@PathVariable String token) {
        try {
            this.authService.logout(token);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Logged out successfully");
    }


}
