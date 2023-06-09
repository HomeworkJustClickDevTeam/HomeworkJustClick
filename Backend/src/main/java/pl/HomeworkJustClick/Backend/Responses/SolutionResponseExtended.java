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
public class SolutionResponseExtended {
    @Schema(example = "0")
    private int id;
    private UserResponse user;
    private AssignmentResponse assignment;
    private GroupResponse group;
    private OffsetDateTime creationDateTime;
    private OffsetDateTime lastModifiedDatetime;
    @Schema(example = "Example comment")
    private String comment;
    @JsonIgnore
    private boolean forbidden;
}
