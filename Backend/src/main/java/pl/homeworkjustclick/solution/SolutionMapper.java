package pl.homeworkjustclick.solution;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.homeworkjustclick.assignment.AssignmentMapper;
import pl.homeworkjustclick.group.GroupMapper;
import pl.homeworkjustclick.user.UserMapper;

@Component
@RequiredArgsConstructor
public class SolutionMapper {
    private final UserMapper userMapper;
    private final AssignmentMapper assignmentMapper;
    private final GroupMapper groupMapper;

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

    public SolutionResponseExtendedDto mapExtended(Solution solution) {
        return SolutionResponseExtendedDto.builder()
                .id(solution.getId())
                .user(userMapper.map(solution.getUser()))
                .assignment(assignmentMapper.map(solution.getAssignment()))
                .group(groupMapper.map(solution.getGroup()))
                .creationDateTime(solution.getCreationDatetime())
                .lastModifiedDatetime(solution.getLastModifiedDatetime())
                .comment(solution.getComment())
                .build();
    }
}
