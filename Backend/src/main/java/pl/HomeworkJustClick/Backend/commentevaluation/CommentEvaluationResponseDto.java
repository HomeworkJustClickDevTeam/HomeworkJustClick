package pl.HomeworkJustClick.Backend.commentevaluation;

import lombok.*;
import pl.HomeworkJustClick.Backend.comment.CommentSimpleResponseDto;
import pl.HomeworkJustClick.Backend.evaluation.EvaluationResponseDto;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentEvaluationResponseDto implements Serializable {
    private Integer id;
    private CommentSimpleResponseDto comment;
    private EvaluationResponseDto evaluation;
    private String description;
}
