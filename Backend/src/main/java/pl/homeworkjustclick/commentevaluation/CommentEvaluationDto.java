package pl.homeworkjustclick.commentevaluation;

import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentEvaluationDto implements Serializable {
    private Integer id;
    @NonNull
    private Integer commentId;
    @NonNull
    private Integer evaluationId;
    @Size(max = 255)
    private String description;
}
