package pl.HomeworkJustClick.Backend.Responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.HomeworkJustClick.Backend.Entities.User;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationResponse {
    @Schema(example = "0")
    private int id;
    @Schema(example = "0.0")
    private Double result;
    @Schema(example = "0")
    private Integer userId;
    @Schema(example = "0")
    private Integer solutionId;
    @Schema(example = "0")
    private Integer groupId;
    private OffsetDateTime creationDatetime;
    private OffsetDateTime lastModifiedDatetime;
    @Schema(example = "3.5")
    private Double grade;
    @JsonIgnore
    private boolean forbidden;
}
