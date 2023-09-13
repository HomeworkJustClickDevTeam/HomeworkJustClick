package pl.HomeworkJustClick.Backend.infrastructure.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest {
    @Schema(example = "dadwa@wda.com")
    private String email;
    @Schema(example = "d092")
    private String password;
    @Schema(example = "123")
    private String newPassword;
}
