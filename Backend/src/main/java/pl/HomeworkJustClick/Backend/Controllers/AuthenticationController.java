package pl.HomeworkJustClick.Backend.Controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.HomeworkJustClick.Backend.Auth.AuthenticationRequest;
import pl.HomeworkJustClick.Backend.Entities.Group;
import pl.HomeworkJustClick.Backend.Responses.AuthenticationResponse;
import pl.HomeworkJustClick.Backend.Services.AuthenticationService;
import pl.HomeworkJustClick.Backend.Auth.RegisterRequest;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication related calls.")
@ApiResponse(
        responseCode = "200",
        description = "OK",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthenticationResponse.class)
        )
)
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(
            summary = "Serves registration purpose to get auth token and add user to DB.",
            responses = {
                    @ApiResponse(
                            responseCode = "400",
                            description = "Email has been taken or is not in correct format.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        AuthenticationResponse response = authenticationService.registerUser(request);
        if (response.getMessage().equals("ok")){
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register/admin")
    @Operation(
            summary = "Serves registration purpose to get authentication token and add user as an admin to DB.",
            responses = {
                    @ApiResponse(
                            responseCode = "400",
                            description = "Email has been taken or is not in correct format.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<AuthenticationResponse> registerAdmin(@RequestBody RegisterRequest request) {
        AuthenticationResponse response = authenticationService.registerAdmin(request);
        if(response.getMessage().equals("ok")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/authenticate")
    @Operation(
            summary = "Serves authentication purpose mainly to get authentication token.",
            responses = {
                    @ApiResponse(
                            responseCode = "403",
                            description = "Email not verified.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponse.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "User with this email is missing in DB.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthenticationResponse.class)
                            )
                    )
            }
    )
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.authenticate(request);
        if(response.getMessage().equals("ok")) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else if (response.getMessage().equals("User not found!")) {
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
        else {
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        }

    }
}
