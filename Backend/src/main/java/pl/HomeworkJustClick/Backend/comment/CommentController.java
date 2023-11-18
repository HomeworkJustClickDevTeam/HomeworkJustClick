package pl.HomeworkJustClick.Backend.comment;


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
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    @Operation(
            summary = "Returns paged list of comments.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentResponseDto.class))
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
                    @Parameter(name = "sort", example = "counter,desc", description = "default = lastUsedDate,desc")
            }
    )
    public Slice<CommentResponseDto> getComments(@Parameter(hidden = true) @PageableDefault(sort = "lastUsedDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return service.getComments(pageable);
    }

    @GetMapping("{commentId}")
    @Operation(
            summary = "Returns comment by it's id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No comment with this id in the DB.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public CommentResponseDto getCommentById(@PathVariable Integer commentId) {
        return service.getCommentById(commentId);
    }

    @GetMapping("/byUser/{userId}")
    @Operation(
            summary = "Returns paged list of comments by user id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No comment with this userId in the DB.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentResponseDto.class))
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
                    @Parameter(name = "sort", example = "counter,desc", description = "default = lastUsedDate,desc")
            }
    )
    public Slice<CommentResponseDto> getCommentsByUser(@PathVariable Integer userId, @Parameter(hidden = true) @PageableDefault(sort = "lastUsedDate", direction = Sort.Direction.DESC) Pageable pageable) {
        return service.getCommentsByUser(userId, pageable);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Creates comment",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not found",
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
    public CommentResponseDto createComment(@RequestBody @Valid CommentDto commentDto) {
        return service.createComment(commentDto);
    }

    @PutMapping("{commentId}")
    @Operation(
            summary = "Updates comment",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentResponseDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Comment or user not found",
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
    public CommentResponseDto updateComment(@PathVariable Integer commentId, @RequestBody @Valid CommentDto commentDto) {
        return service.updateComment(commentDto, commentId);
    }

    @DeleteMapping("{commentId}")
    @Operation(
            summary = "Deletes comment by id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Comment not found",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    public void deleteComment(@PathVariable Integer commentId) {
        service.deleteComment(commentId);
    }
}
