package pl.HomeworkJustClick.Backend.Controllers;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.HomeworkJustClick.Backend.Entities.Comment;
import pl.HomeworkJustClick.Backend.Entities.User;
import pl.HomeworkJustClick.Backend.Responses.CommentResponse;
import pl.HomeworkJustClick.Backend.Services.CommentEvaluationService;
import pl.HomeworkJustClick.Backend.Services.CommentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
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

    private final CommentService commentService;


    private final CommentEvaluationService commentEvaluationService;

    @GetMapping("/comments")
    @Operation(summary = "Returns list of all comments in DB.",
            responses =
            @ApiResponse(
                    responseCode = "200",
                    description = "List returned.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Comment.class)))
            )
    )
    public List<Comment> getAll() {
        return commentService.getAll();
    }

    @GetMapping("/comment/{comment_id}")
    @Operation(
            summary = "Gets comment by it's id.",
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
                                    schema = @Schema(implementation = Comment.class))
                    )
            }
    )
    public ResponseEntity<Comment> getById(@PathVariable("comment_id") int id) {
        Optional<Comment> comment = commentService.getById(id);
        return comment.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/comments/byUser/{user_id}")
    @Operation(
            summary = "Returns list of all comments written by a user with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing user with this id in the DB or this user has not written any comments.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Comment.class)))
                    )
            }
    )

    public ResponseEntity<List<Comment>> getByUser(@PathVariable("user_id") int id) {
        List<Comment> response = commentService.getCommentsByUser(id);
        if(response.isEmpty()){
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @GetMapping("/comments/byEvaluation/{evaluation_id}")
    @Operation(
            summary = "Returns list of all comments associated with an evaluation.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing evaluation with this id in the DB or this evaluation have not any comments associated.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Comment.class)))
                    )
            }
    )
    public ResponseEntity<List<Comment>> getByEvaluation(@PathVariable("evaluation_id") int id) {
        List<Comment> response = commentService.getCommentsByEvaluation(id);
        if(response.isEmpty())
        {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @Operation(
            summary = "Creates comment with user already attached to it.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing user with this id in the db.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = CommentResponse.class)
                            )
                    )
            }
    )
    @PostMapping("/comment/addWithUser/{user_id}")
    public ResponseEntity<CommentResponse> addWithUser(@PathVariable("user_id") int user_id, @RequestBody Comment comment) {
        CommentResponse response = commentService.addWithUser(comment, user_id);
        if (response.getId() != 0) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/comment/addCommentToEvaluation/{comment_id}/{evaluation_id}")
    @Operation(
            summary = "Adds comment to evaluation.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing comment or evaluation with this id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> addCommentToEvaluation(@PathVariable("comment_id") int comment_id, @PathVariable("evaluation_id") int evaluation_id) {
        if(commentEvaluationService.addWithCommentAndEvaluation(comment_id, evaluation_id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/comment/{comment_id}")
    @Operation(
            summary = "Deletes comment.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing comment with this id in the db.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> delete (@PathVariable("comment_id") int id) {
        if (commentService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/comment/fromEvaluation/{comment_id}/{evaluation_id}")
    @Operation(
            summary = "Deletes connection between comment and evaluation.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing comment or evaluation with this id in the db.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> deleteCommentFromEvaluation (@PathVariable("comment_id") int comment_id, @PathVariable("evaluation_id") int evaluation_id) {
        if(commentEvaluationService.deleteFromEvaluation(comment_id, evaluation_id)){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/comment/changeTitleById/{comment_id}")
    @Operation(
            summary = "Changes comment's title.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing comment with this id in the db.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> changeTitleById (@PathVariable("comment_id") int id, @RequestBody String title) {
        if (commentService.changeTitleById(id, title)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/comment/changeDescriptionById/{comment_id}")
    @Operation(
            summary = "Changes comment's description.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing comment with this id in the db.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> changeDescriptionById (@PathVariable("comment_id") int id, @RequestBody String description ) {
        if(commentService.changeDescriptionById(id, description)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
