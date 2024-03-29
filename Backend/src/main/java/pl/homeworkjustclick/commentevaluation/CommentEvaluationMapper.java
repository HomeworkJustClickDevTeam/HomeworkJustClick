package pl.homeworkjustclick.commentevaluation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.homeworkjustclick.comment.CommentMapper;
import pl.homeworkjustclick.evaluation.EvaluationMapper;

@Component
@RequiredArgsConstructor
public class CommentEvaluationMapper {
    private final CommentMapper commentMapper;
    private final EvaluationMapper evaluationMapper;

    public CommentEvaluationResponseDto map(CommentEvaluation commentEvaluation) {
        return CommentEvaluationResponseDto.builder()
                .id(commentEvaluation.getId())
                .comment(commentMapper.mapToSimpleResponseDto(commentEvaluation.getComment()))
                .evaluation(evaluationMapper.map(commentEvaluation.getEvaluation()))
                .description(commentEvaluation.getDescription())
                .build();
    }

    public CommentEvaluation map(CommentEvaluationDto commentEvaluationDto) {
        return CommentEvaluation.builder()
                .description(commentEvaluationDto.getDescription())
                .build();
    }
}
