package com.k1onnu.notes_app.service;

import com.k1onnu.notes_app.dto.NotePatchRequest;
import com.k1onnu.notes_app.dto.NoteRequest;
import com.k1onnu.notes_app.dto.NoteResponse;
import com.k1onnu.notes_app.exception.NotFoundException; // Добавлен импорт
import com.k1onnu.notes_app.model.Note;
import com.k1onnu.notes_app.model.User;
import com.k1onnu.notes_app.repository.NoteRepository;
import com.k1onnu.notes_app.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteResponse createNote(NoteRequest request) {
        // Временно берем первого пользователя из БД
        User owner = userRepository.findAll().stream().findFirst()
                .orElseThrow(() -> new NotFoundException("Нет пользователей в БД. Создайте через SQL."));

        Note note = new Note();
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        note.setOwner(owner);

        Note savedNote = noteRepository.save(note);
        return mapToResponse(savedNote);
    }

    public Page<NoteResponse> getAllNotes(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return noteRepository.findAll(pageable).map(this::mapToResponse);
    }

    public NoteResponse getNoteById(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заметка не найдена"));
        return mapToResponse(note);
    }

    public NoteResponse updateNote(Long id, NoteRequest request) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заметка не найдена"));

        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        return mapToResponse(noteRepository.save(note));
    }

    public void deleteNote(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new NotFoundException("Заметка не найдена");
        }
        noteRepository.deleteById(id);
    }

    public NoteResponse patchNote(Long id, NotePatchRequest request) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заметка не найдена"));

        if (request.getTitle() != null) note.setTitle(request.getTitle());
        if (request.getContent() != null) note.setContent(request.getContent());

        return mapToResponse(noteRepository.save(note));
    }

    private NoteResponse mapToResponse(Note note) {
        NoteResponse response = new NoteResponse();
        response.setId(note.getId());
        response.setTitle(note.getTitle());
        response.setContent(note.getContent());
        response.setCreatedAt(note.getCreatedAt());
        response.setUpdatedAt(note.getUpdatedAt());
        response.setOwnerId(note.getOwner().getId());
        return response;
    }
}