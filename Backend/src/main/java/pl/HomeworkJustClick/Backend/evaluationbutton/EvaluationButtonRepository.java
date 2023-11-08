package pl.HomeworkJustClick.Backend.evaluationbutton;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EvaluationButtonRepository extends JpaRepository<EvaluationButton, Integer> {
    Optional<EvaluationButton> findByPoints(Double points);

    List<EvaluationButton> findAllByIdIn(List<Integer> ids);
}
