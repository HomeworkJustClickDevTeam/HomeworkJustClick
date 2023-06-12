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
public class EvaluationResponseExtended {
    @Schema(example = "0")
    private int id;
    @Schema(example = "0.0")
    private Double result;
    private UserResponse user;
    private SolutionResponse solution;
    private GroupResponse group;
    private OffsetDateTime creationDatetime;
    private OffsetDateTime lastModifiedDatetime;
    @Schema(example = "3.5")
    private Double grade;
    @JsonIgnore
    private boolean forbidden;
}
