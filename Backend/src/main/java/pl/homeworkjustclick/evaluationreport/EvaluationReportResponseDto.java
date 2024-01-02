package pl.homeworkjustclick.evaluationreport;

import lombok.*;
import pl.homeworkjustclick.evaluation.EvaluationResponseDto;
import pl.homeworkjustclick.solution.SolutionResponseExtendedDto;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationReportResponseDto implements Serializable {
    private Integer id;
    private String comment;
    private EvaluationResponseDto evaluation;
    private SolutionResponseExtendedDto solution;
}
