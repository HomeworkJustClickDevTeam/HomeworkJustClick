package pl.HomeworkJustClick.Backend.evaluationpanel;

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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.HomeworkJustClick.Backend.comment.CommentResponseDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/evaluation_panel")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Evaluation panel", description = "Evaluation panel related calls.")
public class EvaluationPanelController {
    private final EvaluationPanelService service;

    @GetMapping("/byUserId/{userId}")
    @Operation(
            summary = "Returns paged list of evaluation panels by user id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No evaluation panel with this userId in the DB.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EvaluationPanelResponseDto.class))
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
                    @Parameter(name = "sort", example = "counter,desc", description = "default = lastUsedDate,desc")
            }
    )
    public Slice<EvaluationPanelResponseDto> getEvaluationPanelsByUserId(@PathVariable Integer userId, @Parameter(hidden = true) @PageableDefault(sort = "lastUsedDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return service.getEvaluationPanelsByUserId(userId, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Creates evaluation panel",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EvaluationPanelResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
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
    public EvaluationPanelResponseDto createEvaluationPanel(@RequestBody @Valid EvaluationPanelDto evaluationPanelDto) {
        return service.createEvaluationPanel(evaluationPanelDto);
    }

    @PostMapping("/use/{id}")
    @Operation(
            summary = "Updates evaluation panel lastUsedDate and increments counter",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Evaluation panel not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public EvaluationPanelResponseDto updateEvaluationPanelCounterAndLastUsedDate(@PathVariable Integer id) {
        return service.updateEvaluationPanelCounterAndLastUsedDate(id);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Deletes evaluation panel by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Comment not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public void deleteEvaluationPanel(@PathVariable Integer id) {
        service.deleteEvaluationPanel(id);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "Updates evaluation panel",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EvaluationPanelResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Evaluation panel or user not found",
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
    public EvaluationPanelResponseDto updateEvaluationPanel(@PathVariable Integer id, @RequestBody @Valid EvaluationPanelDto evaluationPanelDto) {
        return service.updateEvaluationPanel(id, evaluationPanelDto);
    }
}
