package com.k1onnu.notes_app.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserResponse {
    private Long id;
    private String username;
    private String role;
    private LocalDateTime createdAt;
}