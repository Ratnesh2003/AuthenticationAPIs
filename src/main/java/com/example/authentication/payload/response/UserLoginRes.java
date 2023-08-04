package com.example.authentication.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserLoginRes {
    private long id;
    private String username;
    private String email;
    private String accessToken;
    private List<String> roles;
}
