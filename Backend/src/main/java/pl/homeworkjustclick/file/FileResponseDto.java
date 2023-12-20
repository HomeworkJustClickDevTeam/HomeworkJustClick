package pl.homeworkjustclick.file;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileResponseDto implements Serializable {
    @Schema(example = "0")
    private int id;
    @Schema(example = "Example file")
    private String name;
    @Schema(example = ".txt")
    private String format;
    @Schema(example = "0")
    private String mongoId;
}
