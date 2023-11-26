package pl.HomeworkJustClick.Backend.evaluationpanel;

import lombok.*;
import pl.HomeworkJustClick.Backend.evaluationbutton.EvaluationButtonDto;

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
