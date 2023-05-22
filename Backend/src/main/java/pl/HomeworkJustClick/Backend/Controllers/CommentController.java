package pl.HomeworkJustClick.Backend.Controllers;


import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.HomeworkJustClick.Backend.Entities.Comment;
import pl.HomeworkJustClick.Backend.Responses.CommentResponse;
import pl.HomeworkJustClick.Backend.Services.CommentEvaluationService;
import pl.HomeworkJustClick.Backend.Services.CommentService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Comment")
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    CommentEvaluationService commentEvaluationService;

    @GetMapping("/comments")
    public List<Comment> getAll() {
        return commentService.getAll();
    }

    @GetMapping("/comment/{id}")
    public ResponseEntity<Comment> getById(@PathVariable("id") int id) {
        Optional<Comment> comment = commentService.getById(id);
        return comment.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/comments/byUser/{user_id}")
    public List<Comment> getByUser(@PathVariable("user_id") int id) {
        return commentService.getCommentsByUser(id);
    }

    @GetMapping("/comments/byEvaluation/{evaluation_id}")
    public List<Comment> getByEvaluation(@PathVariable("evaluation_id") int id) {
        return commentService.getCommentsByEvaluation(id);
    }

    @PostMapping("/comment/addWithUser/{user_id}")
    public ResponseEntity<CommentResponse> addWithUser(@PathVariable("user_id") int user_id, @RequestBody Comment comment) {
        CommentResponse response = commentService.addWithUser(comment, user_id);
        if (response.getId() != 0) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/comment/addCommentToEvaluation/{comment_id}/{evaluation_id}")
    public ResponseEntity<Void> addCommentToEvaluation(@PathVariable("comment_id") int comment_id, @PathVariable("evaluation_id") int evaluation_id) {
        if(commentEvaluationService.addWithCommentAndEvaluation(comment_id, evaluation_id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/comment/{id}")
    public ResponseEntity<Void> delete (@PathVariable("id") int id) {
        if (commentService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/comment/fromEvaluation/{comment_id}/{evaluation_id}")
    public ResponseEntity<Void> deleteCommentFromEvaluation (@PathVariable("comment_id") int comment_id, @PathVariable("evaluation_id") int evaluation_id) {
        if(commentEvaluationService.deleteFromEvaluation(comment_id, evaluation_id)){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/comment/changeTitleById/{id}")
    public ResponseEntity<Void> changeTitleById (@PathVariable("id") int id, @RequestBody String title) {
        if (commentService.changeTitleById(id, title)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/comment/changeDescriptionById/{id}")
    public ResponseEntity<Void> changeDescriptionById (@PathVariable("id") int id, @RequestBody String description ) {
        if(commentService.changeDescriptionById(id, description)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
