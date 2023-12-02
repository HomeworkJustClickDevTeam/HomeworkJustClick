package pl.HomeworkJustClick.Backend.evaluation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EvaluationUtilsService {
    private final EvaluationRepository repository;

    public Boolean existsBySolutionId(Integer soultionId) {
        return repository.existsBySolutionId(soultionId);
    }
}
