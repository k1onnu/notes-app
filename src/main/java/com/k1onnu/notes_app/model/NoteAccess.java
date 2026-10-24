package com.k1onnu.notes_app.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "note_access")
@Data
@NoArgsConstructor
public class NoteAccess {

    @EmbeddedId
    private NoteAccessId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("noteId")
    @JoinColumn(name = "note_id")
    private Note note;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Boolean canEdit = true;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime sharedAt;

    // Вложенный класс для составного ключа
    @Data
    @NoArgsConstructor
    @Embeddable
    public static class NoteAccessId implements Serializable {
        @Column(name = "note_id")
        private Long noteId;

        @Column(name = "user_id")
        private Long userId;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NoteAccessId that = (NoteAccessId) o;
            return Objects.equals(noteId, that.noteId) && Objects.equals(userId, that.userId);
        }

        @Override
        public int hashCode() {
            return Objects.hash(noteId, userId);
        }
    }
}
