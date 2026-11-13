package com.k1onnu.notes_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ShareRequest {
    @NotBlank(message = "Имя пользователя обязательно")
    private String username;

    @NotNull(message = "Уровень доступа обязателен")
    private Boolean canEdit;
}
