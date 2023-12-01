package pl.HomeworkJustClick.Backend.evaluation;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationDto {
    @NotNull
    private Double result;
    @NotNull
    private Integer userId;
    @NotNull
    private Integer solutionId;
    @NotNull
    private Integer groupId;
    private Double grade;
    private Boolean reported;
}
