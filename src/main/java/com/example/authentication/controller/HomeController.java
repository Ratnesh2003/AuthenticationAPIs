package com.example.authentication.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api")
public class HomeController {
    @GetMapping("/user")
    public ResponseEntity<?> userRoute() {
        return ResponseEntity.status(HttpStatus.OK).body("This is your user route");
    }

    @GetMapping("/admin")
    public ResponseEntity<?> adminRoute() {
        return ResponseEntity.status(HttpStatus.OK).body("This is your admin route");
    }
}
