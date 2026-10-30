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

    public List<User> getAllUsers() {
        return userRepository.findAll();
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
}
