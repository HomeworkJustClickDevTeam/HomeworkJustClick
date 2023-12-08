package pl.homeworkjustclick.report;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentReportDto {
    private Integer assignmentId;
    private Boolean maxResult;
    private Boolean minResult;
    private Boolean avgResult;
    private Boolean late;
    private List<Integer> hist;
}
