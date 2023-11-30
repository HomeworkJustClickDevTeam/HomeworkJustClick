package pl.HomeworkJustClick.Backend.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.assignment.AssignmentService;
import pl.HomeworkJustClick.Backend.infrastructure.exception.EntityNotFoundException;
import pl.HomeworkJustClick.Backend.user.UserService;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository repository;
    private final CommentMapper mapper;
    private final AssignmentService assignmentService;
    private final UserService userService;

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

    public Slice<CommentResponseDto> getCommentsByAssignment(Integer assignmentId, Pageable pageable) {
        return repository.getCommentsByAssignmentIdAndVisible(assignmentId, true, pageable)
                .map(mapper::map);
    }

    public Slice<CommentResponseDto> getCommentsByUser(Integer userId, Pageable pageable) {
        return repository.getCommentsByUserIdAndVisible(userId, true, pageable)
                .map(mapper::map);
    }

    public Slice<CommentResponseDto> getCommentsByUserAndAssignment(Integer userId, Integer assignmentId, Pageable pageable) {
        return repository.getCommentsByUserIdAndAssignmentIdAndVisible(userId, assignmentId, true, pageable)
                .map(mapper::map);
    }

    public CommentResponseDto createComment(CommentDto commentDto) {
        var comment = mapper.map(commentDto);
        setRelationFields(commentDto, comment);
        return mapper.map(repository.save(comment));
    }

    public CommentResponseDto updateComment(CommentDto commentDto, int commentId) {
        var comment = findById(commentId);
        mapper.map(comment, commentDto);
        setRelationFields(commentDto, comment);
        return mapper.map(repository.save(comment));
    }

    public void deleteComment(int commentId) {
        var comment = findById(commentId);
        comment.setVisible(false);
        repository.save(comment);
    }

    private void setRelationFields(CommentDto commentDto, Comment comment) {
        comment.setAssignment(assignmentService.findById(commentDto.getAssignmentId()));
        comment.setUser(userService.findById(commentDto.getUserId()));
    }
}
