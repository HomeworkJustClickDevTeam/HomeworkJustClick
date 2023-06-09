package pl.HomeworkJustClick.Backend.Responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentResponseExtended {
    @Schema(example = "0")
    private int id;
    @Schema(example = "Example title")
    private String title;
    @Schema(example = "true")
    private Boolean visible;
    @Schema(example = "0")
    private UserResponse user;
    @Schema(example = "0")
    private GroupResponse group;
    @Schema(example = "Example desc")
    private String taskDescription;
    private OffsetDateTime creationDatetime;
    private OffsetDateTime lastModifiedDatetime;
    private OffsetDateTime completionDatetime;
    @JsonIgnore
    private boolean forbidden;
    @Schema(example = "10")
    private int max_points;
}
