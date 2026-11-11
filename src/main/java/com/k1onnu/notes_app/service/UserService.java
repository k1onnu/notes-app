package com.k1onnu.notes_app.service;

import com.k1onnu.notes_app.dto.UserRequest;
import com.k1onnu.notes_app.exception.ConflictException;
import com.k1onnu.notes_app.exception.NotFoundException;
import com.k1onnu.notes_app.model.User;
import com.k1onnu.notes_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public org.springframework.data.domain.Page<com.k1onnu.notes_app.dto.UserResponse> getAllUsers(int page, int size) {
        org.springframework.data.domain.Pageable pageable = org.springframework.data.domain.PageRequest.of(page, size);
        return userRepository.findAll(pageable).map(this::mapToResponse);
    }

    public User createUser(UserRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ConflictException("Пользователь с таким именем уже существует");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPasswordHash(request.getPasswordHash());
        user.setRole("USER");

        return userRepository.save(user);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID %d не найден".formatted(id)));
    }

    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        user.setUsername(userDetails.getUsername());
        user.setPasswordHash(userDetails.getPasswordHash());
        user.setRole(userDetails.getRole());
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new NotFoundException("Пользователь с ID %d не найден".formatted(id));
        }
        userRepository.deleteById(id);
    }

    private com.k1onnu.notes_app.dto.UserResponse mapToResponse(User user) {
        com.k1onnu.notes_app.dto.UserResponse response = new com.k1onnu.notes_app.dto.UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setRole(user.getRole());
        response.setCreatedAt(user.getCreatedAt());
        return response;
    }
}
