package pl.HomeworkJustClick.Backend.report;

import lombok.*;
import pl.HomeworkJustClick.Backend.group.GroupResponseDto;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupReportResponseDto {
    private GroupResponseDto group;
    private List<AssignmentReportResponseDto> assignments;
}
