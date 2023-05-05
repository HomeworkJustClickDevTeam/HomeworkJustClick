package pl.HomeworkJustClick.Backend.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.HomeworkJustClick.Backend.Entities.Evaluation;

public interface EvaluationRepository extends JpaRepository<Evaluation, Integer> {
}