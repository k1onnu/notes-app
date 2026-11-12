package com.k1onnu.notes_app.controller;

import com.k1onnu.notes_app.dto.AuthRequest;
import com.k1onnu.notes_app.dto.AuthResponse;
import com.k1onnu.notes_app.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthResponse register(@Valid @RequestBody AuthRequest request) {
        String token = authService.register(request);
        return new AuthResponse(token);
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {
        String token = authService.login(request);
        return new AuthResponse(token);
    }
}
