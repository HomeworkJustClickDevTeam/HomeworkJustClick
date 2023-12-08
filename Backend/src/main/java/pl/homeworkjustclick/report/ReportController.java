package pl.homeworkjustclick.report;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Report", description = "Report generation related calls.")
@ApiResponse(
        responseCode = "403",
        description = "Something is wrong with the token.",
        content = @Content()
)
@ApiResponse(
        responseCode = "200",
        description = "OK."
)
public class ReportController {
    private final ReportService service;

    @PostMapping("/assignment")
    @Operation(
            summary = "Generate assignment report",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AssignmentReportResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Assignment not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public AssignmentReportResponseDto getAssignmentReport(@RequestBody AssignmentReportDto assignmentReportDto) {
        return service.createAssignmentReport(assignmentReportDto);
    }

    @PostMapping("/group")
    @Operation(
            summary = "Generate group report",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GroupReportResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Group not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public GroupReportResponseDto getGroupReport(@RequestBody GroupReportDto groupReportDto) {
        return service.createGroupReport(groupReportDto);
    }


    @PostMapping("/assignment_csv")
    @Operation(
            summary = "Download assignment report csv",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "text/plain")
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Assignment not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<UrlResource> getAssignmentReportCsv(@RequestBody AssignmentReportDto assignmentReportDto) throws IOException {
        return service.createAssignmentCsvReport(assignmentReportDto);
    }

    @PostMapping("/group_csv")
    @Operation(
            summary = "Download group report csv",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "text/plain")
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Group not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<UrlResource> getGroupReportCsv(@RequestBody GroupReportDto groupReportDto) throws IOException {
        return service.createGroupCsvReport(groupReportDto);
    }
}
