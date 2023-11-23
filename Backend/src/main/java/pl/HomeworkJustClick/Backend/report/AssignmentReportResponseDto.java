package pl.HomeworkJustClick.Backend.report;

import lombok.*;
import pl.HomeworkJustClick.Backend.assignment.AssignmentResponseDto;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentReportResponseDto {
    private AssignmentResponseDto assignment;
    private Double maxResult;
    private Double maxResultPercent;
    private Double minResult;
    private Double minResultPercent;
    private Double avgResult;
    private Double avgResultPercent;
    private Integer late;
    private List<Integer> hist;
    private List<Integer> studentsHist;
    private List<AssignmentReportStudentResponseDto> students;
}
