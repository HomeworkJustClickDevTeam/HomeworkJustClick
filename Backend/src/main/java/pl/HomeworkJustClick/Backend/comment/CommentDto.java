package pl.HomeworkJustClick.Backend.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.OffsetDateTime;

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
    private String description;
    @Schema(example = "1")
    private Integer defaultColor;
    private OffsetDateTime lastUsedDate;
    private Integer userId;
}
