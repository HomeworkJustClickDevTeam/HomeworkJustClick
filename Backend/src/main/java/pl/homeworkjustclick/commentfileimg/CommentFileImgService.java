package pl.homeworkjustclick.commentfileimg;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import pl.homeworkjustclick.comment.CommentService;
import pl.homeworkjustclick.comment.CommentUtilsService;
import pl.homeworkjustclick.file.FileService;
import pl.homeworkjustclick.infrastructure.exception.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentFileImgService {
    private final CommentFileImgRepository repository;
    private final CommentFileImgMapper mapper;
    private final CommentService commentService;
    private final FileService fileService;
    private final CommentUtilsService commentUtilsService;

    public CommentFileImg findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CommentFileImg with id = " + id + " not found"));
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
        checkColor(commentFileImg);
        commentUtilsService.update(commentFileImgDto.getCommentId());
        return mapper.map(repository.save(commentFileImg));
    }

    public CommentFileImgResponseDto updateCommentFileImg(Integer id, CommentFileImgDto commentFileImgDto) {
        var commentFileImg = findById(id);
        mapper.map(commentFileImgDto, commentFileImg);
        setRelationFields(commentFileImg, commentFileImgDto);
        checkColor(commentFileImg);
        return mapper.map(repository.save(commentFileImg));
    }

    public List<CommentFileImgResponseDto> changeAllCommentFileImgColorByCommentId(Integer commentId, CommentFileImgUpdateColorDto newColor) {
        var comment = commentService.findById(commentId);
        var commentFileImgs = repository.getCommentFileImgsByCommentId(comment.getId());
        var response = new ArrayList<CommentFileImgResponseDto>();
        commentFileImgs.forEach(commentFileImg -> {
            commentFileImg.setColor(newColor.getColor());
            response.add(mapper.map(repository.save(commentFileImg)));
        });
        return response;
    }

    public void deleteCommentFileImg(Integer id) {
        var commentFileImg = findById(id);
        repository.delete(commentFileImg);
    }

    public void deleteCommentFileImgsByCommentIdAndFileId(Integer commentId, Integer fileId) {
        var commentFileImgsToDelete = repository.getCommentFileImgsByCommentIdAndFileId(commentId, fileId);
        repository.deleteAll(commentFileImgsToDelete);
    }

    private void setRelationFields(CommentFileImg commentFileImg, CommentFileImgDto commentFileImgDto) {
        var comment = commentService.findById(commentFileImgDto.getCommentId());
        var file = fileService.findById(commentFileImgDto.getFileId());
        commentFileImg.setComment(comment);
        commentFileImg.setFile(file);
    }

    private void checkColor(CommentFileImg commentFileImg) {
        if (commentFileImg.getColor() == null) {
            commentFileImg.setColor(commentFileImg.getComment().getColor());
        }
    }
}
