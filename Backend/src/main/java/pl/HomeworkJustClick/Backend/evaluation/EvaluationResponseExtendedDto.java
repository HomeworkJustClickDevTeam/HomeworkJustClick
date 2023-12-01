package pl.HomeworkJustClick.Backend.evaluation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.HomeworkJustClick.Backend.group.GroupResponseDto;
import pl.HomeworkJustClick.Backend.solution.SolutionResponseDto;
import pl.HomeworkJustClick.Backend.user.UserResponseDto;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EvaluationResponseExtendedDto {
    @Schema(example = "0")
    private int id;
    @Schema(example = "0.0")
    private Double result;
    private UserResponseDto user;
    private SolutionResponseDto solution;
    private GroupResponseDto group;
    private OffsetDateTime creationDatetime;
    private OffsetDateTime lastModifiedDatetime;
    @Schema(example = "3.5")
    private Double grade;
    private boolean reported;
    @JsonIgnore
    private boolean forbidden;
}
