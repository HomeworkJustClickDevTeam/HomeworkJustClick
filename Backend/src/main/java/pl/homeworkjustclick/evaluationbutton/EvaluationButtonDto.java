package pl.homeworkjustclick.evaluationbutton;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationButtonDto implements Serializable {
    @NotNull
    private Double points;
}
