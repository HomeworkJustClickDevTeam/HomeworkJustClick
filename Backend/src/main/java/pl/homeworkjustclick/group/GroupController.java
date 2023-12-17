package pl.homeworkjustclick.group;

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
import pl.homeworkjustclick.groupstudent.GroupStudentService;
import pl.homeworkjustclick.groupteacher.GroupTeacher;
import pl.homeworkjustclick.groupteacher.GroupTeacherService;
import pl.homeworkjustclick.user.User;
import pl.homeworkjustclick.user.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Group", description = "Group related calls.")
@ApiResponse(
        responseCode = "403",
        description = "Something is wrong with the token.",
        content = @Content()
)
@ApiResponse(
        responseCode = "200",
        description = "OK."
)

public class GroupController {

    private final GroupService groupService;

    private final GroupTeacherService groupTeacherService;

    private final GroupStudentService groupStudentService;

    private final UserService userService;

    @GetMapping("/groups")
    @Operation(
            summary = "Returns list of all groups in DB.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Group.class))
                            )
                    )
            }
    )

    public List<Group> getAll() {
        return groupService.getAll();
    }

    @GetMapping("/group/{groupId}")
    @Operation(
            summary = "Returns group by it's id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No group with this id in the DB.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Group.class))

                    )
            }
    )
    public ResponseEntity<Group> getById(@PathVariable("groupId") int id) {
        Optional<Group> group = groupService.getById(id);
        return group.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/group")
    @Hidden
    public ResponseEntity<GroupResponseDto> add(@RequestBody Group group) {
        GroupResponseDto response = groupService.add(group);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/group/{groupId}")
    @Operation(
            summary = "Deletes group with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing group with this id.",
                            content = @Content
                    )
            }
    )

    public ResponseEntity<Void> delete(@PathVariable("groupId") int id) {
        if (groupService.delete(id).equals(Boolean.TRUE)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/group/name/{groupId}")
    @Operation(
            summary = "Changes name of the group with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find group with given id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> updateName(@PathVariable("groupId") int id, @RequestBody String name) {
        if (groupService.changeNameById(id, name).equals(Boolean.TRUE)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/group/description/{groupId}")
    @Operation(
            summary = "Changes description of the group with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find description with given id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> updateDescription(@PathVariable("groupId") int id, @RequestBody String description) {
        if (groupService.changeDescriptionById(id, description).equals(Boolean.TRUE)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/group/color/{groupId}")
    @Operation(
            summary = "Updates color of the group with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find group with given id.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Color value out of range.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> updateColor(@PathVariable("groupId") int id, @RequestBody int color) {
        if (color < 0 || color >= 20) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (groupService.changeColorById(id, color).equals(Boolean.TRUE)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/group/archive/{groupId}")
    @Operation(
            summary = "Archives group with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find group with given id.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Group already archived.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> archiveGroup(@PathVariable("groupId") int id) {
        int response = groupService.archiveGroup(id);
        if (response == 0) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else if (response == 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/group/unarchive/{groupId}")
    @Operation(
            summary = "Unarchives group with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find group with given id.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Group already unarchived.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> unarchiveGroup(@PathVariable("groupId") int id) {
        int response = groupService.unarchiveGroup(id);
        if (response == 0) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else if (response == 1) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/group/withTeacher/{groupId}")
    @Operation(
            summary = "Creates group with user as a teacher already associated with it the group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find user with given id.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "Could not create group. The problem might be caused by either auth token or wrongly filled JSON object.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = GroupResponseDto.class))
                    )
            }
    )
    public ResponseEntity<GroupResponseDto> addWithTeacher(@PathVariable("groupId") int id, @RequestBody Group group) {
        GroupResponseDto response = groupService.add(group);
        Optional<User> user = userService.getById(id);
        if (user.isPresent()) {
            GroupTeacher groupTeacher = new GroupTeacher(group, user.get(), "");
            if (groupTeacherService.add(groupTeacher).equals(Boolean.TRUE)) {
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @Operation(
            summary = "Adds teacher with given id to the group with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find user or group with given id.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The user with given id is already a student or a teacher in the given group.",
                            content = @Content
                    )
            }
    )
    @PostMapping("/group/addTeacher/{userId}/{groupId}")
    public ResponseEntity<Void> addTeacherToGroup(@PathVariable("userId") int teacherId, @PathVariable("groupId") int groupId) {
        if (groupTeacherService.addTeacherToGroup(groupId, teacherId).equals(Boolean.TRUE)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else if (groupTeacherService.addTeacherToGroup(groupId, teacherId) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Returns a list of groups in which a user with given id is a teacher.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "The user is not present in any group.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Group.class))
                            )
                    )
            }
    )
    @GetMapping("/groups/byTeacher/{userId}")
    public ResponseEntity<List<Group>> getGroupsByUserIdWhereUserIsTeacher(@PathVariable("userId") int id) {
        if (groupService.getGroupsByTeacher(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(groupService.getGroupsByTeacher(id), HttpStatus.OK);
        }
    }

    @Operation(
            summary = "Adds student with given id to the group with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Could not find user or group with given id.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The user with given id is already a student or a teacher in the given group.",
                            content = @Content
                    )
            }
    )
    @PostMapping("/group/addStudent/{userId}/{groupId}")
    public ResponseEntity<Void> addStudentToGroup(@PathVariable("userId") int studentId, @PathVariable("groupId") int groupId) {
        if (groupStudentService.addStudentToGroup(groupId, studentId).equals(Boolean.TRUE)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else if (groupStudentService.addStudentToGroup(groupId, studentId) == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @Operation(
            summary = "Returns a list of groups in which a user with given id is a student.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "The user is not present in any group.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Group.class))
                            )
                    )
            }
    )
    @GetMapping("/groups/byStudent/{userId}")
    public ResponseEntity<List<Group>> getGroupsByUserIdWhereUserIsStudent(@PathVariable("userId") int id) {
        if (groupService.getGroupsByStudent(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(groupService.getGroupsByStudent(id), HttpStatus.OK);
        }
    }

    @Operation(
            summary = "Returns a list of groups in which a user with given id is a student or a teacher.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "The user is not present in any group.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Group.class))
                            )
                    )
            }
    )
    @GetMapping("/groups/byUser/{userId}")
    public ResponseEntity<List<Group>> getGroupsByUserId(@PathVariable("userId") int id) {
        List<Group> groupListTeacher = groupService.getGroupsByTeacher(id);
        List<Group> groupListStudent = groupService.getGroupsByStudent(id);
        List<Group> groupList = new ArrayList<>(groupListTeacher);
        groupList.addAll(groupListStudent);
        if (groupList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(groupList, HttpStatus.OK);
        }
    }

    @Operation(
            summary = "Deletes a student with given id from group with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "The user is not present in any group.",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("/group/deleteStudent/{userId}/{groupId}")
    public ResponseEntity<Void> deleteStudentFromGroup(@PathVariable("userId") int studentId, @PathVariable("groupId") int groupId) {
        if (groupStudentService.deleteStudentFromGroup(groupId, studentId).equals(Boolean.TRUE)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Deletes a teacher with given id from group with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "The user is not present in any group.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "The user with given id is the only teacher within the group. The group can not exist without a teacher.",
                            content = @Content
                    )
            }
    )
    @DeleteMapping("/group/deleteTeacher/{userId}/{groupId}")
    public ResponseEntity<Void> deleteTeacherFromGroup(@PathVariable("userId") int teacherId, @PathVariable("groupId") int groupId) {
        if (groupTeacherService.deleteTeacherFromGroup(groupId, teacherId).equals(Boolean.TRUE)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else if (groupTeacherService.countTeachersInGroup(groupId) < 2) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @Operation(
            summary = "Checks if user with given id is a teacher within a group in given id. Returns true if so, false otherwise. False may also indicates that there is no user in the DB with given id.",
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
    @GetMapping("/group/teacherCheck/{userId}/{groupId}")
    public ResponseEntity<Boolean> checkForTeacherInGroup(@PathVariable("userId") int teacherId, @PathVariable("groupId") int groupId) {
        return new ResponseEntity<>(groupTeacherService.checkForTeacherInGroup(teacherId, groupId), HttpStatus.OK);
    }

    @Operation(
            summary = "Checks if user with given id is a student within a group in given id. Returns true if so, false otherwise. False may also indicates that there is no user in the DB with given id.",
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
    @GetMapping("/group/studentCheck/{userId}/{groupId}")
    public ResponseEntity<Boolean> checkForStudentInGroup(@PathVariable("userId") int studentId, @PathVariable("groupId") int groupId) {
        return new ResponseEntity<>(groupStudentService.checkForStudentInGroup(studentId, groupId), HttpStatus.OK);
    }

    @Operation(
            summary = "Checks if user with given id is present in a group in given id. Returns true if so, false otherwise. False may also indicates that there is no user in the DB with given id.",
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
    @GetMapping("/group/userCheck/{userId}/{groupId}")
    public ResponseEntity<Boolean> checkForUserInGroup(@PathVariable("userId") int userId, @PathVariable("groupId") int groupId) {
        return new ResponseEntity<>(groupTeacherService.checkForTeacherInGroup(userId, groupId) || groupStudentService.checkForStudentInGroup(userId, groupId), HttpStatus.OK);
    }

    @Operation(
            summary = "Checks if user with given id is a teacher within a group in given id. Returns true if so, false otherwise. False may also indicates that there is no user in the DB with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "User not in group.",
                            content = @Content
                    ),
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
    @GetMapping("/group/userCheckWithRole/{userId}/{groupId}")
    public ResponseEntity<String> checkForUserInGroupWithRole(@PathVariable("userId") int userId, @PathVariable("groupId") int groupId) {
        if (groupTeacherService.checkForTeacherInGroup(userId, groupId).equals(Boolean.TRUE)) {
            return new ResponseEntity<>("Teacher", HttpStatus.OK);
        } else if (groupStudentService.checkForStudentInGroup(userId, groupId).equals(Boolean.TRUE)) {
            return new ResponseEntity<>("Student", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not in group", HttpStatus.NOT_FOUND);
        }
    }

}
