package pl.HomeworkJustClick.Backend.commentfiletext;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.comment.CommentService;
import pl.HomeworkJustClick.Backend.comment.CommentUtilsService;
import pl.HomeworkJustClick.Backend.file.FileService;
import pl.HomeworkJustClick.Backend.infrastructure.exception.EntityNotFoundException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentFileTextService {
    private final CommentFileTextRepository repository;
    private final CommentFileTextMapper mapper;
    private final CommentService commentService;
    private final FileService fileService;
    private final CommentUtilsService commentUtilsService;

    public CommentFileText findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("CommentFileText with id = " + id + " not found"));
    }

    public Slice<CommentFileTextResponseDto> getCommentFileTexts(Pageable pageable) {
        return repository.findAll(pageable)
                .map(mapper::map);
    }

    public CommentFileTextResponseDto getCommentFileTextById(Integer id) {
        return mapper.map(findById(id));
    }

    public Slice<CommentFileTextResponseDto> getCommentFileTextsByCommentId(Integer commentId, Pageable pageable) {
        return repository.findCommentFileTextsByCommentId(commentId, pageable)
                .map(mapper::map);
    }

    public Slice<CommentFileTextResponseDto> getCommentFileTextsByFileId(Integer fileId, Pageable pageable) {
        return repository.findCommentFileTextsByFileId(fileId, pageable)
                .map(mapper::map);
    }

    public CommentFileTextResponseDto createCommentFileText(CommentFileTextDto commentFileTextDto) {
        var commentFileText = mapper.map(commentFileTextDto);
        setRelationFields(commentFileText, commentFileTextDto);
        checkColor(commentFileText);
        commentUtilsService.update(commentFileTextDto.getCommentId());
        return mapper.map(repository.save(commentFileText));
    }

    public CommentFileTextResponseDto updateCommentFileText(Integer id, CommentFileTextDto commentFileTextDto) {
        var commentFileText = findById(id);
        mapper.map(commentFileText, commentFileTextDto);
        setRelationFields(commentFileText, commentFileTextDto);
        checkColor(commentFileText);
        return mapper.map(repository.save(commentFileText));
    }

    public List<CommentFileTextResponseDto> updateAllCommentFileTextColorByCommentId(Integer commentId, CommentFileTextUpdateColorDto colorDto) {
        var comment = commentService.findById(commentId);
        var response = new ArrayList<CommentFileTextResponseDto>();
        var commentFileTexts = repository.findCommentFileTextsByCommentId(comment.getId());
        commentFileTexts.forEach(commentFileText -> {
            commentFileText.setColor(colorDto.getColor());
            response.add(mapper.map(repository.save(commentFileText)));
        });
        return response;
    }

    public void deleteCommentFileText(Integer id) {
        var commentFileText = findById(id);
        repository.delete(commentFileText);
    }

    public void deleteCommentFileTextsByCommentIdAndFileId(Integer commentId, Integer fileId) {
        var commentFileTextsToDelete = repository.findCommentFileTextsByCommentIdAndFileId(commentId, fileId);
        repository.deleteAll(commentFileTextsToDelete);
    }

    private void setRelationFields(CommentFileText commentFileText, CommentFileTextDto commentFileTextDto) {
        var comment = commentService.findById(commentFileTextDto.getCommentId());
        var file = fileService.findById(commentFileTextDto.getFileId());
        commentFileText.setComment(comment);
        commentFileText.setFile(file);
    }

    private void checkColor(CommentFileText commentFileText) {
        if (commentFileText.getColor() == null) {
            commentFileText.setColor(commentFileText.getComment().getColor());
        }
    }
}
