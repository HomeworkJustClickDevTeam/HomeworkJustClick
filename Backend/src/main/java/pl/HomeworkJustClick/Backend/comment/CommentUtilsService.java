package pl.HomeworkJustClick.Backend.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.infrastructure.exception.EntityNotFoundException;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class CommentUtilsService {
    private final CommentRepository repository;

    public void update(Integer commentId) {
        updateLastUsedDate(commentId);
        incrementCounter(commentId);
    }

    private void updateLastUsedDate(Integer commentId) {
        var comment = repository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id = " + commentId + " not found"));
        comment.setLastUsedDate(OffsetDateTime.now());
        repository.save(comment);
    }

    private void incrementCounter(Integer commentId) {
        var comment = repository.findById(commentId)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id = " + commentId + " not found"));
        comment.setCounter(comment.getCounter() + 1);
        repository.save(comment);
    }
}
