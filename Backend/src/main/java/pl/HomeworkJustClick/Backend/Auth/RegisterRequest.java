package pl.HomeworkJustClick.Backend.Auth;

import io.swagger.v3.oas.annotations.media.Schema;
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
    private String firstname;
    @Schema(example = "Examplowski")
    private String lastname;
    @Schema(example = "dadwa@wda.com")
    private String email;
    @Schema(example = "d092")
    private String password;
}
