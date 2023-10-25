package pl.HomeworkJustClick.Backend.commentfileimg;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.HomeworkJustClick.Backend.comment.CommentMapper;
import pl.HomeworkJustClick.Backend.file.FileMapper;

@Component
@RequiredArgsConstructor
public class CommentFileImgMapper {
    private final CommentMapper commentMapper;
    private final FileMapper fileMapper;

    public CommentFileImgResponseDto map(CommentFileImg commentFileImg) {
        return CommentFileImgResponseDto.builder()
                .id(commentFileImg.getId())
                .leftTopX(commentFileImg.getLeftTopX())
                .leftTopY(commentFileImg.getLeftTopY())
                .width(commentFileImg.getWidth())
                .height(commentFileImg.getHeight())
                .lineWidth(commentFileImg.getLineWidth())
                .color(commentFileImg.getColor())
                .comment(commentMapper.map(commentFileImg.getComment()))
                .file(fileMapper.map(commentFileImg.getFile()))
                .build();
    }

    public CommentFileImg map(CommentFileImgDto commentFileImgDto) {
        return CommentFileImg.builder()
                .leftTopX(commentFileImgDto.getLeftTopX())
                .leftTopY(commentFileImgDto.getLeftTopY())
                .width(commentFileImgDto.getWidth())
                .height(commentFileImgDto.getHeight())
                .lineWidth(commentFileImgDto.getLineWidth())
                .color(commentFileImgDto.getColor())
                .build();
    }

    public void map(CommentFileImgDto source, CommentFileImg target) {
        target.setLeftTopX(source.getLeftTopX());
        target.setLeftTopY(source.getLeftTopY());
        target.setWidth(source.getWidth());
        target.setHeight(source.getHeight());
        target.setLineWidth(source.getLineWidth());
        target.setColor(source.getColor());
    }
}
