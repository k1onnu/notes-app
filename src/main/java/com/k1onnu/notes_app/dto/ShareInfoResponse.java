package com.k1onnu.notes_app.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ShareInfoResponse {
    private String username;
    private Boolean canEdit;
}
