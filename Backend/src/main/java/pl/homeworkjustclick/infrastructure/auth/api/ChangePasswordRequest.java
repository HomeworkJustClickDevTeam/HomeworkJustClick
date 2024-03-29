package pl.homeworkjustclick.infrastructure.auth.api;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordRequest implements Serializable {
    @Schema(example = "dadwa@wda.com")
    @Size(max = 255)
    private String email;
    @Schema(example = "d092")
    @Size(min = 8, max = 255)
    private String password;
    @Schema(example = "123")
    @Size(min = 8, max = 255)
    private String newPassword;
}
