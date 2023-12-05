package pl.HomeworkJustClick.Backend.evaluationreport;

import lombok.*;
import pl.HomeworkJustClick.Backend.evaluation.EvaluationResponseDto;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationReportResponseDto {
    private Integer id;
    private String comment;
    private EvaluationResponseDto evaluation;
}
