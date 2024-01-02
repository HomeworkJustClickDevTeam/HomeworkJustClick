package pl.homeworkjustclick.evaluationreport;

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

import java.util.List;

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
            summary = "Returns list of evaluationReports by evaluationId.",
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
            }
    )
    public EvaluationReportResponseDto getEvaluationReportByEvaluationId(@PathVariable Integer evaluationId) {
        return service.getEvaluationReportByEvaluationId(evaluationId);
    }

    @GetMapping("byTeacherId/{teacherId}")
    @Operation(
            summary = "Returns list of evaluationReports by teacherId.",
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
            }
    )
    public List<EvaluationReportResponseDto> getEvaluationReportByTeacherId(@PathVariable Integer teacherId) {
        return service.getEvaluationReportsByTeacherId(teacherId);
    }

    @GetMapping("byStudentId/{studentId}")
    @Operation(
            summary = "Returns list of evaluationReports by studentId.",
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
            }
    )
    public List<EvaluationReportResponseDto> getEvaluationReportByStudentId(@PathVariable Integer studentId) {
        return service.getEvaluationReportsByStudentId(studentId);
    }

    @GetMapping("byGroupId/{groupId}")
    @Operation(
            summary = "Returns list of evaluationReports by groupId.",
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
            }
    )
    public List<EvaluationReportResponseDto> getEvaluationReportByGroupId(@PathVariable Integer groupId) {
        return service.getEvaluationReportsByGroupId(groupId);
    }

    @GetMapping("byTeacherIdAndGroupId/{teacherId}/{groupId}")
    @Operation(
            summary = "Returns list of evaluationReports by teacherId and groupId.",
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
            }
    )
    public List<EvaluationReportResponseDto> getEvaluationReportByTeacherIdAndGroupId(@PathVariable Integer teacherId, @PathVariable Integer groupId) {
        return service.getEvaluationReportsByTeacherIdAndGroupId(teacherId, groupId);
    }

    @GetMapping("byStudentIdAndGroupId/{studentId}/{groupId}")
    @Operation(
            summary = "Returns list of evaluationReports by studentId and groupId.",
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
            }
    )
    public List<EvaluationReportResponseDto> getEvaluationReportByStudentIdAndGroupId(@PathVariable Integer studentId, @PathVariable Integer groupId) {
        return service.getEvaluationReportsByStudentIdAndGroupId(studentId, groupId);
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
