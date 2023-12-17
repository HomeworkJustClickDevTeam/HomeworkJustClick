package pl.homeworkjustclick.comment;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import pl.homeworkjustclick.assignment.AssignmentResponseDto;
import pl.homeworkjustclick.user.UserSimpleResponseDto;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponseDto implements Serializable {
    @Schema(example = "0")
    private int id;
    @Schema(example = "Example title")
    private String title;
    @Schema(example = "Example desc")
    private String description;
    private String color;
    private OffsetDateTime lastUsedDate;
    private Integer counter;
    private AssignmentResponseDto assignment;
    private UserSimpleResponseDto user;
}
