package pl.HomeworkJustClick.Backend.report;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
@SecurityRequirement(name = "Bearer Authentication")
public class ReportController {
    private final ReportService service;

    @GetMapping("/assignment")
    public AssignmentReportResponseDto getAssignmentReport(@RequestBody AssignmentReportDto assignmentReportDto) {
        return service.createAssignmentReport(assignmentReportDto);
    }

    @GetMapping("/group")
    public GroupReportResponseDto getGroupReport(@RequestBody GroupReportDto groupReportDto) {
        return service.createGroupReport(groupReportDto);
    }

    @GetMapping("/group_csv")
    public ResponseEntity<UrlResource> getGroupReportCsv(@RequestBody GroupReportDto groupReportDto) throws IOException {
        return service.createGroupCsvReport(groupReportDto);
    }

    @GetMapping("/assignment_csv")
    public ResponseEntity<UrlResource> getAssignmentReportCsv(@RequestBody AssignmentReportDto assignmentReportDto) throws IOException {
        return service.createAssignmentCsvReport(assignmentReportDto);
    }
}
