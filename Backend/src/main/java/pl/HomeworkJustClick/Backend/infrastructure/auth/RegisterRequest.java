package pl.HomeworkJustClick.Backend.infrastructure.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @Schema(example = "Example")
    @Size(max = 255)
    private String firstname;
    @Schema(example = "Examplowski")
    @Size(max = 255)
    private String lastname;
    @Schema(example = "dadwa@wda.com")
    @Size(max = 255)
    private String email;
    @Schema(example = "d092")
    @Size(min = 8, max = 255)
    private String password;
}
