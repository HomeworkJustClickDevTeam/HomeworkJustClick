package pl.homeworkjustclick.infrastructure.auth.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.homeworkjustclick.infrastructure.auth.JwtService;
import pl.homeworkjustclick.infrastructure.enums.Role;
import pl.homeworkjustclick.infrastructure.exception.EntityNotFoundException;
import pl.homeworkjustclick.user.User;
import pl.homeworkjustclick.user.UserRepository;

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
    private final UserDetailsService userDetailsService;

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
            return AuthenticationResponseDto.builder().token(jwtToken).id(user.getId()).firstname(user.getFirstname()).lastname(user.getLastname()).role(user.getRole()).color(user.getColor()).index(user.getIndex()).message("ok").build();
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
        return AuthenticationResponseDto.builder().token(jwtToken).id(id).role(user.getRole()).message("ok").color(user.getColor()).firstname(user.getFirstname()).lastname(user.getLastname()).index(user.getIndex()).build();
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
        user.setPassword(passwordEncoder.encode(salt + request.getNewPassword()));
        userRepository.save(user);
        var jwtToken = jwtService.generateToken(user);
        var id = user.getId();
        return AuthenticationResponseDto.builder().token(jwtToken).id(id).role(user.getRole()).message("ok").color(user.getColor()).firstname(user.getFirstname()).lastname(user.getLastname()).index(user.getIndex()).build();
    }

    public AuthenticationResponseDto refreshToken(int id) {
        var user = userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found!"));
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponseDto.builder().token(jwtToken).id(user.getId()).role(user.getRole()).message("ok").color(user.getColor()).firstname(user.getFirstname()).lastname(user.getLastname()).index(user.getIndex()).build();
    }

    public Boolean checkToken(String token) {
        try {
            var userEmail = jwtService.extractUserEmail(token);
            var userDetails = userDetailsService.loadUserByUsername(userEmail);
            return jwtService.isTokenValid(token, userDetails);
        } catch (Exception e) {
            return false;
        }
    }

    private Boolean emailCheck(String email) {
        String pattern = "^([a-z0-9_\\.-]+\\@[\\da-z\\.-]+\\.[a-z\\.]{2,6})$";
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
