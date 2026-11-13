package com.k1onnu.notes_app.service;

import com.k1onnu.notes_app.dto.AuthRequest;
import com.k1onnu.notes_app.exception.ConflictException;
import com.k1onnu.notes_app.exception.NotFoundException;
import com.k1onnu.notes_app.model.User;
import com.k1onnu.notes_app.repository.UserRepository;
import com.k1onnu.notes_app.security.JwtProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private AuthService authService;

    @Test
    void register_ShouldThrowException_WhenUsernameExists() {
        AuthRequest request = new AuthRequest();
        request.setUsername("existingUser");
        request.setPassword("password");

        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        assertThrows(ConflictException.class, () -> authService.register(request));
    }

    @Test
    void register_ShouldReturnToken_WhenValid() {
        AuthRequest request = new AuthRequest();
        request.setUsername("newUser");
        request.setPassword("password");

        when(userRepository.existsByUsername("newUser")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("hashedPassword");
        when(jwtProvider.generateToken("newUser")).thenReturn("fake-jwt-token");

        String token = authService.register(request);

        assertEquals("fake-jwt-token", token);
        verify(userRepository, times(1)).save(any(User.class)); // Проверяем, что юзер был сохранен
    }

    @Test
    void login_ShouldThrowException_WhenUserNotFound() {
        AuthRequest request = new AuthRequest();
        request.setUsername("unknown");
        request.setPassword("password");

        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> authService.login(request));
    }

    @Test
    void login_ShouldThrowException_WhenPasswordIsWrong() {
        AuthRequest request = new AuthRequest();
        request.setUsername("testuser");
        request.setPassword("wrongPassword");

        User user = new User();
        user.setUsername("testuser");
        user.setPasswordHash("correctHashedPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "correctHashedPassword")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authService.login(request));
    }

    @Test
    void login_ShouldReturnToken_WhenCredentialsAreValid() {
        AuthRequest request = new AuthRequest();
        request.setUsername("testuser");
        request.setPassword("correctPassword");

        User user = new User();
        user.setUsername("testuser");
        user.setPasswordHash("correctHashedPassword");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("correctPassword", "correctHashedPassword")).thenReturn(true);
        when(jwtProvider.generateToken("testuser")).thenReturn("fake-jwt-token");

        String token = authService.login(request);

        assertEquals("fake-jwt-token", token);
    }
}
