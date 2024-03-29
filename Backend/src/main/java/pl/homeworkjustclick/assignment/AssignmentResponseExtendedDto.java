package pl.homeworkjustclick.assignment;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import pl.homeworkjustclick.group.GroupResponseDto;
import pl.homeworkjustclick.user.UserResponseDto;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentResponseExtendedDto implements Serializable {
    @Schema(example = "0")
    private int id;
    @Schema(example = "Example title")
    private String title;
    @Schema(example = "true")
    private Boolean visible;
    private UserResponseDto user;
    private GroupResponseDto group;
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
    private Boolean advancedEvaluation;
}
