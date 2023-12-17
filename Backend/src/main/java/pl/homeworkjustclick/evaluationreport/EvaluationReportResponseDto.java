package pl.homeworkjustclick.evaluationreport;

import lombok.*;
import pl.homeworkjustclick.evaluation.EvaluationResponseDto;
import pl.homeworkjustclick.solution.SolutionResponseExtendedDto;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationReportResponseDto {
    private Integer id;
    private String comment;
    private EvaluationResponseDto evaluation;
    private SolutionResponseExtendedDto solution;
}
