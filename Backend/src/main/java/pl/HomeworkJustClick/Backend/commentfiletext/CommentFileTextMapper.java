package pl.HomeworkJustClick.Backend.commentfiletext;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.HomeworkJustClick.Backend.comment.CommentMapper;
import pl.HomeworkJustClick.Backend.file.FileMapper;

@Component
@RequiredArgsConstructor
public class CommentFileTextMapper {
    private final CommentMapper commentMapper;
    private final FileMapper fileMapper;

    public CommentFileText map(CommentFileTextDto commentFileTextDto) {
        return CommentFileText.builder()
                .highlightStart(commentFileTextDto.getHighlightStart())
                .highlightEnd(commentFileTextDto.getHighlightEnd())
                .color(commentFileTextDto.getColor())
                .build();
    }

    public CommentFileTextResponseDto map(CommentFileText commentFileText) {
        return CommentFileTextResponseDto.builder()
                .id(commentFileText.getId())
                .highlightStart(commentFileText.getHighlightStart())
                .highlightEnd(commentFileText.getHighlightEnd())
                .color(commentFileText.getColor())
                .comment(commentMapper.map(commentFileText.getComment()))
                .file(fileMapper.map(commentFileText.getFile()))
                .build();
    }

    public void map(CommentFileText target, CommentFileTextDto source) {
        target.setHighlightStart(source.getHighlightStart());
        target.setHighlightEnd(source.getHighlightEnd());
        target.setColor(source.getColor());
    }
}
