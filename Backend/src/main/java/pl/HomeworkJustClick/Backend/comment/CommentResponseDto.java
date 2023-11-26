package pl.HomeworkJustClick.Backend.comment;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import pl.HomeworkJustClick.Backend.user.UserResponseDto;

import java.time.OffsetDateTime;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto {
    @Schema(example = "0")
    private int id;
    @Schema(example = "Example title")
    private String title;
    @Schema(example = "Example desc")
    private String description;
    private String color;
    private OffsetDateTime lastUsedDate;
    private Integer counter;
    private UserResponseDto user;
}
