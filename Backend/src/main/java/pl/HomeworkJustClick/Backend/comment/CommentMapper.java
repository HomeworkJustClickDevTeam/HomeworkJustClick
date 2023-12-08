package pl.HomeworkJustClick.Backend.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.HomeworkJustClick.Backend.assignment.AssignmentMapper;
import pl.HomeworkJustClick.Backend.user.UserMapper;

import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final AssignmentMapper assignmentMapper;
    private final UserMapper userMapper;

    public Comment map(CommentDto commentDto) {
        return Comment.builder()
                .title(commentDto.getTitle())
                .description(commentDto.getDescription())
                .color(commentDto.getColor())
                .lastUsedDate(OffsetDateTime.now())
                .counter(0)
                .visible(true)
                .build();
    }

    public CommentResponseDto map(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .title(comment.getTitle())
                .description(comment.getDescription())
                .color(comment.getColor())
                .lastUsedDate(comment.getLastUsedDate())
                .counter(comment.getCounter())
                .assignment(assignmentMapper.map(comment.getAssignment()))
                .user(userMapper.map2SimpleResponseDto(comment.getUser()))
                .build();
    }

    public CommentSimpleResponseDto mapToSimpleResponseDto(Comment comment) {
        return CommentSimpleResponseDto.builder()
                .id(comment.getId())
                .title(comment.getTitle())
                .description(comment.getDescription())
                .color(comment.getColor())
                .lastUsedDate(comment.getLastUsedDate())
                .counter(comment.getCounter())
                .assignmentId(comment.getAssignment().getId())
                .userId(comment.getUser().getId())
                .build();
    }

    public void map(Comment target, CommentDto source) {
        target.setTitle(source.getTitle());
        target.setDescription(source.getDescription());
        target.setColor(source.getColor());
    }
}
