package pl.HomeworkJustClick.Backend.commentfileimg;

import lombok.*;
import pl.HomeworkJustClick.Backend.comment.CommentResponseDto;
import pl.HomeworkJustClick.Backend.file.FileResponseDto;

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
    private Integer lineWidth;
    private Integer imgWidth;
    private Integer imgHeight;
    private Integer color;
    private CommentResponseDto comment;
    private FileResponseDto file;
}
