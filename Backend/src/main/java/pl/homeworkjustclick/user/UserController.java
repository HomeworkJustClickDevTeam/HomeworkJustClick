package pl.homeworkjustclick.user;

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
import pl.homeworkjustclick.infrastructure.auth.api.AuthenticationResponseDto;
import pl.homeworkjustclick.infrastructure.auth.api.AuthenticationService;
import pl.homeworkjustclick.infrastructure.auth.api.ChangePasswordRequest;

import java.util.List;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "Bearer Authentication")
@RequiredArgsConstructor
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

    private final UserService userService;

    private final AuthenticationService authenticationService;

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

    @GetMapping("/user/{userId}")
    @Operation(
            summary = "Returns user by his id.",
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
    public ResponseEntity<User> getById(@PathVariable("userId") int id) {
        if (userService.getById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
        }
    }

    @PutMapping("/user/{userId}")
    @Operation(
            summary = "Changes user with given id.",
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
            @PathVariable("userId") int id, @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Empty fields will be ignored. Id field is ignored but needed in JSON.") @RequestBody @Valid User updatedUser) {
        if (userService.update(id, updatedUser).equals(Boolean.TRUE)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else if (userService.update(id, updatedUser) == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/user/index/{userId}")
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
    public ResponseEntity<Void> updateIndex(@PathVariable("userId") int id, @RequestBody int index) {
        if (userService.changeIndexById(id, index).equals(Boolean.TRUE)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/user/color/{userId}")
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
    public ResponseEntity<Void> updateColor(@PathVariable("userId") int id, @RequestBody int color) {
        if (color < 0 || color >= 20) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } else if (userService.changeColorById(id, color).equals(Boolean.TRUE)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/getTeachersByGroup/{groupId}")
    @Operation(
            summary = "Returns list of all teachers within a given group.",
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
    public ResponseEntity<List<User>> getTeachersByGroup(@PathVariable("groupId") int groupId) {
        if (userService.getTeachersByGroup(groupId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(userService.getTeachersByGroup(groupId), HttpStatus.OK);
        }
    }

    @GetMapping("/user/getStudentsByGroup/{groupId}")
    @Operation(
            summary = "Returns list of all students within a given group.",
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
    public ResponseEntity<List<User>> getStudentsByGroup(@PathVariable("groupId") int groupId) {
        if (userService.getStudentsByGroup(groupId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(userService.getStudentsByGroup(groupId), HttpStatus.OK);
        }
    }

    @PostMapping("/changePassword")
    @Operation(
            summary = "Allows user to change his password",
            responses = {
                    @ApiResponse(
                            responseCode = "403",
                            description = "Email not verified.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Password incorrect.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponseDto.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User with this email is missing in DB.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponseDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<AuthenticationResponseDto> changePassword(@RequestBody @Valid ChangePasswordRequest request) {
        AuthenticationResponseDto response = authenticationService.changePassword(request);
        return switch (response.getMessage()) {
            case "ok" -> new ResponseEntity<>(response, HttpStatus.OK);
            case "User not found!" -> new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            case "Password incorrect!" -> new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            default -> new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        };
    }

    @PostMapping("/refreshToken/{userId}")
    @Operation(
            summary = "Allows user to refresh his token",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Token is refreshed.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponseDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<AuthenticationResponseDto> refreshToken(@PathVariable int userId) {
        return new ResponseEntity<>(authenticationService.refreshToken(userId), HttpStatus.OK);
    }
}
