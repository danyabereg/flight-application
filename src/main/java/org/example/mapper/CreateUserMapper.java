package org.example.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.dto.CreateUserDto;
import org.example.entity.Gender;
import org.example.entity.Role;
import org.example.entity.User;
import org.example.utils.LocalDateFormatter;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CreateUserMapper implements Mapper<User, CreateUserDto> {
    private static final CreateUserMapper INSTANCE = new CreateUserMapper();

    public static CreateUserMapper getInstance() {
        return INSTANCE;
    }

    @Override
    public User mapFrom(CreateUserDto createUserDto) {
        return User.builder()
                .name(createUserDto.getName())
                .birthday(LocalDateFormatter.format(createUserDto.getBirthday()))
                .email(createUserDto.getEmail())
                .password(createUserDto.getPassword())
                .role(Role.valueOf(createUserDto.getRole()))
                .gender(Gender.valueOf(createUserDto.getGender()))
                .build();
    }
}
