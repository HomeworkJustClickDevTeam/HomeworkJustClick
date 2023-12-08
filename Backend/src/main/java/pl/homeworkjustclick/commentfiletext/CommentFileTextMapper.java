package pl.homeworkjustclick.commentfiletext;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.homeworkjustclick.comment.CommentMapper;
import pl.homeworkjustclick.file.FileMapper;

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
                .comment(commentMapper.mapToSimpleResponseDto(commentFileText.getComment()))
                .file(fileMapper.map(commentFileText.getFile()))
                .build();
    }

    public void map(CommentFileText target, CommentFileTextDto source) {
        target.setHighlightStart(source.getHighlightStart());
        target.setHighlightEnd(source.getHighlightEnd());
        target.setColor(source.getColor());
    }
}
