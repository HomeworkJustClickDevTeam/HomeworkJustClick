package pl.homeworkjustclick.solution;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pl.homeworkjustclick.assignment.Assignment;
import pl.homeworkjustclick.evaluation.Evaluation;
import pl.homeworkjustclick.file.File;
import pl.homeworkjustclick.group.Group;
import pl.homeworkjustclick.user.User;

import java.io.Serializable;
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
public class Solution implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    @Schema(example = "0")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "solution_user_id_fk"))
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assignment_id", nullable = false, foreignKey = @ForeignKey(name = "solution_assignment_id_fk"))
    @JsonIgnore
    private Assignment assignment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false, foreignKey = @ForeignKey(name = "solution_group_id_fk"))
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

    @Column(name = "comment", length = 1500)
    @Schema(example = "Example comment")
    @Size(max = 1500)
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
