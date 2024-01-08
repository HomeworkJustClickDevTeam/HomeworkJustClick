package pl.homeworkjustclick.assignment;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Assignment", description = "Assignment related calls.")
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
public class AssignmentController {

    private final AssignmentService assignmentService;

    @GetMapping("/assignments")
    @Operation(
            summary = "Returns list of all assignments in DB.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public List<AssignmentResponseDto> getAll() {
        List<AssignmentResponseDto> responseList = assignmentService.getAll();
        responseList.sort(Comparator.comparing(AssignmentResponseDto::getCompletionDatetime).reversed());
        return responseList;
    }

    @GetMapping("/extended/assignments")
    @Operation(
            summary = "Returns list of all assignments with user and group in DB.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseExtendedDto.class))
                            )
                    )
            }
    )
    public List<AssignmentResponseExtendedDto> getAllExtended() {
        List<AssignmentResponseExtendedDto> responseList = assignmentService.getAllExtended();
        responseList.sort(Comparator.comparing(AssignmentResponseExtendedDto::getCompletionDatetime).reversed());
        return responseList;
    }

    @GetMapping("/assignment/{assignmentId}")
    @Operation(
            summary = "Returns assignment by it's id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No assignment with this id in the DB.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AssignmentResponseDto.class))

                    )
            }
    )
    public ResponseEntity<AssignmentResponseDto> getById(@PathVariable("assignmentId") int id) {
        AssignmentResponseDto assignmentResponseDto = assignmentService.getById(id);
        if (assignmentResponseDto != null) {
            return new ResponseEntity<>(assignmentResponseDto, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/extended/assignment/{assignmentId}")
    @Operation(
            summary = "Returns assignment by it's id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No assignment with this id in the DB.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AssignmentResponseDto.class))

                    )
            }
    )
    public ResponseEntity<AssignmentResponseExtendedDto> getByIdExtended(@PathVariable("assignmentId") int id) {
        AssignmentResponseExtendedDto assignmentResponse = assignmentService.getByIdExtended(id);
        if (assignmentResponse != null) {
            return new ResponseEntity<>(assignmentResponse, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/assignments/byGroupId/{groupId}")
    @Operation(
            summary = "Returns list of all assignments in the given group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No assignment with this id in the DB.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseDto>> getAssignmentsByGroupId(@PathVariable("groupId") int id) {
        List<AssignmentResponseDto> responseList = assignmentService.getAssignmentsByGroupId(id);
        if (responseList.isEmpty()) {
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseDto::getCompletionDatetime).reversed());
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/assignments/byGroupId/{groupId}")
    @Operation(
            summary = "Returns list of all assignments in the given group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No assignment with this id in the DB.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseExtendedDto>> getAssignmentsByGroupIdExtended(@PathVariable("groupId") int id) {
        List<AssignmentResponseExtendedDto> responseList = assignmentService.getAssignmentsByGroupIdExtended(id);
        if (responseList.isEmpty()) {
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseExtendedDto::getCompletionDatetime).reversed());
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @PostMapping("/assignment/withUserAndGroup/{userId}/{groupId}")
    @Operation(
            summary = "Creates assignment with user and group already attached to it.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No user or group with this id in the DB.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AssignmentResponseDto.class))

                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The user is not a teacher in the given group",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<AssignmentResponseDto> addWithUserAndGroup(@PathVariable("userId") int userId, @PathVariable("groupId") int groupId, @RequestBody @Valid Assignment assignment) {
        AssignmentResponseDto response = assignmentService.addWithUserAndGroup(assignment, userId, groupId);
        if (response.getId() != 0) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (response.isForbidden()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/assignment/{assignmentId}")
    @Operation(
            summary = "Deletes assignment with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing assignment with this id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> delete(@NonNull HttpServletRequest request, @PathVariable("assignmentId") int id) {
        if (assignmentService.delete(id, request).equals(Boolean.TRUE)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/assignment/{assignmentId}")
    @Operation(
            summary = "Changes assignment with given id.",
            description = "Change whole assignment object for a given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing assignment with this id in the DB.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> update(@PathVariable("assignmentId") int id, @RequestBody @Valid Assignment assignment) {
        if (assignmentService.update(id, assignment).equals(Boolean.TRUE)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/assignment/visibility/{assignmentId}")
    @Operation(
            summary = "Changes visibility of assignment with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing assignment with this id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> updateVisibility(@PathVariable("assignmentId") int id, @RequestBody Boolean visible) {
        if (assignmentService.changeVisibility(id, visible).equals(Boolean.TRUE)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/assignment/title/{assignmentId}")
    @Operation(
            summary = "Changes title of assignment with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing assignment with this id.",
                            content = @Content
                    )
            }
    )

    public ResponseEntity<Void> updateTitle(@PathVariable("assignmentId") int id, @RequestBody String title) {
        if (assignmentService.changeTitleById(id, title).equals(Boolean.TRUE)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/assignment/taskDescription/{assignmentId}")
    @Operation(
            summary = "Changes task description of assignment with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing assignment with this id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> updateTaskDescription(@PathVariable("assignmentId") int id, @RequestBody String taskDescription) {
        if (assignmentService.changeTaskDescriptionById(id, taskDescription).equals(Boolean.TRUE)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/assignment/completionDatetime/{assignmentId}")
    @Operation(
            summary = "Changes completion time of assignment with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing assignment with this id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> updateCompletionDatetime(@PathVariable("assignmentId") int id, @RequestBody OffsetDateTime completionDatetime) {
        if (assignmentService.changeCompletionDatetime(id, completionDatetime).equals(Boolean.TRUE)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/assignment/user/{userId}/{assignmentId}")
    @Operation(
            summary = "Changes user of assignment with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing assignment or user with this id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> updateUser(@PathVariable("userId") int userId, @PathVariable("assignmentId") int assignmentId) {
        if (assignmentService.changeUser(assignmentId, userId).equals(Boolean.TRUE)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/assignment/group/{groupId}/{assignmentId}")
    @Operation(
            summary = "Changes group of assignment with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing assignment or group with this id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> updateGroup(@PathVariable("groupId") int groupId, @PathVariable("assignmentId") int assignmentId) {
        if (assignmentService.changeGroup(assignmentId, groupId).equals(Boolean.TRUE)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/assignment/maxPoints/{assignmentId}")
    @Operation(
            summary = "Changes max points of assignment with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing assignment with this id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> updatePoints(@PathVariable("assignmentId") int id, @RequestBody int points) {
        if (assignmentService.changeMaxPoints(id, points).equals(Boolean.TRUE)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/assignment/autoPenalty/{assignmentId}")
    @Operation(
            summary = "Changes auto penalty of assignment with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing assignment with this id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> updatePenalty(@PathVariable("assignmentId") int id, @RequestBody int autoPenalty) {
        if (assignmentService.changeAutoPenalty(id, autoPenalty).equals(Boolean.TRUE)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/assignments/unchecked/{groupId}")
    @Operation(
            summary = "Returns list of unchecked assignments from group with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No unchecked assignments in the group.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseDto>> getUncheckedAssignmentsInGroup(@PathVariable("groupId") int groupId) {
        List<AssignmentResponseDto> responseList = assignmentService.getUncheckedAssignmentsByGroup(groupId);
        if (responseList.isEmpty()) {
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseDto::getCompletionDatetime).reversed());
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/assignments/allByGroupAndStudent/{groupId}/{studentId}")
    @Operation(
            summary = "Returns list of assignments of a student for a given group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No assignments in the group for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseDto>> getAllAssignmentsByUserAndGroup(@PathVariable("groupId") int groupId, @PathVariable("studentId") int userId) {
        List<AssignmentResponseDto> responseList = assignmentService.getAllAssignmentsByGroupIdAndUserId(groupId, userId);
        if (responseList.isEmpty()) {
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseDto::getCompletionDatetime).reversed());
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/assignments/undoneByGroupAndStudent/{groupId}/{studentId}")
    @Operation(
            summary = "Returns list of undone assignments of a student for a given group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No undone assignments in the group for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseDto>> getUndoneAssignmentsByGroupAndStudent(@PathVariable("groupId") int groupId, @PathVariable("studentId") int studentId) {
        List<AssignmentResponseDto> responseList = assignmentService.getUndoneAssignmentsByGroupIdAndUserId(groupId, studentId);
        if (responseList.isEmpty()) {
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseDto::getCompletionDatetime).reversed());
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/assignments/undoneByStudent/{studentId}")
    @Operation(
            summary = "Returns list of undone assignments of a student.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No undone assignments for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseDto>> getUndoneAssignmentsByStudent(@PathVariable("studentId") int studentId) {
        List<AssignmentResponseDto> responseList = assignmentService.getUndoneAssignmentsByStudent(studentId);
        if (responseList.isEmpty()) {
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseDto::getCompletionDatetime).reversed());
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/assignments/doneByGroupAndStudent/{groupId}/{studentId}")
    @Operation(
            summary = "Returns list of done assignments of a student in the group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No done assignments for the student in the group.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseDto>> getDoneAssignmentsByGroupAndStudent(@PathVariable("groupId") int groupId, @PathVariable("studentId") int studentId) {
        List<AssignmentResponseDto> responseList = assignmentService.getDoneAssignmentsByGroupIdAndUserId(groupId, studentId);
        if (responseList.isEmpty()) {
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseDto::getCompletionDatetime).reversed());
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/assignments/doneByStudent/{studentId}")
    @Operation(
            summary = "Returns list of done assignments of a student.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No done assignments for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseDto>> getDoneAssignmentsByStudent(@PathVariable("studentId") int studentId) {
        List<AssignmentResponseDto> responseList = assignmentService.getDoneAssignmentsByStudent(studentId);
        if (responseList.isEmpty()) {
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseDto::getCompletionDatetime).reversed());
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/assignments/expiredUndoneByGroupAndStudent/{groupId}/{studentId}")
    @Operation(
            summary = "Returns list of undone expired assignments of a student in the group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No undone expired assignments for the student and group.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseDto>> getExpiredUndoneAssignmentsByGroupAndStudent(@PathVariable("groupId") int groupId, @PathVariable("studentId") int studentId) {
        List<AssignmentResponseDto> responseList = assignmentService.getExpiredUndoneAssignmentsByGroupIdAndUserId(groupId, studentId);
        if (responseList.isEmpty()) {
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseDto::getCompletionDatetime).reversed());
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/assignments/expiredUndoneByStudent/{studentId}")
    @Operation(
            summary = "Returns list of undone expired assignments of a student.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No undone expired assignments for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseDto>> getExpiredUndoneAssignmentsByStudent(@PathVariable("studentId") int studentId) {
        List<AssignmentResponseDto> responseList = assignmentService.getExpiredUndoneAssignmentsByStudent(studentId);
        if (responseList.isEmpty()) {
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseDto::getCompletionDatetime).reversed());
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/assignments/nonExpiredUndoneByGroupAndStudent/{groupId}/{studentId}")
    @Operation(
            summary = "Returns list of undone non expired assignments of a student and group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No undone non expired assignments for the student and group.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseDto>> getNonExpiredUndoneAssignmentsByGroupAndStudent(@PathVariable("groupId") int groupId, @PathVariable("studentId") int studentId) {
        List<AssignmentResponseDto> responseList = assignmentService.getNonExpiredUndoneAssignmentsByGroupIdAndUserId(groupId, studentId);
        if (responseList.isEmpty()) {
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseDto::getCompletionDatetime).reversed());
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/assignments/nonExpiredUndoneByStudent/{studentId}")
    @Operation(
            summary = "Returns list of undone non expired assignments of a student.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No undone non expired assignments for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseDto>> getNonExpiredUndoneAssignmentsByStudent(@PathVariable("studentId") int studentId) {
        List<AssignmentResponseDto> responseList = assignmentService.getNonExpiredUndoneAssignmentsByStudent(studentId);
        if (responseList.isEmpty()) {
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseDto::getCompletionDatetime).reversed());
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/assignments/byStudent/{studentId}")
    @Operation(
            summary = "Returns list of all student's assignments.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No assignments for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseDto>> getAllStudentAssignments(@PathVariable("studentId") int id) {
        List<AssignmentResponseDto> responseList = assignmentService.getAllAssignmentsByStudent(id);
        if (responseList.isEmpty()) {
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseDto::getCompletionDatetime).reversed());
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/assignments/unchecked/{groupId}")
    @Operation(
            summary = "Returns list of unchecked assignments from group with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No unchecked assignments in the group.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseExtendedDto>> getUncheckedAssignmentsInGroupExtended(@PathVariable("groupId") int groupId) {
        List<AssignmentResponseExtendedDto> responseList = assignmentService.getUncheckedAssignmentsByGroupExtended(groupId);
        if (responseList.isEmpty()) {
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseExtendedDto::getCompletionDatetime).reversed());
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/assignments/allByGroupAndStudent/{groupId}/{studentId}")
    @Operation(
            summary = "Returns list of assignments of a student for a given group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No assignments in the group for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseExtendedDto>> getAllAssignmentsByUserAndGroupExtended(@PathVariable("groupId") int groupId, @PathVariable("studentId") int userId) {
        List<AssignmentResponseExtendedDto> responseList = assignmentService.getAllAssignmentsByGroupIdAndUserIdExtended(groupId, userId);
        if (responseList.isEmpty()) {
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseExtendedDto::getCompletionDatetime).reversed());
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/assignments/undoneByGroupAndStudent/{groupId}/{studentId}")
    @Operation(
            summary = "Returns list of undone assignments of a student for a given group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No undone assignments in the group for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseExtendedDto>> getUndoneAssignmentsByGroupAndStudentExtended(@PathVariable("groupId") int groupId, @PathVariable("studentId") int studentId) {
        List<AssignmentResponseExtendedDto> responseList = assignmentService.getUndoneAssignmentsByGroupIdAndUserIdExtended(groupId, studentId);
        if (responseList.isEmpty()) {
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseExtendedDto::getCompletionDatetime).reversed());
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/assignments/undoneByStudent/{studentId}")
    @Operation(
            summary = "Returns list of undone assignments of a student.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No undone assignments for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseExtendedDto>> getUndoneAssignmentsByStudentExtended(@PathVariable("studentId") int studentId) {
        List<AssignmentResponseExtendedDto> responseList = assignmentService.getUndoneAssignmentsByStudentExtended(studentId);
        if (responseList.isEmpty()) {
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseExtendedDto::getCompletionDatetime).reversed());
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/assignments/doneByGroupAndStudent/{groupId}/{studentId}")
    @Operation(
            summary = "Returns list of done assignments of a student in the group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No done assignments for the student in the group.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseExtendedDto>> getDoneAssignmentsByGroupAndStudentExtended(@PathVariable("groupId") int groupId, @PathVariable("studentId") int studentId) {
        List<AssignmentResponseExtendedDto> responseList = assignmentService.getDoneAssignmentsByGroupIdAndUserIdExtended(groupId, studentId);
        if (responseList.isEmpty()) {
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseExtendedDto::getCompletionDatetime).reversed());
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/assignments/doneByStudent/{studentId}")
    @Operation(
            summary = "Returns list of done assignments of a student.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No done assignments for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseExtendedDto>> getDoneAssignmentsByStudentExtended(@PathVariable("studentId") int studentId) {
        List<AssignmentResponseExtendedDto> responseList = assignmentService.getDoneAssignmentsByStudentExtended(studentId);
        if (responseList.isEmpty()) {
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseExtendedDto::getCompletionDatetime).reversed());
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/assignments/expiredUndoneByGroupAndStudent/{groupId}/{studentId}")
    @Operation(
            summary = "Returns list of undone expired assignments of a student in the group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No undone expired assignments for the student and group.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseExtendedDto>> getExpiredUndoneAssignmentsByGroupAndStudentExtended(@PathVariable("groupId") int groupId, @PathVariable("studentId") int studentId) {
        List<AssignmentResponseExtendedDto> responseList = assignmentService.getExpiredUndoneAssignmentsByGroupIdAndUserIdExtended(groupId, studentId);
        if (responseList.isEmpty()) {
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseExtendedDto::getCompletionDatetime).reversed());
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/assignments/expiredUndoneByStudent/{studentId}")
    @Operation(
            summary = "Returns list of undone expired assignments of a student.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No undone expired assignments for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseExtendedDto>> getExpiredUndoneAssignmentsByStudentExtended(@PathVariable("studentId") int studentId) {
        List<AssignmentResponseExtendedDto> responseList = assignmentService.getExpiredUndoneAssignmentsByStudentExtended(studentId);
        if (responseList.isEmpty()) {
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseExtendedDto::getCompletionDatetime).reversed());
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/assignments/nonExpiredUndoneByGroupAndStudent/{groupId}/{studentId}")
    @Operation(
            summary = "Returns list of undone non expired assignments of a student and group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No undone non expired assignments for the student and group.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseExtendedDto>> getNonExpiredUndoneAssignmentsByGroupAndStudentExtended(@PathVariable("groupId") int groupId, @PathVariable("studentId") int studentId) {
        List<AssignmentResponseExtendedDto> responseList = assignmentService.getNonExpiredUndoneAssignmentsByGroupIdAndUserIdExtended(groupId, studentId);
        if (responseList.isEmpty()) {
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseExtendedDto::getCompletionDatetime).reversed());
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/assignments/nonExpiredUndoneByStudent/{studentId}")
    @Operation(
            summary = "Returns list of undone non expired assignments of a student.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No undone non expired assignments for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseExtendedDto>> getNonExpiredUndoneAssignmentsByStudentExtended(@PathVariable("studentId") int studentId) {
        List<AssignmentResponseExtendedDto> responseList = assignmentService.getNonExpiredUndoneAssignmentsByStudentExtended(studentId);
        if (responseList.isEmpty()) {
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseExtendedDto::getCompletionDatetime).reversed());
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/extended/assignments/byStudent/{studentId}")
    @Operation(
            summary = "Returns list of all student's assignments.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No assignments for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseExtendedDto>> getAllStudentAssignmentsExtended(@PathVariable("studentId") int id) {
        List<AssignmentResponseExtendedDto> responseList = assignmentService.getAllAssignmentsByStudentExtended(id);
        if (responseList.isEmpty()) {
            return new ResponseEntity<>(responseList, HttpStatus.NOT_FOUND);
        } else {
            responseList.sort(Comparator.comparing(AssignmentResponseExtendedDto::getCompletionDatetime).reversed());
            return new ResponseEntity<>(responseList, HttpStatus.OK);
        }
    }

    @GetMapping("/assignment/checkForSolution/{assignmentId}")
    @Operation(
            summary = "Checks if assignment with given id has any solution attached to it. Returns true if so, false otherwise. False may also indicate that there is no assignment in the DB with given id.",
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
    public ResponseEntity<Boolean> checkForSolutionToAssignment(@PathVariable("assignmentId") int id) {
        return new ResponseEntity<>(assignmentService.checkForSolutionToAssignment(id), HttpStatus.OK);
    }

    @GetMapping("/assignment/checkForFile/{assignmentId}")
    @Operation(
            summary = "Checks if assignment with given id has any file attached to it. Returns true if so, false otherwise. False may also indicate that there is no assignment in the DB with given id.",
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
    public ResponseEntity<Boolean> checkForFileToAssignment(@PathVariable("assignmentId") int assignmentId) {
        return new ResponseEntity<>(assignmentService.checkForFileToAssignment(assignmentId), HttpStatus.OK);
    }

    @GetMapping("/assignments/calendarListByStudent/{studentId}")
    @Operation(
            summary = "Returns list of all student's assignments to calendar.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No assignments for the student.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentResponseDto.class))
                            )
                    )
            }
    )
    public ResponseEntity<List<AssignmentResponseCalendarDto>> getAssignmentsByStudentCalendar(@PathVariable("studentId") int studentId) {
        return new ResponseEntity<>(assignmentService.getAssignmentsByStudentCalendar(studentId), HttpStatus.OK);
    }

    @GetMapping("/assignments/byStudentWithEvaluation/{studentId}")
    @Operation(
            summary = "Returns list of all student's assignments with evaluations.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentWithEvaluationResponseDto.class))
                            )
                    )
            }
    )
    public List<AssignmentWithEvaluationResponseDto> getAssignmentsWithEvaluationsByStudent(@PathVariable("studentId") int studentId) {
        return assignmentService.getAllAssignmentsByStudentWithEvaluations(studentId);
    }

    @GetMapping("/assignments/byStudentAndGroupWithEvaluation/{studentId}/{groupId}")
    @Operation(
            summary = "Returns list of all student's in group assignments with evaluations.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = AssignmentWithEvaluationResponseDto.class))
                            )
                    )
            }
    )
    public List<AssignmentWithEvaluationResponseDto> getAssignmentsWithEvaluationsByStudentAndGroup(@PathVariable("studentId") int studentId, @PathVariable("groupId") int groupId) {
        return assignmentService.getAllAssignmentsByStudentAndGroupWithEvaluations(studentId, groupId);
    }
}
