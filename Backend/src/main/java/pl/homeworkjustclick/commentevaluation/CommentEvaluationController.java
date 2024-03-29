package pl.homeworkjustclick.commentevaluation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Comment evaluation", description = "Comment evaluation related calls.")
@ApiResponse(
        responseCode = "403",
        description = "Something is wrong with the token.",
        content = @Content()
)
@ApiResponse(
        responseCode = "200",
        description = "OK."
)
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
            },
            parameters = {
                    @Parameter(name = "page", example = "0", description = "default = 0"),
                    @Parameter(name = "size", example = "20", description = "default = 10"),
                    @Parameter(name = "sort", example = "id,desc", description = "default = id,asc")
            }
    )
    public Slice<CommentEvaluationResponseDto> getCommentEvaluations(@Parameter(hidden = true) @PageableDefault(sort = "id") Pageable pageable) {
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
            summary = "Returns paged list of commentEvaluations by commentId",
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
            },
            parameters = {
                    @Parameter(name = "page", example = "0", description = "default = 0"),
                    @Parameter(name = "size", example = "20", description = "default = 10"),
                    @Parameter(name = "sort", example = "id,desc", description = "default = id,asc")
            }
    )
    public Slice<CommentEvaluationResponseDto> getCommentEvaluationsByCommentId(@PathVariable Integer commentId, @Parameter(hidden = true) @PageableDefault(sort = "id") Pageable pageable) {
        return service.getCommentEvaluationsByCommentId(commentId, pageable);
    }

    @GetMapping("byEvaluationId/{evaluationId}")
    @Operation(
            summary = "Returns paged list of commentEvaluations by evaluationId",
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
            },
            parameters = {
                    @Parameter(name = "page", example = "0", description = "default = 0"),
                    @Parameter(name = "size", example = "20", description = "default = 10"),
                    @Parameter(name = "sort", example = "id,desc", description = "default = id,asc")
            }
    )
    public Slice<CommentEvaluationResponseDto> getCommentEvaluationsByEvaluationId(@PathVariable Integer evaluationId, @Parameter(hidden = true) @PageableDefault(sort = "id") Pageable pageable) {
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
