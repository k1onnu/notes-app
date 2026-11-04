package com.k1onnu.notes_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserStatsResponse {
    private String username;
    private Long noteCount;
}
