package pl.homeworkjustclick.infrastructure.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication related calls.")
@ApiResponse(
        responseCode = "200",
        description = "OK",
        content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = AuthenticationResponseDto.class)
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
                                    schema = @Schema(implementation = AuthenticationResponseDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<AuthenticationResponseDto> register(@RequestBody RegisterRequest request) {
        AuthenticationResponseDto response = authenticationService.registerUser(request);
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
                                    schema = @Schema(implementation = AuthenticationResponseDto.class)
                            )
                    )
            }
    )
    public ResponseEntity<AuthenticationResponseDto> registerAdmin(@RequestBody RegisterRequest request) {
        AuthenticationResponseDto response = authenticationService.registerAdmin(request);
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
    public ResponseEntity<AuthenticationResponseDto> authenticate(@RequestBody AuthenticationRequest request) {
        AuthenticationResponseDto response = authenticationService.authenticate(request);
        return switch (response.getMessage()) {
            case "ok" -> new ResponseEntity<>(response, HttpStatus.OK);
            case "User not found!" -> new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            case "Password incorrect!" -> new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            default -> new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        };

    }

    @PostMapping("/checkToken")
    @Operation(
            summary = "Used by other microservice to check if jwt token is valid.",
            responses = {
                    @ApiResponse(
                            responseCode = "403",
                            description = "Jwt token invalid.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "200",
                            description = "Jwt token valid.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Boolean.class)
                            )
                    )
            }
    )
    public Boolean checkToken(@NonNull HttpServletRequest request) {
        return authenticationService.checkToken(request.getHeader("Authorization").substring(7));
    }
}
