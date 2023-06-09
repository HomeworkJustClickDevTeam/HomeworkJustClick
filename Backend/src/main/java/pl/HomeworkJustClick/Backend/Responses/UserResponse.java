package pl.HomeworkJustClick.Backend.Responses;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.HomeworkJustClick.Backend.Enums.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    @Schema(example = "0")
    int id;
    @Schema(example = "jan_kowalski@gmail.com")
    String email;
    @Schema(example = "USER")
    Role role;
    @Schema(example = "123456")
    int index;
    @Schema(example = "Jan")
    String firstname;
    @Schema(example = "Kowalski")
    String lastname;
    @Schema(example = "3")
    int color;
    @Schema(example = "jan_kowalski@gmail.com")
    String username;
    @Schema(example = "true")
    boolean verified;
}
