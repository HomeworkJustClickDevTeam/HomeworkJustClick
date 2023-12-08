package pl.homeworkjustclick.evaluationpanel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.homeworkjustclick.infrastructure.exception.EntityNotFoundException;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class EvaluationPanelUtilsService {
    private final EvaluationPanelRepository repository;

    public void updateEvaluationPanel(Integer evaluationPanelId) {
        var evaluationPanel = repository.findById(evaluationPanelId)
                .orElseThrow(() -> new EntityNotFoundException("Evaluation panel with id = " + evaluationPanelId + " not found"));
        evaluationPanel.setLastUsedDate(OffsetDateTime.now());
        evaluationPanel.setCounter(evaluationPanel.getCounter() + 1);
        repository.save(evaluationPanel);
    }
}
