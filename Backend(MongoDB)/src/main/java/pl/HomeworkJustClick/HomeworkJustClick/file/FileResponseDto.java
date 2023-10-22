package pl.HomeworkJustClick.HomeworkJustClick.file;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileResponseDto {
    private String id;
    private String name;
    private String format;
}
