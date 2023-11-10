package pl.HomeworkJustClick.Backend.evaluationpanelassignment;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationPanelAssignmentDto implements Serializable {
    @NotNull
    private Integer evaluationPanelId;
    @NotNull
    private Integer assignmentId;
}
