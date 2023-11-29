package pl.HomeworkJustClick.Backend.report;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupReportDto {
    private Integer groupId;
    private Boolean maxResult;
    private Boolean minResult;
    private Boolean avgResult;
    private Boolean late;
    private List<Integer> hist;
}
