package pl.HomeworkJustClick.Backend.evaluation;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<EvaluationResponseDto> getAll() {
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
    public ResponseEntity<EvaluationResponseDto> getById(@PathVariable("evaluation_id") int id) {
        EvaluationResponseDto evaluation = evaluationService.getById(id);
        if(evaluation != null) {
            return new ResponseEntity<>(evaluation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/evaluations/reportedByUserId/{userId}")
    @Operation(
            summary = "Returns list of all reported evaluations associated with user.",
            responses = {
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
    public List<EvaluationResponseExtendedDto> getReportedEvaluationsByUserId(@PathVariable Integer userId) {
        return evaluationService.getReportedEvaluationsByUserId(userId);
    }

    @GetMapping("/evaluations/reportedByGroupId/{groupId}")
    @Operation(
            summary = "Returns list of all reported evaluations associated with group.",
            responses = {
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
    public List<EvaluationResponseExtendedDto> getReportedEvaluationsByGroupId(@PathVariable Integer groupId) {
        return evaluationService.getReportedEvaluationsByGroupId(groupId);
    }

    @GetMapping("/evaluations/reportedByUserIdAndGroupId/{userId}/{groupId}")
    @Operation(
            summary = "Returns list of all reported evaluations associated with user in the group with given ids.",
            responses = {
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
    public List<EvaluationResponseExtendedDto> getReportedEvaluationsByUserIdAndGroupId(@PathVariable Integer userId, @PathVariable Integer groupId) {
        return evaluationService.getReportedEvaluationsByUserIdAndGroupId(userId, groupId);
    }

    @PostMapping("/evaluation")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Creates evaluation",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EvaluationResponseExtendedDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User or solution or group not found",
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
    public EvaluationResponseExtendedDto create(@RequestBody @Valid EvaluationDto evaluationDto) {
        return evaluationService.create(evaluationDto);
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
                                    schema = @Schema(implementation = EvaluationResponseDto.class))
                    )
            },
            deprecated = true
    )
    @PostMapping("/evaluation/withUserAndSolution/{user_id}/{solution_id}")
    public ResponseEntity<EvaluationResponseDto> addWithUserAndSolution(@RequestBody Evaluation evaluation, @PathVariable("user_id") int user_id, @PathVariable("solution_id") int solution_id) {
        EvaluationResponseDto response = evaluationService.addWithUserAndSolution(evaluation, user_id, solution_id);
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
    public List<EvaluationResponseExtendedDto> getAllExtended() {
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
    public ResponseEntity<EvaluationResponseExtendedDto> getByIdExtended(@PathVariable("evaluation_id") int id) {
        EvaluationResponseExtendedDto evaluation = evaluationService.getByIdExtended(id);
        if(evaluation != null) {
            return new ResponseEntity<>(evaluation, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/extended/evaluation")
    @Hidden
    public ResponseEntity<EvaluationResponseExtendedDto> addExtended(@RequestBody Evaluation evaluation) {
        EvaluationResponseExtendedDto response = evaluationService.addExtended(evaluation);
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
                                    schema = @Schema(implementation = EvaluationResponseDto.class))
                    )
            },
            deprecated = true
    )
    @PostMapping("/extended/evaluation/withUserAndSolution/{user_id}/{solution_id}")
    public ResponseEntity<EvaluationResponseExtendedDto> addWithUserAndSolutionExtended(@RequestBody Evaluation evaluation, @PathVariable("user_id") int user_id, @PathVariable("solution_id") int solution_id) {
        EvaluationResponseExtendedDto response = evaluationService.addWithUserAndSolutionExtended(evaluation, user_id, solution_id);
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
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("/evaluation/{evaluation_id}")
    public void delete(@PathVariable("evaluation_id") int id) {
        evaluationService.delete(id);
    }

    @PutMapping("/evaluation/{evaluationId}")
    @Operation(
            summary = "Updates evaluation",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = EvaluationResponseExtendedDto.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User or solution or group not found",
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
    public EvaluationResponseExtendedDto update(@RequestBody @Valid EvaluationDto evaluationDto, @PathVariable Integer evaluationId) {
        return evaluationService.update(evaluationId, evaluationDto);
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
            },
            deprecated = true
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
            },
            deprecated = true
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
            },
            deprecated = true
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
            },
            deprecated = true
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
    public ResponseEntity<List<EvaluationResponseDto>> getAllEvaluationsByStudent(@PathVariable("student_id") int student_id) {
        List<EvaluationResponseDto> response = evaluationService.getAllEvaluationsByStudent(student_id);
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
    public ResponseEntity<List<EvaluationResponseDto>> getAllEvaluationsByStudentInGroup(@PathVariable("student_id") int student_id, @PathVariable("group_id") int group_id) {
        List<EvaluationResponseDto> response = evaluationService.getAllEvaluationsByStudentInGroup(student_id, group_id);
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
    public ResponseEntity<List<EvaluationResponseDto>> getAllEvaluationsByAssignment(@PathVariable("assignment_id") int assignment_id) {
        List<EvaluationResponseDto> response = evaluationService.getAllEvaluationsByAssignment(assignment_id);
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
    public ResponseEntity<EvaluationResponseDto> getEvaluationBySolution(@PathVariable("solution_id") int solution_id) {
        EvaluationResponseDto response = evaluationService.getEvaluationBySolution(solution_id);
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
    public ResponseEntity<List<EvaluationResponseExtendedDto>> getAllEvaluationsByStudentExtended(@PathVariable("student_id") int student_id) {
        List<EvaluationResponseExtendedDto> response = evaluationService.getAllEvaluationsByStudentExtended(student_id);
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
    public ResponseEntity<List<EvaluationResponseExtendedDto>> getAllEvaluationsByStudentInGroupExtended(@PathVariable("student_id") int student_id, @PathVariable("group_id") int group_id) {
        List<EvaluationResponseExtendedDto> response = evaluationService.getAllEvaluationsByStudentInGroupExtended(student_id, group_id);
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
    public ResponseEntity<List<EvaluationResponseExtendedDto>> getAllEvaluationsByAssignmentExtended(@PathVariable("assignment_id") int assignment_id) {
        List<EvaluationResponseExtendedDto> response = evaluationService.getAllEvaluationsByAssignmentExtended(assignment_id);
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
    public ResponseEntity<EvaluationResponseExtendedDto> getEvaluationBySolutionExtended(@PathVariable("solution_id") int solution_id) {
        EvaluationResponseExtendedDto response = evaluationService.getEvaluationBySolutionExtended(solution_id);
        if(response != null){
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Checks if solution with given id has already been evaluated. Returns true if so, false otherwise. False may also indicates that there is no solution in the DB with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "string",
                                    schema = @Schema(implementation = String.class)
                            )
                    )
            }
    )
    @GetMapping("/evaluation/checkForEvaluationToSolution/{solution_id}")
    public ResponseEntity<Boolean> checkForEvaluationToSolution(@PathVariable("solution_id") int solution_id) {
        return new ResponseEntity<>(evaluationService.checkForEvaluationToSolution(solution_id), HttpStatus.OK);
    }

}
