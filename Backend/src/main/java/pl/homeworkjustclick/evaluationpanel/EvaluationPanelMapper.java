package pl.homeworkjustclick.evaluationpanel;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class EvaluationPanelMapper {
    public EvaluationPanel map(EvaluationPanelDto evaluationPanelDto) {
        return EvaluationPanel.builder()
                .name(evaluationPanelDto.getName())
                .width(evaluationPanelDto.getWidth())
                .counter(0)
                .lastUsedDate(OffsetDateTime.now())
                .build();
    }

    public EvaluationPanelResponseDto map(EvaluationPanel evaluationPanel) {
        return EvaluationPanelResponseDto.builder()
                .id(evaluationPanel.getId())
                .name(evaluationPanel.getName())
                .width(evaluationPanel.getWidth())
                .build();
    }

    void map(EvaluationPanelDto source, EvaluationPanel target) {
        target.setName(source.getName());
        target.setWidth(target.getWidth());
    }
}
