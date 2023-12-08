package pl.homeworkjustclick.evaluationpanel;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EvaluationPanelRepository extends JpaRepository<EvaluationPanel, Integer> {
    List<EvaluationPanel> findAllByUserId(Integer userId, Pageable pageable);

    List<EvaluationPanel> findAllByUserId(Integer userId);
}
