package pl.HomeworkJustClick.Backend.notification;

import lombok.Builder;
import lombok.Getter;
import pl.HomeworkJustClick.Backend.assignment.AssignmentResponseDto;
import pl.HomeworkJustClick.Backend.user.UserSimpleResponseDto;

import java.time.OffsetDateTime;

@Getter
@Builder
public class NotificationResponseDto {
    private Integer id;
    private String description;
    private Boolean read;
    private OffsetDateTime date;
    private AssignmentResponseDto assignment;
    private UserSimpleResponseDto user;
}
