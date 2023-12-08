package pl.homeworkjustclick.commentfileimg;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentFileImgUpdateColorDto {
    @Size(max = 10)
    @NotEmpty
    private String color;
}
