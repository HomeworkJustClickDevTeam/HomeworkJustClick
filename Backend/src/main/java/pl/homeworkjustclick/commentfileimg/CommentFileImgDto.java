package pl.homeworkjustclick.commentfileimg;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentFileImgDto implements Serializable {
    private Integer id;
    @NonNull
    private Integer leftTopX;
    @NonNull
    private Integer leftTopY;
    @NonNull
    private Integer width;
    @NonNull
    private Integer height;
    @NonNull
    private Integer imgWidth;
    @NonNull
    private Integer imgHeight;
    private String color;
    @NonNull
    private Integer commentId;
    @NonNull
    private Integer fileId;
}
