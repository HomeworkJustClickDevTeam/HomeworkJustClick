package pl.HomeworkJustClick.Backend.Responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.HomeworkJustClick.Backend.Entities.Group;
import pl.HomeworkJustClick.Backend.Entities.User;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentResponse {
    private int id;
    private String title;
    private Boolean visible;
    private Integer user_id;
    private Integer group_id;
    private String taskDescription;
    private Double result;
    private OffsetDateTime creationDatetime;
    private OffsetDateTime lastModifiedDatetime;
    private OffsetDateTime completionDatetime;
}
