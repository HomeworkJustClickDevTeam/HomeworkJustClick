package pl.HomeworkJustClick.Backend.Services;

import pl.HomeworkJustClick.Backend.Entities.Comment;
import pl.HomeworkJustClick.Backend.Responses.CommentResponse;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    public List<Comment> getAll();

    public Optional<Comment> getById(int id);

    public CommentResponse addWithUser(Comment comment, int user_id);

    public Boolean delete (int id);

    public Boolean changeTitleById (int id, String title);

    public Boolean changeDescriptionById (int id, String description);
}
