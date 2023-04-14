package pl.HomeworkJustClick.Backend.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Auth.AuthenticationRequest;
import pl.HomeworkJustClick.Backend.Auth.AuthenticationResponse;
import pl.HomeworkJustClick.Backend.Auth.RegisterRequest;
import pl.HomeworkJustClick.Backend.Config.JwtService;
import pl.HomeworkJustClick.Backend.Entities.User;
import pl.HomeworkJustClick.Backend.Enums.Role;
import pl.HomeworkJustClick.Backend.Repositories.UserRepository;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImplement implements AuthenticationService{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse registerUser(RegisterRequest request){
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if(optionalUser.isPresent()){
            return new AuthenticationResponse();
        } else {
            var user = User.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.USER)
                    .isVerified(true)
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .build();
            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder().token(jwtToken).id(user.getId()).role(user.getRole()).message("ok").build();
        }
    }

    public AuthenticationResponse registerAdmin(RegisterRequest request){
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if(optionalUser.isPresent()){
            return new AuthenticationResponse();
        } else {
            var user = User.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ADMIN)
                    .isVerified(true)
                    .build();
            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder().token(jwtToken).id(user.getId()).role(user.getRole()).build();
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));
        if(userRepository.findByEmail(request.getEmail()).isEmpty()){
            return AuthenticationResponse.builder().token(null).id(0).role(Role.NONE).message("User not found!").build();
        }
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        if (!user.isVerified()) {
            return AuthenticationResponse.builder().token(null).id(0).role(Role.NONE).message("E-mail not verified!").build();
        }
        if(user.getPassword().equals(request.getPassword())) {
            return AuthenticationResponse.builder().token(null).id(0).role(Role.NONE).message("Password incorrect!").build();
        }
        var jwtToken = jwtService.generateToken(user);
        var id = user.getId();
        return AuthenticationResponse.builder().token(jwtToken).id(id).role(user.getRole()).message("ok").build();
    }
}
