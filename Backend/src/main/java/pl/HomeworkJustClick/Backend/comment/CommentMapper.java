package pl.HomeworkJustClick.Backend.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.HomeworkJustClick.Backend.user.UserMapper;
import pl.HomeworkJustClick.Backend.user.UserService;

import java.time.OffsetDateTime;

@Component
@RequiredArgsConstructor
public class CommentMapper {
    private final UserService userService;
    private final UserMapper userMapper;

    public Comment map(CommentDto commentDto) {
        return Comment.builder()
                .title(commentDto.getTitle())
                .description(commentDto.getDescription())
                .defaultColor(commentDto.getDefaultColor())
                .lastUsedDate(OffsetDateTime.now())
                .user(userService.findById(commentDto.getUserId()))
                .build();
    }

    public CommentResponseDto map(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .title(comment.getTitle())
                .description(comment.getDescription())
                .defaultColor(comment.getDefaultColor())
                .lastUsedDate(comment.getLastUsedDate())
                .user(userMapper.map(comment.getUser()))
                .build();
    }

    public void map(Comment target, CommentDto source) {
        target.setTitle(source.getTitle());
        target.setDescription(source.getDescription());
        target.setDefaultColor(source.getDefaultColor());
        target.setUser(userService.findById(source.getUserId()));
    }
}
