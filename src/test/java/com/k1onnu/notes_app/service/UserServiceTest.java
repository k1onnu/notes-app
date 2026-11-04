package com.k1onnu.notes_app.service;

import com.k1onnu.notes_app.dto.UserRequest;
import com.k1onnu.notes_app.exception.ConflictException;
import com.k1onnu.notes_app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void createUser_ShouldThrowException_WhenUsernameExists() {
        UserRequest request = new UserRequest();
        request.setUsername("existingUser");
        request.setPasswordHash("1234");

        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.createUser(request));
    }
}
