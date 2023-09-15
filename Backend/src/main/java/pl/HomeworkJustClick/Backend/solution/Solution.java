package pl.HomeworkJustClick.Backend.solution;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pl.HomeworkJustClick.Backend.assignment.Assignment;
import pl.HomeworkJustClick.Backend.evaluation.Evaluation;
import pl.HomeworkJustClick.Backend.file.File;
import pl.HomeworkJustClick.Backend.group.Group;
import pl.HomeworkJustClick.Backend.user.User;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_solution")
@Getter
@Setter
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    @Schema(example = "0")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "Fk_user"))
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false, foreignKey = @ForeignKey(name = "Fk_assignment"))
    @JsonIgnore
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false, foreignKey = @ForeignKey(name = "Fk_group"))
    @JsonIgnore
    private Group group;

    @OneToMany(mappedBy = "solution", orphanRemoval = true)
    @JsonIgnore
    private List<File> files = new ArrayList<>();

    @OneToMany(mappedBy = "solution", orphanRemoval = true)
    @JsonIgnore
    private List<Evaluation> evaluations = new ArrayList<>();

    @Column(name = "creationDatetime", updatable = false, nullable = false)
    @CreationTimestamp
    private OffsetDateTime creationDatetime;

    @Column(name = "lastModifiedDatetime")
    @UpdateTimestamp
    private OffsetDateTime lastModifiedDatetime;

    @Column(name = "comment")
    @Schema(example = "Example comment")
    private String comment;

    public Solution(User user, Assignment assignment, Group group, OffsetDateTime creationDatetime, OffsetDateTime lastModifiedDatetime, String comment) {
        this.user = user;
        this.assignment = assignment;
        this.group = group;
        this.creationDatetime = creationDatetime;
        this.lastModifiedDatetime = lastModifiedDatetime;
        this.comment = comment;
    }
}
