package pl.homeworkjustclick.report;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupReportDto {
    @NotNull
    private Integer groupId;
    private Boolean maxResult = true;
    private Boolean minResult = true;
    private Boolean avgResult = true;
    private Boolean late = true;
    private List<Integer> hist;
}
