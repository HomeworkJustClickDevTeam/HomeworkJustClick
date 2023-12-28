package pl.homeworkjustclick.assignment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import pl.homeworkjustclick.evaluation.EvaluationResponseDto;

@SuperBuilder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentWithEvaluationResponseDto extends AssignmentResponseExtendedDto {
    private EvaluationResponseDto evaluation;
}
