package pl.homeworkjustclick.evaluationpanelassignment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/evaluation_panel_assignment")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Evaluation panel assignment", description = "Evaluation panel assignment related calls.")
public class EvaluationPanelAssignmentController {
    private final EvaluationPanelAssignmentService service;

    @GetMapping("{userId}/{assignmentId}")
    @Operation(
            summary = "Returns evaluation panel assignment by user id and assignment id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No evaluation panel assignment with this userId and assignmentId in the DB.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EvaluationPanelAssignmentResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public EvaluationPanelAssignmentResponseDto getEvaluationPanelAssignmentByUserIdAndAssignmentId(@PathVariable Integer userId, @PathVariable Integer assignmentId) {
        return service.getEvaluationPanelAssignmentByUserIdAndAssignmentId(userId, assignmentId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Creates evaluation panel assignment",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EvaluationPanelAssignmentResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Evaluation panel or assignment not found",
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
    public EvaluationPanelAssignmentResponseDto createEvaluationPanelAssignment(@RequestBody @Valid EvaluationPanelAssignmentDto evaluationPanelAssignmentDto) {
        return service.createEvaluationPanelAssignment(evaluationPanelAssignmentDto);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Deletes evaluation panel assignment by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Evaluation panel assignment not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public void deleteEvaluationPanelAssignment(@PathVariable Integer id) {
        service.deleteEvaluationPanelAssignment(id);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "Updates evaluation panel assignment",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EvaluationPanelAssignmentResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Evaluation panel or assignment not found",
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
    public EvaluationPanelAssignmentResponseDto updateEvaluationPanelAssignment(@PathVariable Integer id, @RequestBody @Valid EvaluationPanelAssignmentDto evaluationPanelAssignmentDto) {
        return service.updateEvaluationPanelAssignment(id, evaluationPanelAssignmentDto);
    }
}
