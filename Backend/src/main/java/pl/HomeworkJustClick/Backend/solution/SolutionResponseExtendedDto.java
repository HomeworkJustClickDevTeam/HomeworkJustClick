package pl.HomeworkJustClick.Backend.solution;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.HomeworkJustClick.Backend.assignment.AssignmentResponseDto;
import pl.HomeworkJustClick.Backend.group.GroupResponseDto;
import pl.HomeworkJustClick.Backend.user.UserResponseDto;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolutionResponseExtendedDto {
    @Schema(example = "0")
    private int id;
    private UserResponseDto user;
    private AssignmentResponseDto assignment;
    private GroupResponseDto group;
    private OffsetDateTime creationDateTime;
    private OffsetDateTime lastModifiedDatetime;
    @Schema(example = "Example comment")
    private String comment;
    @JsonIgnore
    private boolean forbidden;
}
