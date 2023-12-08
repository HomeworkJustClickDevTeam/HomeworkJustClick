package pl.HomeworkJustClick.Backend.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.HomeworkJustClick.Backend.assignment.AssignmentMapper;
import pl.HomeworkJustClick.Backend.user.UserMapper;

@Component
@RequiredArgsConstructor
public class NotificationMapper {
    private final UserMapper userMapper;
    private final AssignmentMapper assignmentMapper;

    NotificationResponseDto map(Notification notification) {
        return NotificationResponseDto.builder()
                .id(notification.getId())
                .description(notification.getDescription())
                .read(notification.getRead())
                .date(notification.getDate())
                .assignment(assignmentMapper.map(notification.getAssignment()))
                .user(userMapper.map2SimpleResponseDto(notification.getUser()))
                .build();
    }
}
