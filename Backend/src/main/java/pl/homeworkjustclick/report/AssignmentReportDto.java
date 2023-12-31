package pl.homeworkjustclick.report;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentReportDto implements Serializable {
    @NotNull
    private Integer assignmentId;
    private Boolean maxResult = true;
    private Boolean minResult = true;
    private Boolean avgResult = true;
    private Boolean late = true;
    private List<Integer> hist;
}
