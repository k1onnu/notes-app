package com.k1onnu.notes_app.dto;

import lombok.Data;

@Data
public class NotePatchRequest {
    private String title;
    private String content;
}
