package pl.HomeworkJustClick.Backend.infrastructure.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.HomeworkJustClick.Backend.infrastructure.enums.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponseDto {
    @Schema(example = "Bearer ...")
    private String token;
    @Schema(example = "0")
    private int id;
    @Schema(example = "USER")
    private Role role;
    @Schema(example = "Example message")
    private String message;
    @Schema(example = "0")
    private int color;
    @Schema(example = "Exampler")
    private String name;
    @Schema(example = "Examplowski")
    private String lastname;
    @Schema(example = "6969")
    private int index;
}
