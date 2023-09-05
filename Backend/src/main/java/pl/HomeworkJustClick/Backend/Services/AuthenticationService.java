package pl.HomeworkJustClick.Backend.Services;

import pl.HomeworkJustClick.Backend.Auth.AuthenticationRequest;
import pl.HomeworkJustClick.Backend.Auth.ChangePasswordRequest;
import pl.HomeworkJustClick.Backend.Auth.RegisterRequest;
import pl.HomeworkJustClick.Backend.Responses.AuthenticationResponse;

public interface AuthenticationService {

    public AuthenticationResponse registerUser(RegisterRequest request);

    public AuthenticationResponse registerAdmin(RegisterRequest request);

    public AuthenticationResponse changePassword(ChangePasswordRequest request);

    public AuthenticationResponse authenticate(AuthenticationRequest request);

    public AuthenticationResponse refreshToken(String userMail);
}
