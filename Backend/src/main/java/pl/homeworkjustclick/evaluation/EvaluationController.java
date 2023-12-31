package pl.homeworkjustclick.evaluation;

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
                            array = @ArraySchema(schema = @Schema(implementation = EvaluationResponseDto.class)))
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
                                    schema = @Schema(implementation = EvaluationResponseDto.class))
                    )
            }
    )
    @GetMapping("/evaluations/{evaluationId}")
    public ResponseEntity<EvaluationResponseDto> getById(@PathVariable("evaluationId") int id) {
        EvaluationResponseDto evaluation = evaluationService.getById(id);
        if (evaluation != null) {
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
                                    array = @ArraySchema(schema = @Schema(implementation = EvaluationResponseExtendedDto.class))
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
                                    array = @ArraySchema(schema = @Schema(implementation = EvaluationResponseExtendedDto.class))
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
                                    array = @ArraySchema(schema = @Schema(implementation = EvaluationResponseExtendedDto.class))
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
    @PostMapping("/evaluation/withUserAndSolution/{userId}/{solutionId}")
    public ResponseEntity<EvaluationResponseDto> addWithUserAndSolution(@RequestBody Evaluation evaluation, @PathVariable("userId") int userId, @PathVariable("solutionId") int solutionId) {
        EvaluationResponseDto response = evaluationService.addWithUserAndSolution(evaluation, userId, solutionId);
        if (response.getId() != 0) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (response.isForbidden()) {
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
                            array = @ArraySchema(schema = @Schema(implementation = EvaluationResponseExtendedDto.class)))
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
                                    schema = @Schema(implementation = EvaluationResponseExtendedDto.class))
                    )
            }
    )
    @GetMapping("/extended/evaluations/{evaluationId}")
    public ResponseEntity<EvaluationResponseExtendedDto> getByIdExtended(@PathVariable("evaluationId") int id) {
        EvaluationResponseExtendedDto evaluation = evaluationService.getByIdExtended(id);
        if (evaluation != null) {
            return new ResponseEntity<>(evaluation, HttpStatus.OK);
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
    @DeleteMapping("/evaluation/{evaluationId}")
    public void delete(@PathVariable("evaluationId") int id) {
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

    @GetMapping("/evaluations/byStudent/{studentId}")
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
                                    array = @ArraySchema(schema = @Schema(implementation = EvaluationResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<EvaluationResponseDto>> getAllEvaluationsByStudent(@PathVariable("studentId") int studentId) {
        List<EvaluationResponseDto> response = evaluationService.getAllEvaluationsByStudent(studentId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else {
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
                                    array = @ArraySchema(schema = @Schema(implementation = EvaluationResponseDto.class))
                            )
                    )
            }
    )
    @GetMapping("/evaluations/byStudentAndGroup/{studentId}/{groupId}")
    public ResponseEntity<List<EvaluationResponseDto>> getAllEvaluationsByStudentInGroup(@PathVariable("studentId") int studentId, @PathVariable("groupId") int groupId) {
        List<EvaluationResponseDto> response = evaluationService.getAllEvaluationsByStudentInGroup(studentId, groupId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else {
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
                                    array = @ArraySchema(schema = @Schema(implementation = EvaluationResponseDto.class))
                            )
                    )
            }
    )
    @GetMapping("/evaluations/byAssignment/{assignmentId}")
    public ResponseEntity<List<EvaluationResponseDto>> getAllEvaluationsByAssignment(@PathVariable("assignmentId") int assignmentId) {
        List<EvaluationResponseDto> response = evaluationService.getAllEvaluationsByAssignment(assignmentId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else {
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
                                    schema = @Schema(implementation = EvaluationResponseDto.class)
                            )
                    )
            }
    )

    @GetMapping("/evaluation/bySolution/{solutionId}")
    public ResponseEntity<EvaluationResponseDto> getEvaluationBySolution(@PathVariable("solutionId") int solutionId) {
        EvaluationResponseDto response = evaluationService.getEvaluationBySolution(solutionId);
        if (response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/evaluation/byGroupId/{groupId}")
    @Operation(
            summary = "Returns list of all evaluations associated with group with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = EvaluationResponseDto.class))
                            )
                    )
            }
    )
    public List<EvaluationResponseDto> getEvaluationsByGroupId(@PathVariable Integer groupId) {
        return evaluationService.findAllByGroupId(groupId);
    }

    @GetMapping("/extended/evaluations/byStudent/{studentId}")
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
                                    array = @ArraySchema(schema = @Schema(implementation = EvaluationResponseExtendedDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<EvaluationResponseExtendedDto>> getAllEvaluationsByStudentExtended(@PathVariable("studentId") int studentId) {
        List<EvaluationResponseExtendedDto> response = evaluationService.getAllEvaluationsByStudentExtended(studentId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else {
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
                                    array = @ArraySchema(schema = @Schema(implementation = EvaluationResponseExtendedDto.class))
                            )
                    )
            }
    )
    @GetMapping("/extended/evaluations/byStudentAndGroup/{studentId}/{groupId}")
    public ResponseEntity<List<EvaluationResponseExtendedDto>> getAllEvaluationsByStudentInGroupExtended(@PathVariable("studentId") int studentId, @PathVariable("groupId") int groupId) {
        List<EvaluationResponseExtendedDto> response = evaluationService.getAllEvaluationsByStudentInGroupExtended(studentId, groupId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else {
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
                                    array = @ArraySchema(schema = @Schema(implementation = EvaluationResponseExtendedDto.class))
                            )
                    )
            }
    )
    @GetMapping("/extended/evaluations/byAssignment/{assignmentId}")
    public ResponseEntity<List<EvaluationResponseExtendedDto>> getAllEvaluationsByAssignmentExtended(@PathVariable("assignmentId") int assignmentId) {
        List<EvaluationResponseExtendedDto> response = evaluationService.getAllEvaluationsByAssignmentExtended(assignmentId);
        if (response.isEmpty()) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        } else {
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
                                    schema = @Schema(implementation = EvaluationResponseExtendedDto.class)
                            )
                    )
            }
    )

    @GetMapping("/extended/evaluation/bySolution/{solutionId}")
    public ResponseEntity<EvaluationResponseExtendedDto> getEvaluationBySolutionExtended(@PathVariable("solutionId") int solutionId) {
        EvaluationResponseExtendedDto response = evaluationService.getEvaluationBySolutionExtended(solutionId);
        if (response != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
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
    @GetMapping("/evaluation/checkForEvaluationToSolution/{solutionId}")
    public ResponseEntity<Boolean> checkForEvaluationToSolution(@PathVariable("solutionId") int solutionId) {
        return new ResponseEntity<>(evaluationService.checkForEvaluationToSolution(solutionId), HttpStatus.OK);
    }

}
