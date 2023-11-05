package pl.HomeworkJustClick.Backend.commentfiletext;

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

@RestController
@RequestMapping("api/comment_file_text")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Comment file text", description = "Comment file text related calls.")
public class CommentFileTextController {
    private final CommentFileTextService service;

    @GetMapping
    @Operation(
            summary = "Returns paged list of commentFileText",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentFileTextResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            },
            parameters = {
                    @Parameter(name = "page", example = "0", description = "default = 0"),
                    @Parameter(name = "size", example = "10", description = "default = 20"),
                    @Parameter(name = "sort", example = "id,desc", description = "default = id,asc")
            }
    )
    public Slice<CommentFileTextResponseDto> getCommentFileTexts(@Parameter(hidden = true) @PageableDefault(sort = "id") Pageable pageable) {
        return service.getCommentFileTexts(pageable);
    }

    @GetMapping("{id}")
    @Operation(
            summary = "Returns commentFileText by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentFileTextResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "commentFileText not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public CommentFileTextResponseDto getCommentFileTextById(@PathVariable Integer id) {
        return service.getCommentFileTextById(id);
    }

    @GetMapping("byCommentId/{commentId}")
    @Operation(
            summary = "Returns paged list of commentFileTexts by commentId",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentFileTextResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "commentFileText not found",
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
                    @Parameter(name = "size", example = "10", description = "default = 20"),
                    @Parameter(name = "sort", example = "id,desc", description = "default = id,asc")
            }
    )
    public Slice<CommentFileTextResponseDto> getCommentFileTextsByCommentId(@PathVariable Integer commentId, @Parameter(hidden = true) @PageableDefault(sort = "id") Pageable pageable) {
        return service.getCommentFileTextsByCommentId(commentId, pageable);
    }

    @GetMapping("byFileId/{fileId}")
    @Operation(
            summary = "Returns paged list of commentFileTexts by fileId",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentFileTextResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "commentFileText not found",
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
                    @Parameter(name = "size", example = "10", description = "default = 20"),
                    @Parameter(name = "sort", example = "id,desc", description = "default = id,asc")
            }
    )
    public Slice<CommentFileTextResponseDto> getCommentFileTextsByFileId(@PathVariable Integer fileId, @Parameter(hidden = true) @PageableDefault(sort = "id") Pageable pageable) {
        return service.getCommentFileTextsByFileId(fileId, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Creates commentFileText",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentFileTextResponseDto.class))
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
    public CommentFileTextResponseDto createCommentFileText(@RequestBody @Valid CommentFileTextDto commentFileTextDto) {
        return service.createCommentFileText(commentFileTextDto);
    }

    @PutMapping("{id}")
    @Operation(
            summary = "Updates commentFileText",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentFileTextResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "CommentFileText or comment or file not found",
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
    public CommentFileTextResponseDto updateCommentFileText(@PathVariable Integer id, @RequestBody @Valid CommentFileTextDto commentFileTextDto) {
        return service.updateCommentFileText(id, commentFileTextDto);
    }

    @DeleteMapping("{id}")
    @Operation(
            summary = "Deletes commentFileText",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "CommentFileText not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public void deleteCommentFileText(@PathVariable Integer id) {
        service.deleteCommentFileText(id);
    }
}
