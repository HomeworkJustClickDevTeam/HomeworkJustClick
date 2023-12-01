package pl.HomeworkJustClick.Backend.solution;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SolutionMapper {
    public SolutionResponseDto map(Solution solution) {
        return SolutionResponseDto.builder()
                .id(solution.getId())
                .userId(solution.getUser().getId())
                .assignmentId(solution.getAssignment().getId())
                .groupId(solution.getGroup().getId())
                .creationDateTime(solution.getCreationDatetime())
                .lastModifiedDatetime(solution.getLastModifiedDatetime())
                .comment(solution.getComment())
                .build();
    }
}
