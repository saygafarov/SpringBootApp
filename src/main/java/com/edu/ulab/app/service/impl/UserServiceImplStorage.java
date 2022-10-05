package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.UserNotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import com.edu.ulab.app.storage.UserStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImplStorage implements UserService {

    private final UserStorage userStorage;

    private final UserMapper userMapper;

    @Override
    public UserDto createUser(UserDto userDto) {
        Person personNew = userMapper.userDtoToPerson(userDto);
        log.info("Mapped user: {}", personNew);
        Person savedPerson = userStorage.save(personNew);
        log.info("Saved user: {}", savedPerson);

        return userMapper.personToUserDto(savedPerson);
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        Person updatedPerson = userStorage.findById(userDto.getId())
                .map(user -> userMapper.userDtoToPerson(userDto))
                .map(userStorage::save)
                .orElseThrow(() -> new UserNotFoundException(userDto.getId()));
        log.info("Updated user: {}", updatedPerson);
        return userMapper.personToUserDto(updatedPerson);
    }

    @Override
    public UserDto getUserById(Long id) {
        Person person = userStorage.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        log.info("Received user: {}", person);
        return userMapper.personToUserDto(person);
    }


    @Override
    public void deleteUserById(Long id) {
        userStorage.deleteById(id);
        log.info("Delete book by id = {}", id);
    }
}
