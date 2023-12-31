package pl.homeworkjustclick.commentfileimg;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentFileImgUpdateColorDto implements Serializable {
    @Size(max = 10)
    @NotEmpty
    private String color;
}
