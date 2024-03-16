package org.example.service;

import lombok.NoArgsConstructor;
import org.example.dao.UserDao;
import org.example.dto.CreateUserDto;
import org.example.dto.UserDto;
import org.example.entity.User;
import org.example.exception.ValidationException;
import org.example.mapper.CreateUserMapper;
import org.example.mapper.UserMapper;
import org.example.validator.CreateUserValidator;
import org.example.validator.ValidationResult;

import java.util.Optional;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class UserService {
    private static final UserService INSTANCE = new UserService();
    private final CreateUserMapper createUserMapper = CreateUserMapper.getInstance();
    private final UserDao userDao = UserDao.getInstance();
    private final CreateUserValidator createUserValidator = CreateUserValidator.getInstance();
    private final UserMapper userMapper = UserMapper.getInstance();

    public Optional<UserDto> login(String email, String password) {
        return userDao.findByEmailAndPassword(email, password).map(userMapper::mapFrom);
    }

    public Integer create(CreateUserDto createUserDto) {
        ValidationResult validationResult = createUserValidator.isValid(createUserDto);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult.getErrors());
        }
        User user = createUserMapper.mapFrom(createUserDto);
        userDao.save(user);
        return user.getId();
    }

    public static UserService getInstance() {
        return INSTANCE;
    }

}
