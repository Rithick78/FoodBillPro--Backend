package com.foodbillpro.controller;

import com.foodbillpro.dto.LoginRequest;
import com.foodbillpro.dto.LoginResponse;
import com.foodbillpro.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;

    @Value("${app.admin.username}")
    private String adminUsername;

    @Value("${app.admin.password}")
    private String adminPassword;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        if (adminUsername.equals(request.getUsername()) &&
                adminPassword.equals(request.getPassword())) {
            String token = jwtUtil.generateToken(request.getUsername());
            return ResponseEntity.ok(
                    new LoginResponse(token, request.getUsername(), "Login successful"));
        }
        return ResponseEntity.status(401)
                .body(new LoginResponse(null, null, "Invalid username or password"));
    }
}