package pl.homeworkjustclick.report;

import lombok.*;
import pl.homeworkjustclick.group.GroupResponseDto;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GroupReportResponseDto implements Serializable {
    private GroupResponseDto group;
    private List<AssignmentReportResponseDto> assignments;
    private String description;
}
