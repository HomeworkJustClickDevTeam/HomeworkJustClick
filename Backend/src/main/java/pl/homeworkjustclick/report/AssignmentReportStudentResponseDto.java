package pl.homeworkjustclick.report;

import lombok.*;
import pl.homeworkjustclick.user.UserSimpleResponseDto;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssignmentReportStudentResponseDto implements Serializable {
    private UserSimpleResponseDto student;
    private Double result;
    private Double resultPercent;
    private Boolean late;
    private String description;
}
