package pl.HomeworkJustClick.Backend.evaluationpanelassignment;

import lombok.*;
import pl.HomeworkJustClick.Backend.assignment.AssignmentResponseDto;
import pl.HomeworkJustClick.Backend.evaluationpanel.EvaluationPanelResponseDto;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationPanelAssignmentResponseDto implements Serializable {
    private Integer id;
    private EvaluationPanelResponseDto evaluationPanel;
    private AssignmentResponseDto assignment;
}
