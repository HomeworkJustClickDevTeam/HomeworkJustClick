package pl.homeworkjustclick.assignment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.homeworkjustclick.evaluation.EvaluationResponseDto;
import pl.homeworkjustclick.group.GroupMapper;
import pl.homeworkjustclick.user.UserMapper;

@Component
@RequiredArgsConstructor
public class AssignmentMapper {
    private final UserMapper userMapper;
    private final GroupMapper groupMapper;

    public AssignmentResponseDto map(Assignment assignment) {
        return AssignmentResponseDto.builder()
                .id(assignment.getId())
                .userId((assignment.getUser() == null) ? null : assignment.getUser().getId())
                .groupId((assignment.getGroup() == null) ? null : assignment.getGroup().getId())
                .taskDescription(assignment.getTaskDescription())
                .creationDatetime(assignment.getCreationDatetime())
                .lastModifiedDatetime(assignment.getLastModifiedDatetime())
                .completionDatetime(assignment.getCompletionDatetime())
                .title(assignment.getTitle())
                .visible(assignment.getVisible())
                .maxPoints(assignment.getMaxPoints())
                .autoPenalty(assignment.getAutoPenalty())
                .build();
    }

    public AssignmentWithEvaluationResponseDto map(Assignment assignment, EvaluationResponseDto evaluation) {
        return AssignmentWithEvaluationResponseDto.builder()
                .id(assignment.getId())
                .user(userMapper.map(assignment.getUser()))
                .group(groupMapper.map(assignment.getGroup()))
                .taskDescription(assignment.getTaskDescription())
                .creationDatetime(assignment.getCreationDatetime())
                .lastModifiedDatetime(assignment.getLastModifiedDatetime())
                .completionDatetime(assignment.getCompletionDatetime())
                .title(assignment.getTitle())
                .visible(assignment.getVisible())
                .maxPoints(assignment.getMaxPoints())
                .autoPenalty(assignment.getAutoPenalty())
                .evaluation(evaluation)
                .build();
    }
}
