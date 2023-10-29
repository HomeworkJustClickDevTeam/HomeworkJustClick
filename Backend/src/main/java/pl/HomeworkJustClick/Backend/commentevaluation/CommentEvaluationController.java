package pl.HomeworkJustClick.Backend.commentevaluation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/comment_evaluation")
@RequiredArgsConstructor
public class CommentEvaluationController {
    private final CommentEvaluationService service;

    @GetMapping
    @Operation(
            summary = "Returns paged list of commentEvaluations",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentEvaluationResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public Slice<CommentEvaluationResponseDto> getCommentEvaluations(@PageableDefault(sort = "id") Pageable pageable) {
        return service.getCommentEvaluations(pageable);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "Returns commentEvaluation by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentEvaluationResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "CommentEvaluation not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public CommentEvaluationResponseDto getCommentEvaluationById(@PathVariable Integer id) {
        return service.getCommentEvaluationById(id);
    }

    @GetMapping("byCommentId/{commentId}")
    @Operation(
            summary = "Returns commentEvaluation by commentId",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentEvaluationResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "CommentEvaluation not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public Slice<CommentEvaluationResponseDto> getCommentEvaluationsByCommentId(@PathVariable Integer commentId, @PageableDefault(sort = "id") Pageable pageable) {
        return service.getCommentEvaluationsByCommentId(commentId, pageable);
    }

    @GetMapping("byEvaluationId/{evaluationId}")
    @Operation(
            summary = "Returns commentEvaluation by evaluationId",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentEvaluationResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "CommentEvaluation not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public Slice<CommentEvaluationResponseDto> getCommentEvaluationsByEvaluationId(@PathVariable Integer evaluationId, @PageableDefault(sort = "id") Pageable pageable) {
        return service.getCommentEvaluationsByEvaluationId(evaluationId, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Creates commentEvaluation",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentEvaluationResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Comment or evaluation not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid dto",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public CommentEvaluationResponseDto createCommentEvaluation(@RequestBody @Valid CommentEvaluationDto commentEvaluationDto) {
        return service.addCommentEvaluation(commentEvaluationDto);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Deletes commentEvaluation",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "CommentEvaluation not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public void deleteCommentEvaluation(@PathVariable Integer id) {
        service.deleteCommentEvaluation(id);
    }
}
