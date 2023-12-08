package pl.homeworkjustclick.evaluationpanelassignment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.homeworkjustclick.assignment.AssignmentService;
import pl.homeworkjustclick.evaluationpanel.EvaluationPanelResponseDto;
import pl.homeworkjustclick.evaluationpanel.EvaluationPanelService;
import pl.homeworkjustclick.evaluationpanel.EvaluationPanelUtilsService;
import pl.homeworkjustclick.infrastructure.exception.EntityNotFoundException;
import pl.homeworkjustclick.infrastructure.exception.InvalidArgumentException;

@Service
@RequiredArgsConstructor
public class EvaluationPanelAssignmentService {
    private final EvaluationPanelAssignmentRepository repository;
    private final EvaluationPanelAssignmentMapper mapper;
    private final AssignmentService assignmentService;
    private final EvaluationPanelService evaluationPanelService;
    private final EvaluationPanelUtilsService evaluationPanelUtilsService;

    public EvaluationPanelAssignment findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("EvaluationPanelAssignment with id = " + id + " not found"));
    }

    public EvaluationPanelAssignmentResponseDto getEvaluationPanelAssignmentByUserIdAndAssignmentId(Integer userId, Integer assignmentId) {
        var evaluationPanels = evaluationPanelService.getEvaluationPanelsListByUserId(userId);
        for (EvaluationPanelResponseDto evaluationPanel : evaluationPanels) {
            var evaluationPanelAssignmentOptional = repository.findByEvaluationPanelIdAndAssignmentId(evaluationPanel.getId(), assignmentId);
            if (evaluationPanelAssignmentOptional.isPresent()) {
                var evaluationPanelAssignmentResponseDto = mapper.map(evaluationPanelAssignmentOptional.get());
                evaluationPanelAssignmentResponseDto.setEvaluationPanel(evaluationPanel);
                return evaluationPanelAssignmentResponseDto;
            }
        }
        throw new EntityNotFoundException("EvaluationPanelAssignment with userId = " + userId + " and assignmentId = " + assignmentId + " not found");
    }

    public EvaluationPanelAssignmentResponseDto createEvaluationPanelAssignment(EvaluationPanelAssignmentDto evaluationPanelAssignmentDto) {
        validateDto(evaluationPanelAssignmentDto);
        var evaluationPanelAssignment = mapper.map(evaluationPanelAssignmentDto);
        setRelationFields(evaluationPanelAssignment, evaluationPanelAssignmentDto);
        evaluationPanelUtilsService.updateEvaluationPanel(evaluationPanelAssignmentDto.getEvaluationPanelId());
        var savedEvaluationPanelAssignment = repository.save(evaluationPanelAssignment);
        var evaluationPanelAssignmentResponseDto = mapper.map(savedEvaluationPanelAssignment);
        evaluationPanelAssignmentResponseDto.setEvaluationPanel(evaluationPanelService.getEvaluationPanelResponseDtoById(savedEvaluationPanelAssignment.getEvaluationPanel().getId()));
        return evaluationPanelAssignmentResponseDto;
    }

    public void deleteEvaluationPanelAssignment(Integer id) {
        var evaluationPanelAssignment = findById(id);
        repository.delete(evaluationPanelAssignment);
    }

    public EvaluationPanelAssignmentResponseDto updateEvaluationPanelAssignment(Integer id, EvaluationPanelAssignmentDto evaluationPanelAssignmentDto) {
        var evaluationPanelAssignment = findById(id);
        validateDto(evaluationPanelAssignmentDto);
        setRelationFields(evaluationPanelAssignment, evaluationPanelAssignmentDto);
        var savedEvaluationPanelAssignment = repository.save(evaluationPanelAssignment);
        var evaluationPanelAssignmentResponseDto = mapper.map(savedEvaluationPanelAssignment);
        evaluationPanelAssignmentResponseDto.setEvaluationPanel(evaluationPanelService.getEvaluationPanelResponseDtoById(savedEvaluationPanelAssignment.getEvaluationPanel().getId()));
        return evaluationPanelAssignmentResponseDto;
    }

    private void validateDto(EvaluationPanelAssignmentDto evaluationPanelAssignmentDto) {
        if (repository.existsByEvaluationPanelIdAndAssignmentId(evaluationPanelAssignmentDto.getEvaluationPanelId(), evaluationPanelAssignmentDto.getAssignmentId())) {
            throw new InvalidArgumentException("EvaluationPanelAssignment with evaluationPanelId = " + evaluationPanelAssignmentDto.getEvaluationPanelId() +
                    " and assignmentId = " + evaluationPanelAssignmentDto.getAssignmentId() + " already exists");
        }
    }

    private void setRelationFields(EvaluationPanelAssignment evaluationPanelAssignment, EvaluationPanelAssignmentDto evaluationPanelAssignmentDto) {
        evaluationPanelAssignment.setAssignment(assignmentService.findById(evaluationPanelAssignmentDto.getAssignmentId()));
        evaluationPanelAssignment.setEvaluationPanel(evaluationPanelService.findById(evaluationPanelAssignmentDto.getEvaluationPanelId()));
    }
}
