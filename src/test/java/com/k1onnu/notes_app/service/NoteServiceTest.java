package com.k1onnu.notes_app.service;

import com.k1onnu.notes_app.dto.NoteRequest;
import com.k1onnu.notes_app.dto.NoteResponse;
import com.k1onnu.notes_app.exception.NotFoundException;
import com.k1onnu.notes_app.model.Note;
import com.k1onnu.notes_app.model.User;
import com.k1onnu.notes_app.repository.NoteAccessRepository;
import com.k1onnu.notes_app.repository.NoteRepository;
import com.k1onnu.notes_app.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.messaging.simp.SimpMessagingTemplate;

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

    @Mock
    private NoteAccessRepository noteAccessRepository;

    @Mock
    private SimpMessagingTemplate messagingTemplate;

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
            note.setOwner(user); // ВАЖНО: Прикрепляем владельца, чтобы mapToResponse не упал
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
        when(noteRepository.findByIdWithOwner(99L)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> noteService.getNoteById(99L));
    }

    @Test
    void deleteNote_ShouldThrowException_WhenNotFound() {
        when(noteRepository.existsById(99L)).thenReturn(false);
        assertThrows(NotFoundException.class, () -> noteService.deleteNote(99L));
        verify(noteRepository, never()).deleteById(99L);
    }

    @Test
    void updateNote_ShouldThrowException_WhenUserIsNotOwnerAndHasNoAccess() {
        Note note = new Note();
        note.setId(1L);
        User owner = new User();
        owner.setUsername("owner_user");
        note.setOwner(owner);

        User otherUser = new User();
        otherUser.setId(2L);
        otherUser.setUsername("other_user");

        // Используем lenient(), чтобы Mockito не ругался, если до этих строк не дойдет выполнение
        lenient().when(noteRepository.findById(1L)).thenReturn(Optional.of(note));
        lenient().when(userRepository.findByUsername("other_user")).thenReturn(Optional.of(otherUser));
        lenient().when(noteAccessRepository.findByNoteIdAndUserId(1L, 2L)).thenReturn(Optional.empty());

        NoteRequest request = new NoteRequest();
        request.setTitle("Hacked Title");

        assertThrows(RuntimeException.class, () -> noteService.updateNote(1L, request, "other_user"));
    }
}
