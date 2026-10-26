package com.k1onnu.notes_app.controller;

import com.k1onnu.notes_app.dto.NotePatchRequest;
import com.k1onnu.notes_app.dto.NoteRequest;
import com.k1onnu.notes_app.dto.NoteResponse;
import com.k1onnu.notes_app.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteResponse createNote(@Valid @RequestBody NoteRequest request) {
        return noteService.createNote(request);
    }

    @GetMapping
    public org.springframework.data.domain.Page<NoteResponse> getAllNotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return noteService.getAllNotes(page, size);
    }

    @GetMapping("/{id}")
    public NoteResponse getNoteById(@PathVariable Long id) {
        return noteService.getNoteById(id);
    }

    @PutMapping("/{id}")
    public NoteResponse updateNote(@PathVariable Long id, @Valid @RequestBody NoteRequest request) {
        return noteService.updateNote(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
    }

    @PatchMapping("/{id}")
    public NoteResponse patchNote(@PathVariable Long id, @RequestBody NotePatchRequest request) {
        return noteService.patchNote(id, request);
    }
}
