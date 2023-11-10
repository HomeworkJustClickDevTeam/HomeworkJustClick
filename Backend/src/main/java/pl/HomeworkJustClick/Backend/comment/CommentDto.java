package pl.HomeworkJustClick.Backend.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto implements Serializable {
    private Integer id;
    @Schema(example = "Example title")
    @Size(max = 255)
    private String title;
    @Schema(example = "Example desc")
    @Size(max = 255)
    @NotEmpty
    private String description;
    @Schema(example = "1")
    @NonNull
    private Integer defaultColor;
    @NonNull
    private Integer userId;
}
