package org.example.validator;

import lombok.NoArgsConstructor;
import org.example.dto.CreateUserDto;
import org.example.entity.Gender;
import org.example.entity.Role;
import org.example.utils.LocalDateFormatter;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class CreateUserValidator implements Validator<CreateUserDto> {
    private final static CreateUserValidator INSTANCE = new CreateUserValidator();

    public ValidationResult isValid(CreateUserDto object) {
        ValidationResult validationResult = new ValidationResult();
        if (!LocalDateFormatter.isValid(object.getBirthday())) {
            validationResult.add(Error.of("invalid.birthday", "Birthday is invalid"));
        }
        if (Gender.find(object.getGender()).isEmpty()) {
            validationResult.add(Error.of("invalid.gender", "Gender is invalid"));
        }
        if (Role.find(object.getRole()).isEmpty()) {
            validationResult.add(Error.of("invalid.role", "Role is invalid"));
        }

        return validationResult;
    }

    public static CreateUserValidator getInstance() {
        return INSTANCE;
    }
}
