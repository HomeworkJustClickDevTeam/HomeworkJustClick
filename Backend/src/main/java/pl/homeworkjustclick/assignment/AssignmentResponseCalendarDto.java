package pl.homeworkjustclick.assignment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.homeworkjustclick.infrastructure.enums.CalendarStatus;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentResponseCalendarDto implements Serializable {
    @Schema(example = "0")
    private int id;
    @Schema(example = "Example title")
    private String title;
    @Schema(example = "true")
    private Boolean visible;
    @Schema(example = "0")
    private Integer userId;
    @Schema(example = "0")
    private Integer groupId;
    @Schema(example = "Example desc")
    private String taskDescription;
    private OffsetDateTime creationDatetime;
    private OffsetDateTime lastModifiedDatetime;
    private OffsetDateTime completionDatetime;
    @JsonIgnore
    private boolean forbidden;
    @Schema(example = "10")
    private int maxPoints;
    @Schema(example = "50")
    private int autoPenalty;
    private CalendarStatus status;
}
