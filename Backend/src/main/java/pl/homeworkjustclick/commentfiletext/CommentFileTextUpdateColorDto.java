package pl.homeworkjustclick.commentfiletext;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentFileTextUpdateColorDto {
    @Size(max = 10)
    @NotEmpty
    private String color;
}
