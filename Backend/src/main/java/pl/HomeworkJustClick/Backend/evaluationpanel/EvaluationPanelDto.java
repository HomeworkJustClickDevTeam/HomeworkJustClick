package pl.HomeworkJustClick.Backend.evaluationpanel;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import pl.HomeworkJustClick.Backend.evaluationbutton.EvaluationButtonDto;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationPanelDto implements Serializable {
    @NotEmpty
    @Size(max = 255)
    private String name;
    @NotNull
    private Integer width;
    @NotNull
    private Integer userId;
    @NotNull
    private List<EvaluationButtonDto> buttons;
}
