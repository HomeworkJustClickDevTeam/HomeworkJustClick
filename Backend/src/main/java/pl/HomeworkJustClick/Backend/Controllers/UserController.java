package pl.HomeworkJustClick.Backend.Controllers;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import pl.HomeworkJustClick.Backend.Entities.Group;
import pl.HomeworkJustClick.Backend.Entities.User;
import pl.HomeworkJustClick.Backend.Services.GroupTeacherService;
import pl.HomeworkJustClick.Backend.Services.UserService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "User", description = "User related calls.")
@ApiResponse(
        responseCode = "403",
        description = "Something is wrong with the token.",
        content = @Content()
)
@ApiResponse(
        responseCode = "200",
        description = "OK."
)
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    GroupTeacherService groupTeacherService;

    @GetMapping("/users")
    @Operation(summary = "Returns list of all users in DB.",
                responses =
                    @ApiResponse(
                        responseCode = "200",
                        description = "List returned.",
                        content = @Content(
                                mediaType = "application/json",
                                array = @ArraySchema(schema = @Schema(implementation = User.class)))
                    )
    )
    public List<User> getAll() {
        return userService.getAll();
    }

    @GetMapping("/user/{id}")
    @Operation(
            summary = "Gets user by his id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No user with this id in the DB.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class))
                    )
            }
    )
    public ResponseEntity<User> getById(@PathVariable("id") int id) {
        if(userService.getById(id).isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(userService.getById(id).get(), HttpStatus.OK);
        }
    }

    @PostMapping("/user")
    @Hidden
    public ResponseEntity<Void> add(@RequestBody User user) {
        if(userService.add(user)) {
            return new ResponseEntity<>(HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/user/{id}")
    @Operation(
            summary = "Updates user with given id.",
            description = "Change whole User object for a given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing user with this id in the DB.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            content = @Content(
                                mediaType = "application/json",
                                schema = @Schema(implementation = User.class))
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Something is wrong with JSON user object.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = User.class))
                    )
            }
    )
    public ResponseEntity<Void> update(
            @PathVariable("id") int id,@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Empty fields will be ignored. Id field is ignored but needed in JSON.") @RequestBody User updatedUser){
        if(userService.update(id, updatedUser)){
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else if(userService.update(id, updatedUser) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/user/{id}")
    @Operation(
            summary = "Deletes user with given id.",
            responses = {
                @ApiResponse(
                        responseCode = "404",
                        description = "Missing user with this id.",
                        content = @Content
                )
            }
    )
    public ResponseEntity<Void> delete (@PathVariable("id") int id) {
        if(userService.delete(id)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/user/index/{id}")
    @Operation(
            summary = "Changes index of user with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing user with this id.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> updateIndex(@PathVariable("id") int id, @RequestBody int index){
        if(userService.changeIndexById(id, index)){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/user/color/{id}")
    @Operation(
            summary = "Changes color of user with given id.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "Missing user with this id.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Color value out of range.",
                            content = @Content
                    )
            }
    )
    public ResponseEntity<Void> updateColor(@PathVariable("id") int id, @RequestBody int color){
        if(color < 0 || color >= 20) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        else if(userService.changeColorById(id, color)){
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/getTeachersByGroup/{group_id}")
    @Operation(
            summary = "Gets all teachers within a given group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No teachers in the group with given id.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = User.class)))
                    )
            }
    )
    public ResponseEntity<List<User>> getTeachersByGroup(@PathVariable("group_id") int group_id) {
        if(userService.getTeachersByGroup(group_id).isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(userService.getTeachersByGroup(group_id), HttpStatus.OK);
        }
    }

    @GetMapping("/user/getStudentsByGroup/{group_id}")
    @Operation(
            summary = "Gets all students within a given group.",
            responses = {
                    @ApiResponse(
                            responseCode = "404",
                            description = "No students in the group with given id.",
                            content = @Content
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "List returned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = User.class)))
                    )
            }
    )
    public ResponseEntity<List<User>> getStudentsByGroup(@PathVariable("group_id") int group_id) {
        if(userService.getStudentsByGroup(group_id).isEmpty())
        {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(userService.getTeachersByGroup(group_id), HttpStatus.OK);
        }
    }
}
