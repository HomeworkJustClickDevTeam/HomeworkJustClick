package pl.homeworkjustclick.group;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GroupResponseDto {
    @Schema(example = "0")
    private int id;
    @Schema(example = "Example group name")
    private String name;
    @Schema(example = "Example desc")
    private String description;
    @Schema(example = "0")
    private int color;
    @Schema(example = "false")
    private boolean isArchived;
}
