package com.k1onnu.notes_app.repository;

import com.k1onnu.notes_app.model.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NoteRepository extends JpaRepository<Note, Long> {

    Page<Note> findByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Note> findByOwnerId(Long ownerId, Pageable pageable);
    Page<Note> findByContentContainingIgnoreCase(String content, Pageable pageable);
    Page<Note> findByOwnerIdAndTitleContainingIgnoreCase(Long ownerId, String title, Pageable pageable);

    // Для статистики
    @Query("SELECT u.username, COUNT(n) FROM Note n JOIN n.owner u GROUP BY u.username")
    List<Object[]> countNotesByUser();

    @Query("SELECT u.username, COUNT(n) FROM Note n JOIN n.owner u GROUP BY u.username ORDER BY COUNT(n) DESC")
    List<Object[]> findTopUsers(Pageable pageable);

    // Для шаринга (в shareNote)
    @Query("SELECT n FROM Note n JOIN FETCH n.owner WHERE n.id = :id")
    Optional<Note> findByIdWithOwner(@Param("id") Long id);

    // Главный метод для получения своих + расшаренных (с подгрузкой владельца)
    @Query("SELECT n FROM Note n JOIN FETCH n.owner " +
            "WHERE n.owner.id = :userId " +
            "OR n.id IN (SELECT na.note.id FROM NoteAccess na WHERE na.user.id = :userId)")
    Page<Note> findAllAccessibleToUser(@Param("userId") Long userId, Pageable pageable);
}