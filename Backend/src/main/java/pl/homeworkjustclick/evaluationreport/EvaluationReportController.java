package pl.homeworkjustclick.evaluationreport;

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

@RestController
@RequestMapping("api/evaluation_report")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Evaluation Report", description = "Evaluation Report related calls.")
@ApiResponse(
        responseCode = "403",
        description = "Something is wrong with the token.",
        content = @Content()
)
@ApiResponse(
        responseCode = "200",
        description = "OK."
)
public class EvaluationReportController {
    private final EvaluationReportService service;

    @GetMapping("byEvaluationId/{evaluationId}")
    @Operation(
            summary = "Returns paged list of evaluationReports by evaluationId.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EvaluationReportResponseDto.class))
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
                    @Parameter(name = "sort", example = "comment,desc", description = "default = id,asc")
            }
    )
    public EvaluationReportResponseDto getEvaluationReportByEvaluationId(@PathVariable Integer evaluationId, @Parameter(hidden = true) @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return service.getEvaluationReportByEvaluationId(evaluationId);
    }

    @GetMapping("byTeacherId/{teacherId}")
    @Operation(
            summary = "Returns paged list of evaluationReports by teacherId.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EvaluationReportResponseDto.class))
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
                    @Parameter(name = "sort", example = "comment,desc", description = "default = id,asc")
            }
    )
    public Slice<EvaluationReportResponseDto> getEvaluationReportByTeacherId(@PathVariable Integer teacherId, @Parameter(hidden = true) @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return service.getEvaluationReportsByTeacherId(teacherId, pageable);
    }

    @GetMapping("byStudentId/{studentId}")
    @Operation(
            summary = "Returns paged list of evaluationReports by studentId.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EvaluationReportResponseDto.class))
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
                    @Parameter(name = "sort", example = "comment,desc", description = "default = id,asc")
            }
    )
    public Slice<EvaluationReportResponseDto> getEvaluationReportByStudentId(@PathVariable Integer studentId, @Parameter(hidden = true) @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return service.getEvaluationReportsByStudentId(studentId, pageable);
    }

    @GetMapping("byGroupId/{groupId}")
    @Operation(
            summary = "Returns paged list of evaluationReports by groupId.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EvaluationReportResponseDto.class))
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
                    @Parameter(name = "sort", example = "comment,desc", description = "default = id,asc")
            }
    )
    public Slice<EvaluationReportResponseDto> getEvaluationReportByGroupId(@PathVariable Integer groupId, @Parameter(hidden = true) @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return service.getEvaluationReportsByGroupId(groupId, pageable);
    }

    @GetMapping("byTeacherIdAndGroupId/{teacherId}/{groupId}")
    @Operation(
            summary = "Returns paged list of evaluationReports by teacherId and groupId.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EvaluationReportResponseDto.class))
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
                    @Parameter(name = "sort", example = "comment,desc", description = "default = id,asc")
            }
    )
    public Slice<EvaluationReportResponseDto> getEvaluationReportByTeacherIdAndGroupId(@PathVariable Integer teacherId, @PathVariable Integer groupId, @Parameter(hidden = true) @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return service.getEvaluationReportsByTeacherIdAndGroupId(teacherId, groupId, pageable);
    }

    @GetMapping("byStudentIdAndGroupId/{studentId}/{groupId}")
    @Operation(
            summary = "Returns paged list of evaluationReports by studentId and groupId.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EvaluationReportResponseDto.class))
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
                    @Parameter(name = "sort", example = "comment,desc", description = "default = id,asc")
            }
    )
    public Slice<EvaluationReportResponseDto> getEvaluationReportByStudentIdAndGroupId(@PathVariable Integer studentId, @PathVariable Integer groupId, @Parameter(hidden = true) @PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable) {
        return service.getEvaluationReportsByStudentIdAndGroupId(studentId, groupId, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Creates evaluation report",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EvaluationReportResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Evaluation not found",
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
    public EvaluationReportResponseDto createEvaluationReport(@RequestBody @Valid EvaluationReportDto evaluationReportDto) {
        return service.createEvaluationReport(evaluationReportDto);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "Updates evaluation report",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EvaluationReportResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Evaluation not found",
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
    public EvaluationReportResponseDto updateEvaluationReport(@PathVariable Integer id, @RequestBody @Valid EvaluationReportDto evaluationReportDto) {
        return service.updateEvaluationReport(id, evaluationReportDto);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Deletes evaluation report with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing evaluation report with this id.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public void deleteEvaluationReport(@PathVariable Integer id) {
        service.deleteEvaluationReport(id);
    }
}
