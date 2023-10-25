package pl.HomeworkJustClick.Backend.commentfileimg;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/comment_file_img")
@RequiredArgsConstructor
public class CommentFileImgController {
    private final CommentFileImgService service;

    @GetMapping
    public Slice<CommentFileImgResponseDto> getCommentFileImgs(@PageableDefault(sort = "id") Pageable pageable) {
        return service.getCommentFileImgs(pageable);
    }

    @GetMapping("{id}")
    public CommentFileImgResponseDto getCommentFileImgById(@PathVariable Integer id) {
        return service.getCommentFileImgById(id);
    }

    @GetMapping("byCommentId/{commentId}")
    public Slice<CommentFileImgResponseDto> getCommentFileImgsByCommentId(@PathVariable Integer commentId, @PageableDefault(sort = "id") Pageable pageable) {
        return service.getCommentFileImgByCommentId(commentId, pageable);
    }

    @GetMapping("byFileId/{fileId}")
    public Slice<CommentFileImgResponseDto> getCommentFileImgsByFileId(@PathVariable Integer fileId, @PageableDefault(sort = "id") Pageable pageable) {
        return service.getCommentFileImgByFileId(fileId, pageable);
    }

    @PostMapping
    public CommentFileImgResponseDto createCommentFileImg(@RequestBody @Valid CommentFileImgDto commentFileImgDto) {
        return service.createCommentFileImg(commentFileImgDto);
    }

    @PutMapping("{id}")
    public CommentFileImgResponseDto updateCommentFileImg(@PathVariable Integer id, @RequestBody @Valid CommentFileImgDto commentFileImgDto) {
        return service.updateCommentFileImg(id, commentFileImgDto);
    }

    @DeleteMapping("{id}")
    public void deleteCommentFileImg(@PathVariable Integer id) {
        service.deleteCommentFileImg(id);
    }
}
