package pl.HomeworkJustClick.Backend.evaluationpanelbutton;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.evaluationbutton.EvaluationButton;
import pl.HomeworkJustClick.Backend.evaluationpanel.EvaluationPanel;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationPanelButtonService {
    private final EvaluationPanelButtonRepository repository;

    public List<EvaluationPanelButton> save(EvaluationPanel evaluationPanel, List<EvaluationButton> evaluationButtons) {
        var evaluationPanelButtons = new ArrayList<EvaluationPanelButton>();
        evaluationButtons.forEach(evaluationButton -> {
            var evaluationPanelButton = EvaluationPanelButton.builder().evaluationPanel(evaluationPanel).evaluationButton(evaluationButton).build();
            evaluationPanelButtons.add(repository.save(evaluationPanelButton));
        });
        return evaluationPanelButtons;
    }

    public void deleteAllByEvaluationPanelId(Integer evaluationPanelId) {
        var evaluationPanelButtons = repository.findAllByEvaluationPanelId(evaluationPanelId);
        repository.deleteAll(evaluationPanelButtons);
    }
}
