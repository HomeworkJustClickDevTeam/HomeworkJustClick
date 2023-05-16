package pl.HomeworkJustClick.Backend.Responses;

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
    private int id;
    private Double result;
    private Integer userId;
    private Integer solutionId;
    private Integer groupId;
    private OffsetDateTime creationDatetime;
    private OffsetDateTime lastModifiedDatetime;
    private Double grade;
}
