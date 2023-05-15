package pl.HomeworkJustClick.Backend.Responses;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolutionResponse {
    private int id;
    private Integer userId;
    private Integer assignmentId;
    private Integer groupId;
    private OffsetDateTime creationDateTime;
    private OffsetDateTime lastModifiedDatetime;
    private String comment;
}
