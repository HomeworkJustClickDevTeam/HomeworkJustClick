package pl.HomeworkJustClick.Backend.evaluationbutton;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.infrastructure.exception.InvalidArgumentException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationButtonService {
    private final EvaluationButtonRepository repository;
    private final EvaluationButtonMapper mapper;

    public EvaluationButton createOrGetEvaluationButton(EvaluationButtonDto evaluationButtonDto) {
        validateEvaluationButtonDto(evaluationButtonDto);
        var evaluationButtonOptional = repository.findByPoints(evaluationButtonDto.getPoints());
        return evaluationButtonOptional.orElseGet(() -> repository.save(mapper.map(evaluationButtonDto)));
    }

    public List<EvaluationButton> findById(List<Integer> ids) {
        return repository.findAllByIdIn(ids);
    }

    private void validateEvaluationButtonDto(EvaluationButtonDto evaluationButtonDto) {
        if (evaluationButtonDto.getPoints() < 0) {
            throw new InvalidArgumentException("Points must grater or equal 0");
        }
    }
}
