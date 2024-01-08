package pl.homeworkjustclick.evaluationreport;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationReportDto implements Serializable {
    private Integer id;
    @NotNull
    @Size(max = 1500)
    private String comment;
    @NotNull
    private Integer evaluationId;
}
