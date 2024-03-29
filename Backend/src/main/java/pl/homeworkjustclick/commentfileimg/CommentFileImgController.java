package pl.homeworkjustclick.commentfileimg;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/comment_file_img")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Comment file img", description = "Comment file img related calls.")
@ApiResponse(
        responseCode = "403",
        description = "Something is wrong with the token.",
        content = @Content()
)
@ApiResponse(
        responseCode = "200",
        description = "OK."
)
public class CommentFileImgController {
    private final CommentFileImgService service;

    @GetMapping
    @Operation(
            summary = "Returns paged list of commentFileImgs",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentFileImgResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            },
            parameters = {
                    @Parameter(name = "page", example = "0", description = "default = 0"),
                    @Parameter(name = "size", example = "20", description = "default = 10"),
                    @Parameter(name = "sort", example = "id,desc", description = "default = id,asc")
            }
    )
    public Slice<CommentFileImgResponseDto> getCommentFileImgs(@Parameter(hidden = true) @PageableDefault(sort = "id") Pageable pageable) {
        return service.getCommentFileImgs(pageable);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "Returns commentFileImg by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentFileImgResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "commentFileImg not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public CommentFileImgResponseDto getCommentFileImgById(@PathVariable Integer id) {
        return service.getCommentFileImgById(id);
    }

    @GetMapping("byCommentId/{commentId}")
    @Operation(
            summary = "Returns paged list of commentFileImgs by commentId",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentFileImgResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "CommentFileImg not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            },
            parameters = {
                    @Parameter(name = "page", example = "0", description = "default = 0"),
                    @Parameter(name = "size", example = "20", description = "default = 10"),
                    @Parameter(name = "sort", example = "id,desc", description = "default = id,asc")
            }
    )
    public Slice<CommentFileImgResponseDto> getCommentFileImgsByCommentId(@PathVariable Integer commentId, @Parameter(hidden = true) @PageableDefault(sort = "id") Pageable pageable) {
        return service.getCommentFileImgByCommentId(commentId, pageable);
    }

    @GetMapping("byFileId/{fileId}")
    @Operation(
            summary = "Returns paged list of commentFileImgs by fileId",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentFileImgResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "CommentFileImg not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            },
            parameters = {
                    @Parameter(name = "page", example = "0", description = "default = 0"),
                    @Parameter(name = "size", example = "20", description = "default = 10"),
                    @Parameter(name = "sort", example = "id,desc", description = "default = id,asc")
            }
    )
    public Slice<CommentFileImgResponseDto> getCommentFileImgsByFileId(@PathVariable Integer fileId, @Parameter(hidden = true) @PageableDefault(sort = "id") Pageable pageable) {
        return service.getCommentFileImgByFileId(fileId, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Creates commentFileImg",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentFileImgResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Comment or file not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid dto",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public CommentFileImgResponseDto createCommentFileImg(@RequestBody @Valid CommentFileImgDto commentFileImgDto) {
        return service.createCommentFileImg(commentFileImgDto);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "Updates commentFileImg",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentFileImgResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "CommentFileImg or comment or evaluation not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid dto",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public CommentFileImgResponseDto updateCommentFileImg(@PathVariable Integer id, @RequestBody @Valid CommentFileImgDto commentFileImgDto) {
        return service.updateCommentFileImg(id, commentFileImgDto);
    }

    @PutMapping("colorByCommentId/{commentId}")
    @Operation(
            summary = "Updates all commentFileImgs colors by commentId",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentFileImgResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Invalid dto",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public List<CommentFileImgResponseDto> changeAllCommentFileImgColorByCommentId(@PathVariable Integer commentId, @RequestBody @Valid CommentFileImgUpdateColorDto color) {
        return service.changeAllCommentFileImgColorByCommentId(commentId, color);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Deletes commentFileImg",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "CommentFileImg not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public void deleteCommentFileImg(@PathVariable Integer id) {
        service.deleteCommentFileImg(id);
    }

    @DeleteMapping("/byCommentFile/{commentId}/{fileId}")
    @Operation(
            summary = "Deletes commentFileImg with given commentId and fileId",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public void deleteCommentFileImgsByCommentIdAndFileId(@PathVariable Integer commentId, @PathVariable Integer fileId) {
        service.deleteCommentFileImgsByCommentIdAndFileId(commentId, fileId);
    }
}
