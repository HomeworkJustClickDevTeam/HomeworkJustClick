package pl.HomeworkJustClick.Backend.Services;

import pl.HomeworkJustClick.Backend.Auth.AuthenticationRequest;
import pl.HomeworkJustClick.Backend.Auth.AuthenticationResponse;
import pl.HomeworkJustClick.Backend.Auth.RegisterRequest;

public interface AuthenticationService {

    public AuthenticationResponse registerUser(RegisterRequest request);

    public AuthenticationResponse registerAdmin(RegisterRequest request);

    public AuthenticationResponse authenticate(AuthenticationRequest request);
}
