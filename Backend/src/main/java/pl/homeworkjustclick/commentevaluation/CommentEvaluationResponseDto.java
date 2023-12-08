package pl.homeworkjustclick.commentevaluation;

import lombok.*;
import pl.homeworkjustclick.comment.CommentSimpleResponseDto;
import pl.homeworkjustclick.evaluation.EvaluationResponseDto;

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
