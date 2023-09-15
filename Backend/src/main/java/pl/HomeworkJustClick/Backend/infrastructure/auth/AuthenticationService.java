package pl.HomeworkJustClick.Backend.infrastructure.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.HomeworkJustClick.Backend.infrastructure.config.JwtService;
import pl.HomeworkJustClick.Backend.infrastructure.enums.Role;
import pl.HomeworkJustClick.Backend.user.User;
import pl.HomeworkJustClick.Backend.user.UserRepository;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponseDto registerUser(RegisterRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (!emailCheck(request.getEmail())){
            return AuthenticationResponseDto.builder().token(null).id(0).role(Role.NONE).message("E-mail not valid!").build();
        }
        if(optionalUser.isPresent()){
            return AuthenticationResponseDto.builder().token(null).id(0).role(Role.NONE).message("E-mail already taken!").build();
        } else {
            var salt = generateRandomSalt();
            var user = User.builder()
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(salt + request.getPassword()))
                    .role(Role.USER)
                    .isVerified(true)
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .salt(salt)
                    .build();
            userRepository.save(user);
            user.setColor(user.getId()%20);
            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponseDto.builder().token(jwtToken).id(user.getId()).name(user.getFirstname()).lastname(user.getLastname()).role(user.getRole()).color(user.getColor()).index(user.getIndex()).message("ok").build();
        }
    }

    public AuthenticationResponseDto registerAdmin(RegisterRequest request) {
        Optional<User> optionalUser = userRepository.findByEmail(request.getEmail());
        if (!emailCheck(request.getEmail())){
            return AuthenticationResponseDto.builder().token(null).id(0).role(Role.NONE).message("E-mail not valid!").build();
        }
        if(optionalUser.isPresent()) {
            return AuthenticationResponseDto.builder().token(null).id(0).role(Role.NONE).message("E-mail already taken!").build();
        } else {
            var salt = generateRandomSalt();
            var user = User.builder()
                    .email(request.getEmail())
                    .password(salt + passwordEncoder.encode(request.getPassword()))
                    .role(Role.ADMIN)
                    .isVerified(true)
                    .firstname(request.getFirstname())
                    .lastname(request.getLastname())
                    .salt(salt)
                    .build();
            userRepository.save(user);
            user.setColor(user.getId()%20);
            userRepository.save(user);
            var jwtToken = jwtService.generateToken(user);
            return AuthenticationResponseDto.builder().token(jwtToken).id(user.getId()).name(user.getFirstname()).lastname(user.getLastname()).role(user.getRole()).color(user.getColor()).index(user.getIndex()).message("ok").build();
        }
    }

    public AuthenticationResponseDto authenticate(AuthenticationRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isEmpty()){
            return AuthenticationResponseDto.builder().token(null).id(0).role(Role.NONE).message("User not found!").build();
        }
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var salt = user.getSalt();
        if (!user.isVerified()) {
            return AuthenticationResponseDto.builder().token(null).id(0).role(Role.NONE).message("E-mail not verified!").build();
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    salt + request.getPassword()
            ));
        } catch (BadCredentialsException e) {
            return AuthenticationResponseDto.builder().token(null).id(0).role(Role.NONE).message("Password incorrect!").build();
        }
        var jwtToken = jwtService.generateToken(user);
        var id = user.getId();
        return AuthenticationResponseDto.builder().token(jwtToken).id(id).role(user.getRole()).message("ok").color(user.getColor()).name(user.getFirstname()).lastname(user.getLastname()).index(user.getIndex()).build();
    }

    public AuthenticationResponseDto changePassword(ChangePasswordRequest request) {
        if(userRepository.findByEmail(request.getEmail()).isEmpty()){
            return AuthenticationResponseDto.builder().token(null).id(0).role(Role.NONE).message("User not found!").build();
        }
        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var salt = user.getSalt();
        if (!user.isVerified()) {
            return AuthenticationResponseDto.builder().token(null).id(0).role(Role.NONE).message("E-mail not verified!").build();
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    salt + request.getPassword()
            ));
        } catch (BadCredentialsException e) {
            return AuthenticationResponseDto.builder().token(null).id(0).role(Role.NONE).message("Password incorrect!").build();
        }
        user.setPassword(salt + passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var id = user.getId();
        return AuthenticationResponseDto.builder().token(jwtToken).id(id).role(user.getRole()).message("ok").color(user.getColor()).name(user.getFirstname()).lastname(user.getLastname()).index(user.getIndex()).build();
    }

    public AuthenticationResponseDto refreshToken(int id) {
        var user = userRepository.findById(id).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDto.builder().token(jwtToken).id(user.getId()).role(user.getRole()).message("ok").color(user.getColor()).name(user.getFirstname()).lastname(user.getLastname()).index(user.getIndex()).build();
    }

    private Boolean emailCheck(String email) {
        String pattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = java.util.regex.Pattern.compile(pattern);
        return p.matcher(email).matches();
    }

    private String generateRandomSalt() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[20];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
}
