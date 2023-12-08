package pl.HomeworkJustClick.Backend.evaluation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.infrastructure.exception.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class EvaluationUtilsService {
    private final EvaluationRepository repository;

    public Boolean existsBySolutionId(Integer solutionId) {
        return repository.existsBySolutionId(solutionId);
    }

    public Evaluation findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evaluation with id = " + id + " not found"));
    }
}
