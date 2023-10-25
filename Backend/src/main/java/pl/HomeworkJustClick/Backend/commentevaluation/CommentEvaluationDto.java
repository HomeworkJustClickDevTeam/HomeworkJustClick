package pl.HomeworkJustClick.Backend.commentevaluation;

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
    private String description;
}
