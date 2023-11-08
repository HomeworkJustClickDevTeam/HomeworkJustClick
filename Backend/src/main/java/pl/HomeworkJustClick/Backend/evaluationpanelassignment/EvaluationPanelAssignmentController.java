package pl.HomeworkJustClick.Backend.evaluationpanelassignment;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/evaluation_panel_assignment")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Evaluation panel assignment", description = "Evaluation panel assignment related calls.")
public class EvaluationPanelAssignmentController {
    private final EvaluationPanelAssignmentService service;

    @GetMapping("{userId}/{assignmentId}")
    public EvaluationPanelAssignmentResponseDto getEvaluationPanelAssignmentByUserIdAndAssignmentId(@PathVariable Integer userId, @PathVariable Integer assignmentId) {
        return service.getEvaluationPanelAssignmentByUserIdAndAssignmentId(userId, assignmentId);
    }

    @PostMapping
    public EvaluationPanelAssignmentResponseDto createEvaluationPanelAssignment(@RequestBody @Valid EvaluationPanelAssignmentDto evaluationPanelAssignmentDto) {
        return service.createEvaluationPanelAssignment(evaluationPanelAssignmentDto);
    }

    @DeleteMapping("{id}")
    public void deleteEvaluationPanelAssignment(@PathVariable Integer id) {
        service.deleteEvaluationPanelAssignment(id);
    }

    @PutMapping("{id}")
    public EvaluationPanelAssignmentResponseDto updateEvaluationPanelAssignment(@PathVariable Integer id, @RequestBody @Valid EvaluationPanelAssignmentDto evaluationPanelAssignmentDto) {
        return service.updateEvaluationPanelAssignment(id, evaluationPanelAssignmentDto);
    }
}
