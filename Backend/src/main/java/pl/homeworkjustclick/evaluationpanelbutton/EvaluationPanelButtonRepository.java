package pl.homeworkjustclick.evaluationpanelbutton;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluationPanelButtonRepository extends JpaRepository<EvaluationPanelButton, Integer> {
    List<EvaluationPanelButton> findAllByEvaluationPanelId(Integer evaluationPanelId);
}
