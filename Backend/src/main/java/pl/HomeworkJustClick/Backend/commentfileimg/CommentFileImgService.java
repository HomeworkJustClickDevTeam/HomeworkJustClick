package pl.HomeworkJustClick.Backend.commentfileimg;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.comment.CommentService;
import pl.HomeworkJustClick.Backend.file.FileService;
import pl.HomeworkJustClick.Backend.infrastructure.exception.commentfileimg.CommentFileImgNotFoundException;

@Service
@RequiredArgsConstructor
public class CommentFileImgService {
    private final CommentFileImgRepository repository;
    private final CommentFileImgMapper mapper;
    private final CommentService commentService;
    private final FileService fileService;

    public CommentFileImg findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new CommentFileImgNotFoundException("CommentFileImg not found"));
    }

    public Slice<CommentFileImgResponseDto> getCommentFileImgs(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::map);
    }

    public CommentFileImgResponseDto getCommentFileImgById(Integer id) {
        return mapper.map(findById(id));
    }

    public Slice<CommentFileImgResponseDto> getCommentFileImgByCommentId(Integer commentId, Pageable pageable) {
        return repository.getCommentFileImgsByCommentId(commentId, pageable)
                .map(mapper::map);
    }

    public Slice<CommentFileImgResponseDto> getCommentFileImgByFileId(Integer fileId, Pageable pageable) {
        return repository.getCommentFileImgsByFileId(fileId, pageable)
                .map(mapper::map);
    }

    public CommentFileImgResponseDto createCommentFileImg(CommentFileImgDto commentFileImgDto) {
        var commentFileImg = mapper.map(commentFileImgDto);
        setRelationFields(commentFileImg, commentFileImgDto);
        return mapper.map(repository.save(commentFileImg));
    }

    public CommentFileImgResponseDto updateCommentFileImg(Integer id, CommentFileImgDto commentFileImgDto) {
        var commentFileImg = findById(id);
        mapper.map(commentFileImgDto, commentFileImg);
        setRelationFields(commentFileImg, commentFileImgDto);
        return mapper.map(repository.save(commentFileImg));
    }

    public void deleteCommentFileImg(Integer id) {
        var commentFileImg = findById(id);
        repository.delete(commentFileImg);
    }

    private void setRelationFields(CommentFileImg commentFileImg, CommentFileImgDto commentFileImgDto) {
        var comment = commentService.findById(commentFileImgDto.getCommentId());
        var file = fileService.findById(commentFileImgDto.getFileId());
        commentFileImg.setComment(comment);
        commentFileImg.setFile(file);
    }
}
