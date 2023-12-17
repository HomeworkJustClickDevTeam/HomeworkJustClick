package pl.homeworkjustclick.commentfiletext;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentFileTextUpdateColorDto implements Serializable {
    @Size(max = 10)
    @NotEmpty
    private String color;
}
