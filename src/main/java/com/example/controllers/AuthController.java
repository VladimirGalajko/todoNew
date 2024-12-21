package com.example.controllers;

import com.example.dto.LoginRequestDTO;
import com.example.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequestDTO loginRequestDTO) {
        if (authService.login(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())) {
            return "Login successful";
        }
        return "Invalid username or password";
    }

    @PostMapping("/logout")
    public String logout() {
        authService.logout();
        return "Logout successful";
    }
//    @Autowired
//    private AuthService authService;
//
//    @PostMapping("/login")
//    public ResponseEntity<String> login(@RequestBody Map<String, String> loginRequest) {
//        String username = loginRequest.get("username");
//        String password = loginRequest.get("password");
//
//        if (authService.login(username, password)) {
//            return ResponseEntity.ok("Login successful");
//        }
//        return ResponseEntity.status(401).body("Invalid username or password");
//    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<String> logout() {
//        authService.logout();
//        return ResponseEntity.ok("Logout successful");
//    }
}
