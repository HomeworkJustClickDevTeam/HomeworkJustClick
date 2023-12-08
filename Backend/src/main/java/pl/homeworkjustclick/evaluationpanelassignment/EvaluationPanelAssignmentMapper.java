package pl.homeworkjustclick.evaluationpanelassignment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.homeworkjustclick.assignment.AssignmentMapper;

@Component
@RequiredArgsConstructor
public class EvaluationPanelAssignmentMapper {
    private final AssignmentMapper assignmentMapper;

    public EvaluationPanelAssignment map(EvaluationPanelAssignmentDto evaluationPanelAssignmentDto) {
        return EvaluationPanelAssignment.builder().build();
    }

    public EvaluationPanelAssignmentResponseDto map(EvaluationPanelAssignment evaluationPanelAssignment) {
        var assignmentResponseDto = assignmentMapper.map(evaluationPanelAssignment.getAssignment());
        return EvaluationPanelAssignmentResponseDto.builder()
                .id(evaluationPanelAssignment.getId())
                .assignment(assignmentResponseDto)
                .build();
    }
}
