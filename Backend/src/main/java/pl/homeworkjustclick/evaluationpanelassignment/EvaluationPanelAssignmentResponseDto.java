package pl.homeworkjustclick.evaluationpanelassignment;

import lombok.*;
import pl.homeworkjustclick.assignment.AssignmentResponseDto;
import pl.homeworkjustclick.evaluationpanel.EvaluationPanelResponseDto;

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
