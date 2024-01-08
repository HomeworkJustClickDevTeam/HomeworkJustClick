package pl.homeworkjustclick.assignment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pl.homeworkjustclick.comment.Comment;
import pl.homeworkjustclick.evaluationpanelassignment.EvaluationPanelAssignment;
import pl.homeworkjustclick.file.File;
import pl.homeworkjustclick.group.Group;
import pl.homeworkjustclick.notification.Notification;
import pl.homeworkjustclick.solution.Solution;
import pl.homeworkjustclick.user.User;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_assignment")
@Getter
@Setter
public class Assignment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    @Schema(example = "0")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "assignment_user_id_fk"))
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false, foreignKey = @ForeignKey(name = "assignment_group_id_fk"))
    @JsonIgnore
    private Group group;

    @OneToMany(mappedBy = "assignment", orphanRemoval = true)
    @JsonIgnore
    private List<Solution> solutions = new ArrayList<>();

    @OneToMany(mappedBy = "assignment", orphanRemoval = true)
    @JsonIgnore
    private List<File> files = new ArrayList<>();

    @Column(name = "taskDescription")
    @Schema(example = "Example desc")
    @Size(max = 1500)
    private String taskDescription;

    @Column(name = "creationDatetime", updatable = false, nullable = false)
    @CreationTimestamp
    private OffsetDateTime creationDatetime;

    @Column(name = "lastModifiedDatetime")
    @UpdateTimestamp
    private OffsetDateTime lastModifiedDatetime;

    @Column(name = "completionDatetime")
    private OffsetDateTime completionDatetime;

    @Column(name = "title")
    @Schema(example = "Example title")
    @Size(max = 255)
    private String title;

    @Column(name = "visible", columnDefinition = "boolean default false", nullable = false)
    @Schema(example = "false")
    private Boolean visible = false;

    @Column(name = "max_points")
    @Schema(example = "10")
    private Integer maxPoints;

    @Column(name = "auto_penalty")
    @Schema(example = "50")
    private Integer autoPenalty;

    @OneToMany(
            mappedBy = "assignment",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<EvaluationPanelAssignment> evaluationPanelAssignments = new ArrayList<>();

    @OneToMany(
            mappedBy = "assignment",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(
            mappedBy = "assignment",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Notification> notifications = new ArrayList<>();

    private Boolean advancedEvaluation;

    public Assignment(User user, Group group, String taskDescription, OffsetDateTime creationDatetime, OffsetDateTime lastModifiedDatetime, OffsetDateTime completionDatetime, String title, Boolean visible, int maxPoints, int autoPenalty) {
        this.user = user;
        this.group = group;
        this.taskDescription = taskDescription;
        this.creationDatetime = creationDatetime;
        this.lastModifiedDatetime = lastModifiedDatetime;
        this.completionDatetime = completionDatetime;
        this.title = title;
        this.visible = visible;
        this.maxPoints = maxPoints;
        this.autoPenalty = autoPenalty;
    }
}
