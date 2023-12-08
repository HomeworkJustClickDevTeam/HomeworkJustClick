package pl.homeworkjustclick.commentfiletext;

import lombok.*;
import pl.homeworkjustclick.comment.CommentSimpleResponseDto;
import pl.homeworkjustclick.file.FileResponseDto;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentFileTextResponseDto implements Serializable {
    private Integer id;
    private Integer highlightStart;
    private Integer highlightEnd;
    private String color;
    private CommentSimpleResponseDto comment;
    private FileResponseDto file;
}
