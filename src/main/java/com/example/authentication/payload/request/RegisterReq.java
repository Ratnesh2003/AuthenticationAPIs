package com.example.authentication.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class RegisterReq {
    private String username;
    private String email;
    private String password;
    private List<String> roles;
}
