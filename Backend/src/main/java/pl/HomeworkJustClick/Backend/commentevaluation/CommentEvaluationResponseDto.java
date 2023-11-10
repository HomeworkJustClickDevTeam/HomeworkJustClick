package pl.HomeworkJustClick.Backend.commentevaluation;

import lombok.*;
import pl.HomeworkJustClick.Backend.comment.CommentResponseDto;
import pl.HomeworkJustClick.Backend.evaluation.EvaluationResponseDto;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentEvaluationResponseDto implements Serializable {
    private Integer id;
    private CommentResponseDto comment;
    private EvaluationResponseDto evaluation;
    private String description;
}
