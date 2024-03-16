package org.example.dto;

import lombok.*;
import org.example.entity.Gender;
import org.example.entity.Role;

import java.time.LocalDate;

@Value
@Builder
public class UserDto {
    Integer id;
    String name;
    LocalDate birthday;
    String email;
    String password;
    Role role;
    Gender gender;
}
