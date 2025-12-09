package com.cmed.medic.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.cmed.medic.auth.dto.UserRequest;
import com.cmed.medic.auth.service.AuthService;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody UserRequest req) {
        String message = authService.register(req.username(), req.password());
        return ResponseEntity.ok(Map.of("message", message));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserRequest req) {
        String token = authService.login(req.username(), req.password());
        return ResponseEntity.ok(Map.of("token", token));
    }
}
