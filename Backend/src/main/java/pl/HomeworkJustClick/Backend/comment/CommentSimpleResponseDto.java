package pl.HomeworkJustClick.Backend.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.OffsetDateTime;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentSimpleResponseDto {
    @Schema(example = "0")
    private Integer id;
    @Schema(example = "Example title")
    private String title;
    @Schema(example = "Example desc")
    private String description;
    @Schema(example = "1")
    private String color;
    private OffsetDateTime lastUsedDate;
    @Schema(example = "1")
    private Integer counter;
    @Schema(example = "1")
    private Integer assignmentId;
    @Schema(example = "1")
    private Integer userId;
}
