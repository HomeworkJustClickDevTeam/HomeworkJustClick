package pl.HomeworkJustClick.Backend.commentfiletext;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.comment.CommentService;
import pl.HomeworkJustClick.Backend.file.FileService;
import pl.HomeworkJustClick.Backend.infrastructure.exception.commentfiletext.CommentFileTextNotFoundException;

@Service
@RequiredArgsConstructor
public class CommentFileTextService {
    private final CommentFileTextRepository repository;
    private final CommentFileTextMapper mapper;
    private final CommentService commentService;
    private final FileService fileService;

    public CommentFileText findById(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new CommentFileTextNotFoundException("CommentFileText not found"));
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
        return mapper.map(repository.save(commentFileText));
    }

    public CommentFileTextResponseDto updateCommentFileText(Integer id, CommentFileTextDto commentFileTextDto) {
        var commentFileText = findById(id);
        mapper.map(commentFileText, commentFileTextDto);
        setRelationFields(commentFileText, commentFileTextDto);
        return mapper.map(repository.save(commentFileText));
    }

    public void deleteCommentFileText(Integer id) {
        var commentFileText = findById(id);
        repository.delete(commentFileText);
    }

    private void setRelationFields(CommentFileText commentFileText, CommentFileTextDto commentFileTextDto) {
        var comment = commentService.findById(commentFileTextDto.getCommentId());
        var file = fileService.findById(commentFileTextDto.getFileId());
        commentFileText.setComment(comment);
        commentFileText.setFile(file);
    }
}
