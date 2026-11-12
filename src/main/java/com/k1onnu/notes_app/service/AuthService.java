package com.k1onnu.notes_app.service;

import com.k1onnu.notes_app.dto.AuthRequest;
import com.k1onnu.notes_app.exception.ConflictException;
import com.k1onnu.notes_app.exception.NotFoundException;
import com.k1onnu.notes_app.model.User;
import com.k1onnu.notes_app.repository.UserRepository;
import com.k1onnu.notes_app.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    // Регистрация
    public String register(AuthRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("Пользователь с таким именем уже существует");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole("USER");
        userRepository.save(user);

        return jwtProvider.generateToken(user.getUsername());
    }

    // Логин
    public String login(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new RuntimeException("Неверный пароль");
        }

        return jwtProvider.generateToken(user.getUsername());
    }
}