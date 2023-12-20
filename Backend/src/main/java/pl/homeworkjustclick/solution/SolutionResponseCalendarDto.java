package pl.homeworkjustclick.solution;

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
public class SolutionResponseCalendarDto implements Serializable {
    @Schema(example = "0")
    private int id;
    @Schema(example = "0")
    private Integer userId;
    @Schema(example = "0")
    private Integer assignmentId;
    @Schema(example = "0")
    private Integer groupId;
    private OffsetDateTime creationDateTime;
    private OffsetDateTime lastModifiedDatetime;
    @Schema(example = "Example comment")
    private String comment;
    @JsonIgnore
    private boolean forbidden;
    private CalendarStatus status;
}
