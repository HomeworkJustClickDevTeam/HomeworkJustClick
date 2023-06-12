package pl.HomeworkJustClick.Backend.Controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.HomeworkJustClick.Backend.Entities.Evaluation;
import pl.HomeworkJustClick.Backend.Entities.User;
import pl.HomeworkJustClick.Backend.Responses.EvaluationResponse;
import pl.HomeworkJustClick.Backend.Responses.EvaluationResponseExtended;
import pl.HomeworkJustClick.Backend.Services.EvaluationService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Evaluation", description = "Evaluation related calls.")
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
public class EvaluationController {

    private final EvaluationService evaluationService;

    @GetMapping("/evaluations")
    @Operation(summary = "Returns list of all evaluations in DB.",
            responses =
            @ApiResponse(
                    responseCode = "200",
                    description = "List returned.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Evaluation.class)))
            )
    )
    public List<EvaluationResponse> getAll(){
        return evaluationService.getAll();
    }

    @Operation(
            summary = "Returns evaluation by it's id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No evaluation with this id in the DB.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Evaluation.class))
                    )
            }
    )
    @GetMapping("/evaluations/{evaluation_id}")
    public ResponseEntity<EvaluationResponse> getById(@PathVariable("evaluation_id") int id){
        EvaluationResponse evaluation = evaluationService.getById(id);
        if(evaluation != null) {
            return new ResponseEntity<>(evaluation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/evaluation")
    @Hidden
    public ResponseEntity<EvaluationResponse> add(@RequestBody Evaluation evaluation){
        EvaluationResponse response = evaluationService.add(evaluation);
        return ResponseEntity.ok(response);
    }
    @Operation(
            summary = "Creates evaluation with user and solution already attached to it.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No user or solution with those ids in the DB.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The user is not a teacher in the group associated with the solution with given id.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EvaluationResponse.class))
                    )
            }
    )
    @PostMapping("/evaluation/withUserAndSolution/{user_id}/{solution_id}")
    public ResponseEntity<EvaluationResponse> addWithUserAndSolution(@RequestBody Evaluation evaluation, @PathVariable("user_id") int user_id, @PathVariable("solution_id") int solution_id) {
        EvaluationResponse response = evaluationService.addWithUserAndSolution(evaluation, user_id, solution_id);
        if(response.getId()!=0) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (response.isForbidden()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/extended/evaluations")
    @Operation(summary = "Returns list of all evaluations in DB.",
            responses =
            @ApiResponse(
                    responseCode = "200",
                    description = "List returned.",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Evaluation.class)))
            )
    )
    public List<EvaluationResponseExtended> getAllExtended(){
        return evaluationService.getAllExtended();
    }

    @Operation(
            summary = "Returns evaluation by it's id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No evaluation with this id in the DB.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Evaluation.class))
                    )
            }
    )
    @GetMapping("/extended/evaluations/{evaluation_id}")
    public ResponseEntity<EvaluationResponseExtended> getByIdExtended(@PathVariable("evaluation_id") int id){
        EvaluationResponseExtended evaluation = evaluationService.getByIdExtended(id);
        if(evaluation != null) {
            return new ResponseEntity<>(evaluation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/extended/evaluation")
    @Hidden
    public ResponseEntity<EvaluationResponseExtended> addExtended(@RequestBody Evaluation evaluation){
        EvaluationResponseExtended response = evaluationService.addExtended(evaluation);
        return ResponseEntity.ok(response);
    }
    @Operation(
            summary = "Creates evaluation with user and solution already attached to it.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No user or solution with those ids in the DB.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The user is not a teacher in the group associated with the solution with given id.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EvaluationResponse.class))
                    )
            }
    )
    @PostMapping("/extended/evaluation/withUserAndSolution/{user_id}/{solution_id}")
    public ResponseEntity<EvaluationResponseExtended> addWithUserAndSolutionExtended(@RequestBody Evaluation evaluation, @PathVariable("user_id") int user_id, @PathVariable("solution_id") int solution_id) {
        EvaluationResponseExtended response = evaluationService.addWithUserAndSolutionExtended(evaluation, user_id, solution_id);
        if(response.getId()!=0) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (response.isForbidden()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Deletes evaluation with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing evaluation with this id.",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("/evaluation/{evaluation_id}")
    public ResponseEntity<Void> delete(@PathVariable("evaluation_id") int id){
        if(evaluationService.delete(id)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/evaluation/result/{evaluation_id}")
    @Operation(
            summary = "Changes the result of evaluation with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing evaluation with this id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> updateResult(@PathVariable("evaluation_id") int id, @RequestBody Double result){
        if(evaluationService.changeResultById(id, result)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

    }
    @PutMapping("/evaluation/user/{user_id}/{evaluation_id}")
    @Operation(
            summary = "Changes the user of evaluation with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing evaluation or user with the id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> updateUser(@PathVariable("user_id") int userId, @PathVariable("evaluation_id") int id){
        if(evaluationService.changeUserById(id, userId)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/evaluation/solution/{solution_id}/{evaluation_id}")
    @Operation(
            summary = "Changes the solution of evaluation with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing evaluation or solution with the id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> updateSolution(@PathVariable("solution_id") int solutionId, @PathVariable("evaluation_id") int id){
        if(evaluationService.changeSolutionById(id, solutionId)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/evaluation/grade/{evaluation_id}")
    @Operation(
            summary = "Changes the grade of evaluation with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing evaluation with the id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> updateGrade(@RequestBody Double grade, @PathVariable("evaluation_id") int id){
        if(evaluationService.changeGradeById(id, grade)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/evaluations/byStudent/{student_id}")
    @Operation(
            summary = "Returns list of all evaluations associated with user with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "User with given id has no evaluations.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Evaluation.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<EvaluationResponse>> getAllEvaluationsByStudent(@PathVariable("student_id") int student_id) {
        List<EvaluationResponse> response = evaluationService.getAllEvaluationsByStudent(student_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
    @Operation(
            summary = "Returns list of all evaluations associated with user in the group with given ids.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "User in the given group has no evaluations.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Evaluation.class))
                            )
                    )
            }
    )
    @GetMapping("/evaluations/byStudentAndGroup/{student_id}/{group_id}")
    public ResponseEntity<List<EvaluationResponse>> getAllEvaluationsByStudentInGroup(@PathVariable("student_id") int student_id, @PathVariable("group_id") int group_id) {
        List<EvaluationResponse> response = evaluationService.getAllEvaluationsByStudentInGroup(student_id, group_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @Operation(
            summary = "Returns list of all evaluations associated with assignment.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No evaluations for the given assignment.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Evaluation.class))
                            )
                    )
            }
    )
    @GetMapping("/evaluations/byAssignment/{assignment_id}")
    public ResponseEntity<List<EvaluationResponse>> getAllEvaluationsByAssignment(@PathVariable("assignment_id") int assignment_id) {
        List<EvaluationResponse> response = evaluationService.getAllEvaluationsByAssignment(assignment_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @Operation(
            summary = "Returns evaluation associated with the solution.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No evaluation for the given solution.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Evaluation.class)
                            )
                    )
            }
    )

    @GetMapping("/evaluation/bySolution/{solution_id}")
    public ResponseEntity<EvaluationResponse> getEvaluationBySolution(@PathVariable("solution_id") int solution_id) {
        EvaluationResponse response = evaluationService.getEvaluationBySolution(solution_id);
        if(response != null){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/extended/evaluations/byStudent/{student_id}")
    @Operation(
            summary = "Returns list of all evaluations associated with user with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "User with given id has no evaluations.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Evaluation.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<EvaluationResponseExtended>> getAllEvaluationsByStudentExtended(@PathVariable("student_id") int student_id) {
        List<EvaluationResponseExtended> response = evaluationService.getAllEvaluationsByStudentExtended(student_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }
    @Operation(
            summary = "Returns list of all evaluations associated with user in the group with given ids.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "User in the given group has no evaluations.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Evaluation.class))
                            )
                    )
            }
    )
    @GetMapping("/extended/evaluations/byStudentAndGroup/{student_id}/{group_id}")
    public ResponseEntity<List<EvaluationResponseExtended>> getAllEvaluationsByStudentInGroupExtended(@PathVariable("student_id") int student_id, @PathVariable("group_id") int group_id) {
        List<EvaluationResponseExtended> response = evaluationService.getAllEvaluationsByStudentInGroupExtended(student_id, group_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @Operation(
            summary = "Returns list of all evaluations associated with assignment.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No evaluations for the given assignment.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Evaluation.class))
                            )
                    )
            }
    )
    @GetMapping("/extended/evaluations/byAssignment/{assignment_id}")
    public ResponseEntity<List<EvaluationResponseExtended>> getAllEvaluationsByAssignmentExtended(@PathVariable("assignment_id") int assignment_id) {
        List<EvaluationResponseExtended> response = evaluationService.getAllEvaluationsByAssignmentExtended(assignment_id);
        if(response.isEmpty()){
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
    }

    @Operation(
            summary = "Returns evaluation associated with the solution.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No evaluation for the given solution.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Evaluation.class)
                            )
                    )
            }
    )

    @GetMapping("/extended/evaluation/bySolution/{solution_id}")
    public ResponseEntity<EvaluationResponseExtended> getEvaluationBySolutionExtended(@PathVariable("solution_id") int solution_id) {
        EvaluationResponseExtended response = evaluationService.getEvaluationBySolutionExtended(solution_id);
        if(response != null){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

}
