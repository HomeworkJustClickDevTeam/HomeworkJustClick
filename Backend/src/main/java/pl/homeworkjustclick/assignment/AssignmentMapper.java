package pl.homeworkjustclick.assignment;

import org.springframework.stereotype.Component;

@Component
public class AssignmentMapper {
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
}
