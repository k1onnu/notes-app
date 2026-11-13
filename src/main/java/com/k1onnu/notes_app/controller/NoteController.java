package com.k1onnu.notes_app.controller;

import com.k1onnu.notes_app.dto.NotePatchRequest;
import com.k1onnu.notes_app.dto.NoteRequest;
import com.k1onnu.notes_app.dto.NoteResponse;
import com.k1onnu.notes_app.dto.ShareRequest;
import com.k1onnu.notes_app.service.NoteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@RequiredArgsConstructor
public class NoteController {

    private final NoteService noteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NoteResponse createNote(@Valid @RequestBody NoteRequest request, Principal principal) {
        return noteService.createNote(request, principal.getName());
    }

    @GetMapping
    public Page<NoteResponse> getAllNotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "id") String sortBy,
            java.security.Principal principal) {

        return noteService.getAllNotes(page, size, search, sortBy, principal.getName());
    }

    @PostMapping("/{id}/share")
    public void shareNote(@PathVariable Long id, @Valid @RequestBody ShareRequest request, Principal principal) {
        noteService.shareNote(id, request, principal.getName());
    }

    @GetMapping("/stats")
    public List<com.k1onnu.notes_app.dto.UserStatsResponse> getStats() {
        return noteService.getStats();
    }

    @GetMapping("/stats/top")
    public List<com.k1onnu.notes_app.dto.UserStatsResponse> getTopUsers(@RequestParam(defaultValue = "3") int limit) {
        return noteService.getTopUsers(limit);
    }

    @GetMapping("/{id}")
    public NoteResponse getNoteById(@PathVariable Long id) {
        return noteService.getNoteById(id);
    }

    @PutMapping("/{id}")
    public NoteResponse updateNote(@PathVariable Long id, @Valid @RequestBody NoteRequest request, Principal principal) {
        return noteService.updateNote(id, request, principal.getName());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNote(@PathVariable Long id) {
        noteService.deleteNote(id);
    }

    @DeleteMapping("/{id}/share/{username}")
    public void revokeAccess(@PathVariable Long id, @PathVariable String username, Principal principal) {
        noteService.revokeAccess(id, username, principal.getName());
    }

    @PatchMapping("/{id}")
    public NoteResponse patchNote(@PathVariable Long id, @RequestBody NotePatchRequest request, Principal principal) {
        return noteService.patchNote(id, request, principal.getName());
    }
}
