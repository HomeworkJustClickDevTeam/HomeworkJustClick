package pl.HomeworkJustClick.Backend.commentevaluation;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/comment_evaluation")
@RequiredArgsConstructor
public class CommentEvaluationController {
    private final CommentEvaluationService service;

    @GetMapping
    public Slice<CommentEvaluationResponseDto> getCommentEvaluations(@PageableDefault(sort = "id") Pageable pageable) {
        return service.getCommentEvaluations(pageable);
    }

    @GetMapping("{id}")
    public CommentEvaluationResponseDto getCommentEvaluationById(@PathVariable Integer id) {
        return service.getCommentEvaluationById(id);
    }

    @GetMapping("byCommentId/{commentId}")
    public Slice<CommentEvaluationResponseDto> getCommentEvaluationsByCommentId(@PathVariable Integer commentId, @PageableDefault(sort = "id") Pageable pageable) {
        return service.getCommentEvaluationsByCommentId(commentId, pageable);
    }

    @GetMapping("byEvaluationId/{evaluationId}")
    public Slice<CommentEvaluationResponseDto> getCommentEvaluationsByEvaluationId(@PathVariable Integer evaluationId, @PageableDefault(sort = "id") Pageable pageable) {
        return service.getCommentEvaluationsByEvaluationId(evaluationId, pageable);
    }

    @PostMapping
    public CommentEvaluationResponseDto createCommentEvaluation(@RequestBody @Valid CommentEvaluationDto commentEvaluationDto) {
        return service.addCommentEvaluation(commentEvaluationDto);
    }

    @DeleteMapping("{id}")
    public void deleteCommentEvaluation(@PathVariable Integer id) {
        service.deleteCommentEvaluation(id);
    }
}
