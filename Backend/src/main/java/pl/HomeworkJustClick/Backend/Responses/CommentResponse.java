package pl.HomeworkJustClick.Backend.Responses;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {

    private int id;

    private String title;

    private String description;

    private int user_id;

}
