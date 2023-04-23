package pl.HomeworkJustClick.Backend.Responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.HomeworkJustClick.Backend.Enums.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {

    private String token;
    private int id;
    private Role role;
    private String message;
    private int color;
    private String name;
    private String lastname;
    private int index;
}
