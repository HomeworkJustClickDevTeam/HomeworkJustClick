package pl.homeworkjustclick.evaluationreport;

import lombok.*;
import pl.homeworkjustclick.assignment.AssignmentResponseDto;
import pl.homeworkjustclick.evaluation.EvaluationResponseDto;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationReportResponseDto {
    private Integer id;
    private String comment;
    private EvaluationResponseDto evaluation;
    private AssignmentResponseDto assignment;
}
