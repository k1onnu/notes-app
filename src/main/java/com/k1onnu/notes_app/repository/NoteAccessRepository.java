package com.k1onnu.notes_app.repository;

import com.k1onnu.notes_app.model.NoteAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteAccessRepository extends JpaRepository<NoteAccess, NoteAccess.NoteAccessId> {

    Optional<NoteAccess> findByNoteIdAndUserId(Long noteId, Long userId);

    List<NoteAccess> findAllByNoteId(Long noteId);

    List<NoteAccess> findAllByUserId(Long userId);

    @org.springframework.data.jpa.repository.Modifying
    @org.springframework.transaction.annotation.Transactional
    @org.springframework.data.jpa.repository.Query("DELETE FROM NoteAccess na WHERE na.note.id = :noteId AND na.user.id = :userId")
    void deleteByNoteIdAndUserId(@org.springframework.data.repository.query.Param("noteId") Long noteId, @org.springframework.data.repository.query.Param("userId") Long userId);
}
