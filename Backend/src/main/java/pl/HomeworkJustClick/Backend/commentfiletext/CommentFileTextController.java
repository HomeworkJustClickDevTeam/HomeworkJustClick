package pl.HomeworkJustClick.Backend.commentfiletext;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/comment_file_text")
@RequiredArgsConstructor
public class CommentFileTextController {
    private final CommentFileTextService service;

    @GetMapping
    public Slice<CommentFileTextResponseDto> getCommentFileTexts(@PageableDefault(sort = "id") Pageable pageable) {
        return service.getCommentFileTexts(pageable);
    }

    @GetMapping("{id}")
    public CommentFileTextResponseDto getCommentFileTextById(@PathVariable Integer id) {
        return service.getCommentFileTextById(id);
    }

    @GetMapping("byCommentId/{commentId}")
    public Slice<CommentFileTextResponseDto> getCommentFileTextsByCommentId(@PathVariable Integer commentId, @PageableDefault(sort = "id") Pageable pageable) {
        return service.getCommentFileTextsByCommentId(commentId, pageable);
    }

    @GetMapping("byFileId/{fileId}")
    public Slice<CommentFileTextResponseDto> getCommentFileTextsByFileId(@PathVariable Integer fileId, @PageableDefault(sort = "id") Pageable pageable) {
        return service.getCommentFileTextsByFileId(fileId, pageable);
    }

    @PostMapping
    public CommentFileTextResponseDto createCommentFileText(@RequestBody @Valid CommentFileTextDto commentFileTextDto) {
        return service.createCommentFileText(commentFileTextDto);
    }

    @PutMapping("{id}")
    public CommentFileTextResponseDto updateCommentFileText(@PathVariable Integer id, @RequestBody @Valid CommentFileTextDto commentFileTextDto) {
        return service.updateCommentFileText(id, commentFileTextDto);
    }

    @DeleteMapping("{id}")
    public void deleteCommentFileText(@PathVariable Integer id) {
        service.deleteCommentFileText(id);
    }
}
