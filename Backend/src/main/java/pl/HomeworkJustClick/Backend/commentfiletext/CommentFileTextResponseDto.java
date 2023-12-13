package pl.HomeworkJustClick.Backend.commentfiletext;

import lombok.*;
import pl.HomeworkJustClick.Backend.comment.CommentSimpleResponseDto;
import pl.HomeworkJustClick.Backend.file.FileResponseDto;

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