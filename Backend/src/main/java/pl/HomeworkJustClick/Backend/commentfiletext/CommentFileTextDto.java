package pl.HomeworkJustClick.Backend.commentfiletext;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentFileTextDto implements Serializable {
    private Integer id;
    @NonNull
    private Integer highlightStart;
    @NonNull
    private Integer highlightEnd;
    @NonNull
    private Integer color;
    @NonNull
    private Integer commentId;
    @NonNull
    private Integer fileId;
}
