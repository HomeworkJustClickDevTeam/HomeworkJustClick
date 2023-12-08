package pl.HomeworkJustClick.Backend.user;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSimpleResponseDto implements Serializable {
    private Integer id;
    private String firstname;
    private String lastname;
    private Integer index;
}
