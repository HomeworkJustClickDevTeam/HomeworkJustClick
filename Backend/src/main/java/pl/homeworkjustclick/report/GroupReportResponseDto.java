package pl.homeworkjustclick.report;

import lombok.*;
import pl.homeworkjustclick.group.GroupResponseDto;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupReportResponseDto {
    private GroupResponseDto group;
    private List<AssignmentReportResponseDto> assignments;
    private String description;
}
