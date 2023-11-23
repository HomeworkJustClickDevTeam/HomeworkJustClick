package pl.HomeworkJustClick.Backend.report;

import lombok.*;
import pl.HomeworkJustClick.Backend.user.UserSimpleResponseDto;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentReportStudentResponseDto {
    private UserSimpleResponseDto student;
    private Double result;
    private Double resultPercent;
    private Boolean late;
}
