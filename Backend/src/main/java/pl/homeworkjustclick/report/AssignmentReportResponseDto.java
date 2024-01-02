package pl.homeworkjustclick.report;

import lombok.*;
import pl.homeworkjustclick.assignment.AssignmentResponseDto;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentReportResponseDto implements Serializable {
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
    private String description;
}
