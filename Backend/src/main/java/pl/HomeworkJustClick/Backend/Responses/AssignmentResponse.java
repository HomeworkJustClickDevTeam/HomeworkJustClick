package pl.HomeworkJustClick.Backend.Responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentResponse {
    private int id;
    private String title;
    private Boolean visible;
    private Integer userId;
    private Integer groupId;
    private String taskDescription;
    private OffsetDateTime creationDatetime;
    private OffsetDateTime lastModifiedDatetime;
    private OffsetDateTime completionDatetime;
    private boolean forbidden;
    private int max_points;
}
