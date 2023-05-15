package pl.HomeworkJustClick.Backend.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.Auth.AuthenticationRequest;
import pl.HomeworkJustClick.Backend.Responses.AuthenticationResponse;
import pl.HomeworkJustClick.Backend.Auth.RegisterRequest;
import pl.HomeworkJustClick.Backend.Config.JwtService;
import pl.HomeworkJustClick.Backend.Entities.User;
import pl.HomeworkJustClick.Backend.Enums.Role;
import pl.HomeworkJustClick.Backend.Repositories.UserRepository;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImplement implements AuthenticationService{

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse registerUser(RegisterRequest request){
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (!emailCheck(request.getEmail())){
            return AuthenticationResponse.builder().token(null).id(0).role(Role.NONE).message("E-mail not valid!").build();
        }
        if(optionalUser.isPresent()){
            return AuthenticationResponse.builder().token(null).id(0).role(Role.NONE).message("E-mail already taken!").build();
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
            user.setColor(user.getId()%20);
            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder().token(jwtToken).id(user.getId()).name(user.getFirstname()).lastname(user.getLastname()).role(user.getRole()).color(user.getColor()).index(user.getIndex()).message("ok").build();
        }
    }

    public AuthenticationResponse registerAdmin(RegisterRequest request){
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (!emailCheck(request.getEmail())){
            return AuthenticationResponse.builder().token(null).id(0).role(Role.NONE).message("E-mail not valid!").build();
        }
        if(optionalUser.isPresent()) {
            return AuthenticationResponse.builder().token(null).id(0).role(Role.NONE).message("E-mail already taken!").build();
        } else {
            var user = User.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ADMIN)
                    .isVerified(true)
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .build();
            userRepository.save(user);
            user.setColor(user.getId()%20);
            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponse.builder().token(jwtToken).id(user.getId()).name(user.getFirstname()).lastname(user.getLastname()).role(user.getRole()).color(user.getColor()).index(user.getIndex()).message("ok").build();
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){

        if(userRepository.findByEmail(request.getEmail()).isEmpty()){
            return AuthenticationResponse.builder().token(null).id(0).role(Role.NONE).message("User not found!").build();
        }
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        if (!user.isVerified()) {
            return AuthenticationResponse.builder().token(null).id(0).role(Role.NONE).message("E-mail not verified!").build();
        }
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getEmail(),
                request.getPassword()
        ));
        var jwtToken = jwtService.generateToken(user);
        var id = user.getId();
        return AuthenticationResponse.builder().token(jwtToken).id(id).role(user.getRole()).message("ok").color(user.getColor()).name(user.getFirstname()).lastname(user.getLastname()).index(user.getIndex()).build();
    }

    private Boolean emailCheck(String email) {
        String pattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = java.util.regex.Pattern.compile(pattern);
        return p.matcher(email).matches();
    }
}
