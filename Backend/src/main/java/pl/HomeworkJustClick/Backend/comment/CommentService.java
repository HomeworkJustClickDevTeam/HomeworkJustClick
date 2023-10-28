package pl.HomeworkJustClick.Backend.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.infrastructure.exception.comment.CommentNotFoundException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository repository;
    private final CommentMapper mapper;

    public Comment findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException("Comment not found"));
    }

    public Slice<CommentResponseDto> getComments(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::map);
    }

    public Slice<CommentResponseDto> getCommentsByUser(Integer userId, Pageable pageable) {
        return repository.getCommentsByUserId(userId, pageable)
                .map(mapper::map);
    }

    public CommentResponseDto createComment(CommentDto commentDto) {
        return mapper.map(repository.save(mapper.map(commentDto)));
    }

    public CommentResponseDto updateComment(CommentDto commentDto, int commentId) {
        var comment = findById(commentId);
        mapper.map(comment, commentDto);
        return mapper.map(repository.save(comment));
    }

    public void deleteComment(int commentId) {
        var comment = findById(commentId);
        repository.delete(comment);
    }
}
