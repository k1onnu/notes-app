package com.k1onnu.notes_app.service;

import com.k1onnu.notes_app.dto.NoteRequest;
import com.k1onnu.notes_app.dto.NoteResponse;
import com.k1onnu.notes_app.exception.NotFoundException;
import com.k1onnu.notes_app.model.Note;
import com.k1onnu.notes_app.model.User;
import com.k1onnu.notes_app.repository.NoteRepository;
import com.k1onnu.notes_app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NoteServiceTest {

    @Mock
    private NoteRepository noteRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private NoteService noteService;

    @Test
    void createNote_ShouldReturnSavedNote() {
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        NoteRequest request = new NoteRequest();
        request.setTitle("Test Title");
        request.setContent("Test Content");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        when(noteRepository.save(any(Note.class))).thenAnswer(invocation -> {
            Note note = invocation.getArgument(0);
            note.setId(10L);
            return note;
        });

        NoteResponse response = noteService.createNote(request, "testuser");

        assertNotNull(response);
        assertEquals(10L, response.getId());
        assertEquals("Test Title", response.getTitle());
        assertEquals(1L, response.getOwnerId());
    }

    @Test
    void getNoteById_ShouldThrowException_WhenNotFound() {
        when(noteRepository.findById(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> noteService.getNoteById(99L));
    }

    @Test
    void deleteNote_ShouldThrowException_WhenNotFound() {
        when(noteRepository.existsById(99L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> noteService.deleteNote(99L));
        verify(noteRepository, never()).deleteById(99L);
    }
}
