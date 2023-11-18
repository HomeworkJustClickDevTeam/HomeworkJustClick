package pl.HomeworkJustClick.Backend.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.infrastructure.exception.EntityNotFoundException;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository repository;
    private final CommentMapper mapper;

    public Comment findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment with id = " + id + " not found"));
    }

    public Slice<CommentResponseDto> getComments(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::map);
    }

    public CommentResponseDto getCommentById(Integer commentId) {
        return mapper.map(findById(commentId));
    }

    public Slice<CommentResponseDto> getCommentsByUser(Integer userId, Pageable pageable) {
        return repository.getCommentsByUserIdAndVisible(userId, true, pageable)
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
        comment.setVisible(false);
        repository.save(comment);
    }
}
