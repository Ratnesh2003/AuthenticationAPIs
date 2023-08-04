package com.example.authentication.service;

import com.example.authentication.payload.request.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public interface AuthService {
    void signup(RegisterReq registerReq);
    ResponseEntity<?> login(LoginReq loginReq);
    void logout(String token);

}
