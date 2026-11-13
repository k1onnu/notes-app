package com.k1onnu.notes_app.service;

import com.k1onnu.notes_app.dto.*;
import com.k1onnu.notes_app.exception.NotFoundException;
import com.k1onnu.notes_app.exception.ConflictException;
import com.k1onnu.notes_app.model.Note;
import com.k1onnu.notes_app.model.User;
import com.k1onnu.notes_app.model.NoteAccess;
import com.k1onnu.notes_app.repository.NoteRepository;
import com.k1onnu.notes_app.repository.UserRepository;
import com.k1onnu.notes_app.repository.NoteAccessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final NoteAccessRepository noteAccessRepository;
    private final SimpMessagingTemplate messagingTemplate;

    public void shareNote(Long noteId, ShareRequest request, String ownerUsername) {
        Note note = noteRepository.findByIdWithOwner(noteId)
                .orElseThrow(() -> new NotFoundException("Заметка не найдена"));

        if (!note.getOwner().getUsername().equals(ownerUsername)) {
            throw new RuntimeException("Только владелец может делиться заметкой");
        }

        User targetUser = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (targetUser.getUsername().equals(ownerUsername)) {
            throw new ConflictException("Нельзя поделиться заметкой с самим собой");
        }

        if (noteAccessRepository.findByNoteIdAndUserId(noteId, targetUser.getId()).isPresent()) {
            throw new ConflictException("У этого пользователя уже есть доступ");
        }

        NoteAccess access = new NoteAccess();
        NoteAccess.NoteAccessId id = new NoteAccess.NoteAccessId();
        id.setNoteId(noteId);
        id.setUserId(targetUser.getId());

        access.setId(id);
        access.setNote(note);
        access.setUser(targetUser);
        access.setCanEdit(request.getCanEdit());

        noteAccessRepository.save(access);
    }

    // Отзыв доступа
    @Transactional
    public void revokeAccess(Long noteId, String targetUsername, String ownerUsername) {
        Note note = noteRepository.findByIdWithOwner(noteId)
                .orElseThrow(() -> new NotFoundException("Заметка не найдена"));

        if (!note.getOwner().getUsername().equals(ownerUsername)) {
            throw new RuntimeException("Только владелец может отзывать доступ");
        }

        User targetUser = userRepository.findByUsername(targetUsername)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        noteAccessRepository.deleteByNoteIdAndUserId(noteId, targetUser.getId());
    }

    public NoteResponse createNote(NoteRequest request, String username) {
        // Ищем того, кто прислал токен
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Пользователь из токена не найден"));

        Note note = new Note();
        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        note.setOwner(owner);

        Note savedNote = noteRepository.save(note);
        return mapToResponse(savedNote);
    }

    public Page<NoteResponse> getAllNotes(int page, int size, String search, String sortBy, String username) {
        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));

        if (search != null && !search.isEmpty()) {
            return noteRepository.findByOwnerIdAndTitleContainingIgnoreCase(currentUser.getId(), search, pageable)
                    .map(this::mapToResponse);
        }

        return noteRepository.findAllAccessibleToUser(currentUser.getId(), pageable)
                .map(this::mapToResponse);
    }

    // Статистика: все юзеры и количество их заметок
    public List<UserStatsResponse> getStats() {
        return noteRepository.countNotesByUser().stream()
                .map(obj -> new UserStatsResponse((String) obj[0], (Long) obj[1]))
                .collect(Collectors.toList());
    }

    // Отчет Top-N
    public List<UserStatsResponse> getTopUsers(int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        return noteRepository.findTopUsers(pageable).stream()
                .map(obj -> new UserStatsResponse((String) obj[0], (Long) obj[1]))
                .collect(Collectors.toList());
    }

    public NoteResponse getNoteById(Long id) {
        Note note = noteRepository.findByIdWithOwner(id)
                .orElseThrow(() -> new NotFoundException("Заметка не найдена"));

        org.hibernate.Hibernate.initialize(note.getOwner());

        return mapToResponse(note);
    }

    public NoteResponse updateNote(Long id, NoteRequest request, String username) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Заметка не найдена"));

        note.setTitle(request.getTitle());
        note.setContent(request.getContent());
        Note savedNote = noteRepository.save(note);

        NoteResponse response = new NoteResponse();
        response.setId(savedNote.getId());
        response.setTitle(savedNote.getTitle());
        response.setContent(savedNote.getContent());
        response.setCreatedAt(savedNote.getCreatedAt());
        response.setUpdatedAt(savedNote.getUpdatedAt());

        User owner = userRepository.findById(savedNote.getOwner().getId())
                .orElseThrow(() -> new NotFoundException("Владелец не найден"));

        response.setOwnerId(owner.getId());
        response.setOwnerName(owner.getUsername());

        messagingTemplate.convertAndSend("/topic/notes/" + id, response);
        return response;
    }

    public void deleteNote(Long id) {
        if (!noteRepository.existsById(id)) {
            throw new NotFoundException("Заметка не найдена");
        }
        noteRepository.deleteById(id);
    }

    public NoteResponse patchNote(Long id, NotePatchRequest request, String username) {
        Note note = noteRepository.findByIdWithOwner(id)
                .orElseThrow(() -> new NotFoundException("Заметка не найдена"));

        User currentUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        if (!note.getOwner().getUsername().equals(username)) {
            NoteAccess access = noteAccessRepository.findByNoteIdAndUserId(id, currentUser.getId())
                    .orElseThrow(() -> new RuntimeException("У вас нет доступа к этой заметке"));

            if (!access.getCanEdit()) {
                throw new RuntimeException("У вас нет прав на редактирование (только чтение)");
            }
        }

        if (request.getTitle() != null) note.setTitle(request.getTitle());
        if (request.getContent() != null) note.setContent(request.getContent());

        Note savedNote = noteRepository.save(note);

        return mapToResponse(savedNote);
    }

    private NoteResponse mapToResponse(Note note) {
        NoteResponse response = new NoteResponse();
        response.setId(note.getId());
        response.setTitle(note.getTitle());
        response.setContent(note.getContent());
        response.setCreatedAt(note.getCreatedAt());
        response.setUpdatedAt(note.getUpdatedAt());

        User owner = userRepository.findById(note.getOwner().getId()).orElse(null);
        if (owner != null) {
            response.setOwnerId(owner.getId());
            response.setOwnerName(owner.getUsername());
        }

        List<ShareInfoResponse> sharedWith = new java.util.ArrayList<>();
        List<NoteAccess> accessList = noteAccessRepository.findAllByNoteId(note.getId());

        for (NoteAccess access : accessList) {
            User targetUser = userRepository.findById(access.getUser().getId()).orElse(null);
            if (targetUser != null) {
                sharedWith.add(new ShareInfoResponse(targetUser.getUsername(), access.getCanEdit()));
            }
        }
        response.setSharedWith(sharedWith);

        return response;
    }
}