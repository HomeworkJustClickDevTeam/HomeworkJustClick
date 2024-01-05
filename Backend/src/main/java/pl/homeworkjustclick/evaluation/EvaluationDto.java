package pl.homeworkjustclick.evaluation;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationDto implements Serializable {
    @NotNull
    private Double result;
    @NotNull
    private Integer userId;
    @NotNull
    private Integer solutionId;
    @NotNull
    private Integer groupId;
    private Double grade;
    private String comment;

}
