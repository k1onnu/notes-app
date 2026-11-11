package com.k1onnu.notes_app.controller;

import com.k1onnu.notes_app.model.NoteAccess;
import com.k1onnu.notes_app.repository.NoteAccessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/access")
@RequiredArgsConstructor
public class NoteAccessController {
    private final NoteAccessRepository noteAccessRepository;

    @GetMapping
    public Page<NoteAccess> getAllAccess(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return noteAccessRepository.findAll(PageRequest.of(page, size));
    }
}
