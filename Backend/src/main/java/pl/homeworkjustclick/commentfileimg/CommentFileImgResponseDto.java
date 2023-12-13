package pl.homeworkjustclick.commentfileimg;

import lombok.*;
import pl.homeworkjustclick.comment.CommentSimpleResponseDto;
import pl.homeworkjustclick.file.FileResponseDto;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentFileImgResponseDto implements Serializable {
    private Integer id;
    private Integer leftTopX;
    private Integer leftTopY;
    private Integer width;
    private Integer height;
    private Integer imgWidth;
    private Integer imgHeight;
    private String color;
    private CommentSimpleResponseDto comment;
    private FileResponseDto file;
}
