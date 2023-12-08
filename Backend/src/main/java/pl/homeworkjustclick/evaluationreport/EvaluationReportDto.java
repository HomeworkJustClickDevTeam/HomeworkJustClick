package pl.homeworkjustclick.evaluationreport;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EvaluationReportDto {
    private Integer id;
    @NotNull
    @Size(max = 255)
    private String comment;
    @NotNull
    private Integer evaluationId;
}
