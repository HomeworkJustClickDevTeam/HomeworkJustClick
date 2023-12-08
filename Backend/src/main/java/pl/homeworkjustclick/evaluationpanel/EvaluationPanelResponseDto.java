package pl.homeworkjustclick.evaluationpanel;

import lombok.*;
import pl.homeworkjustclick.evaluationbutton.EvaluationButtonDto;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationPanelResponseDto implements Serializable {
    private Integer id;
    private String name;
    private Integer width;
    private List<EvaluationButtonDto> buttons;
}
