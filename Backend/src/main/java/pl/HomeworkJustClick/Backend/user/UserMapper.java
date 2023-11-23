package pl.HomeworkJustClick.Backend.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapper {
    public UserResponseDto map(User user) {
        return UserResponseDto.builder()
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .index(user.getIndex())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .color(user.getColor())
                .username(user.getUsername())
                .verified(user.isVerified())
                .build();
    }

    public UserSimpleResponseDto map2SimpleResponseDto(User user) {
        return UserSimpleResponseDto.builder()
                .id(user.getId())
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .index(user.getIndex())
                .build();
    }
}
