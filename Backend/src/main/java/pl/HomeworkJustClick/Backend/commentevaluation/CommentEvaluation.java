package pl.HomeworkJustClick.Backend.commentevaluation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.HomeworkJustClick.Backend.comment.Comment;
import pl.HomeworkJustClick.Backend.evaluation.Evaluation;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "_comment_evaluation")
@Getter
@Setter
public class CommentEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id", updatable = false, unique = true, nullable = false)
    @Schema(example = "0")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "evaluation_id", nullable = false, foreignKey = @ForeignKey(name = "comment_evaluation_evaluation_id_fk"))
    @JsonIgnore
    private Evaluation evaluation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false, foreignKey = @ForeignKey(name = "comment_evaluation_comment_id_fk"))
    @JsonIgnore
    private Comment comment;

    @Column(name = "description")
    @Schema(example = "Example desc")
    @Size(max = 255)
    private String description;

    public CommentEvaluation(Evaluation evaluation, Comment comment, String description) {
        this.evaluation = evaluation;
        this.comment = comment;
        this.description = description;
    }
}
