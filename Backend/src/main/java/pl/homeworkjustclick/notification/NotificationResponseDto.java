package pl.homeworkjustclick.notification;

import lombok.Builder;
import lombok.Getter;
import pl.homeworkjustclick.assignment.AssignmentResponseDto;
import pl.homeworkjustclick.user.UserSimpleResponseDto;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Getter
@Builder
public class NotificationResponseDto implements Serializable {
    private Integer id;
    private String description;
    private Boolean read;
    private OffsetDateTime date;
    private AssignmentResponseDto assignment;
    private UserSimpleResponseDto user;
}
