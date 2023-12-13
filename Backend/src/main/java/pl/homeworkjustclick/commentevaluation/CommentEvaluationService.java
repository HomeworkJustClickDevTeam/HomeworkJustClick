package pl.homeworkjustclick.commentevaluation;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import pl.homeworkjustclick.comment.CommentService;
import pl.homeworkjustclick.comment.CommentUtilsService;
import pl.homeworkjustclick.evaluation.EvaluationService;
import pl.homeworkjustclick.infrastructure.exception.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class CommentEvaluationService {
    private final CommentEvaluationRepository repository;
    private final CommentEvaluationMapper mapper;
    private final CommentService commentService;
    private final EvaluationService evaluationService;
    private final CommentUtilsService commentUtilsService;

    public Slice<CommentEvaluationResponseDto> getCommentEvaluations(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::map);
    }

    public CommentEvaluation findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CommentEvaluation with id = " + id + " not found"));
    }

    public CommentEvaluationResponseDto getCommentEvaluationById(Integer id) {
        return mapper.map(findById(id));
    }

    public Slice<CommentEvaluationResponseDto> getCommentEvaluationsByCommentId(Integer commentId, Pageable pageable) {
        return repository.getCommentEvaluationsByCommentId(commentId, pageable)
                .map(mapper::map);
    }

    public Slice<CommentEvaluationResponseDto> getCommentEvaluationsByEvaluationId(Integer evaluationId, Pageable pageable) {
        return repository.getCommentEvaluationsByEvaluationId(evaluationId, pageable)
                .map(mapper::map);
    }

    public CommentEvaluationResponseDto addCommentEvaluation(CommentEvaluationDto commentEvaluationDto) {
        var commentEvaluation = mapper.map(commentEvaluationDto);
        setRelationFields(commentEvaluationDto, commentEvaluation);
        commentUtilsService.update(commentEvaluationDto.getCommentId());
        return mapper.map(repository.save(commentEvaluation));
    }

    public void deleteCommentEvaluation(Integer id) {
        var commentEvaluation = findById(id);
        repository.delete(commentEvaluation);
    }

    private void setRelationFields(CommentEvaluationDto commentEvaluationDto, CommentEvaluation commentEvaluation) {
        var comment = commentService.findById(commentEvaluationDto.getCommentId());
        var evaluation = evaluationService.findById(commentEvaluationDto.getEvaluationId());
        commentEvaluation.setComment(comment);
        commentEvaluation.setEvaluation(evaluation);
    }
}
