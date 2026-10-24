package com.k1onnu.notes_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class NoteRequest {
    @NotBlank(message = "Заголовок не может быть пустым")
    @Size(max = 255, message = "Заголовок слишком длинный")
    private String title;
    private String content;
}
