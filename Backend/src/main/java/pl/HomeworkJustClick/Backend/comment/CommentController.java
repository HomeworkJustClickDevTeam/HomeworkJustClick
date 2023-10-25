package pl.HomeworkJustClick.Backend.comment;


import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;
import pl.HomeworkJustClick.Backend.infrastructure.exception.comment.CommentNotFoundException;

@RestController
@RequestMapping("api/comment")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Comment", description = "Comment related calls.")
@ApiResponse(
        responseCode = "403",
        description = "Something is wrong with the token.",
        content = @Content()
)
@ApiResponse(
        responseCode = "200",
        description = "OK."
)
@RequiredArgsConstructor
public class CommentController {
    private final CommentService service;

    @GetMapping("/byUser")
    public Slice<CommentResponseDto> getCommentsByUser(@RequestParam Integer userId, @PageableDefault(sort = "lastUsedDate") Pageable pageable) {
        return service.getCommentsByUser(userId, pageable);
    }

    @GetMapping
    public Slice<CommentResponseDto> getComments(@PageableDefault(sort = "lastUsedDate") Pageable pageable) {
        return service.getComments(pageable);
    }

    @PostMapping
    public CommentResponseDto createComment(@RequestBody @Valid CommentDto commentDto) {
        return service.createComment(commentDto);
    }

    @PutMapping
    public CommentResponseDto updateComment(@RequestBody @Valid CommentDto commentDto, @RequestParam Integer commentId) {
        return service.updateComment(commentDto, commentId);
    }

    @DeleteMapping
    public void deleteComment(@RequestParam Integer commentId) {
        throw new CommentNotFoundException("");
//        service.deleteComment(commentId);
    }
}
