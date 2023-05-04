package pl.HomeworkJustClick.Backend.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_solution")
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "userId")
    private User user;

    @ManyToOne
    @JoinColumn(name = "assignmentId")
    private Assignment assignment;

    @Column(name = "creationDatetime", updatable = false, nullable = false)
    @CreationTimestamp
    private OffsetDateTime creationDatetime;

    @Column(name = "lastModifiedDatetime")
    @UpdateTimestamp
    private OffsetDateTime lastModifiedDatetime;

    public Solution(User user, Assignment assignment){
        this.assignment = assignment;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public OffsetDateTime getCreationDatetime() {
        return creationDatetime;
    }

    public OffsetDateTime getLastModifiedDatetime() {
        return lastModifiedDatetime;
    }
}
