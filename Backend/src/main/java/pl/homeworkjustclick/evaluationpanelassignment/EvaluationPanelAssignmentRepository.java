package pl.homeworkjustclick.evaluationpanelassignment;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EvaluationPanelAssignmentRepository extends JpaRepository<EvaluationPanelAssignment, Integer> {
    Optional<EvaluationPanelAssignment> findByEvaluationPanelIdAndAssignmentId(Integer evaluationPanelId, Integer assignmentId);

    Boolean existsByEvaluationPanelIdAndAssignmentId(Integer evaluationPanelId, Integer assignmentId);
}
