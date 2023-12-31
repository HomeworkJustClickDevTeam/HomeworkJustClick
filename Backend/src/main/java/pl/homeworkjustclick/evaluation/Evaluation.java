package pl.homeworkjustclick.evaluation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pl.homeworkjustclick.commentevaluation.CommentEvaluation;
import pl.homeworkjustclick.evaluationreport.EvaluationReport;
import pl.homeworkjustclick.group.Group;
import pl.homeworkjustclick.solution.Solution;
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
@Table(name = "_evaluation")
@Getter
@Setter
public class Evaluation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    @Schema(example = "0")
    private Integer id;

    @Column(name = "result")
    @Schema(example = "0.0")
    private Double result;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false, foreignKey = @ForeignKey(name = "evaluation_user_id_fk"))
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "solution_id", nullable = false, foreignKey = @ForeignKey(name = "evaluation_solution_id_fk"))
    @JsonIgnore
    private Solution solution;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "group_id", nullable = false, foreignKey = @ForeignKey(name = "evaluation_group_id_fk"))
    @JsonIgnore
    private Group group;

    @Column(name = "creationDatetime", updatable = false, nullable = false)
    @CreationTimestamp
    private OffsetDateTime creationDatetime;

    @Column(name = "lastModifiedDatetime")
    @UpdateTimestamp
    private OffsetDateTime lastModifiedDatetime;

    @Column(name = "grade")
    @Schema(example = "3.5")
    private Double grade;

    @OneToMany(
            mappedBy = "evaluation",
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<CommentEvaluation> commentEvaluations = new ArrayList<>();

    @OneToOne(mappedBy = "evaluation")
    private EvaluationReport evaluationReport;

    public Evaluation(Double result, User user, Solution solution, Group group, OffsetDateTime creationDatetime, OffsetDateTime lastModifiedDatetime, Double grade, Boolean reported) {
        this.result = result;
        this.user = user;
        this.solution = solution;
        this.group = group;
        this.creationDatetime = creationDatetime;
        this.lastModifiedDatetime = lastModifiedDatetime;
        this.grade = grade;
    }
}
