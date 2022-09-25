package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.UserNotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserServiceImpl(UserRepository userRepository,
                           UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public UserDto createUser(@NotNull UserDto userDto) {
        log.debug("Got userDto update userMapper: {}", userDto);
        if (verificationUserDto(userDto)) {
            throw new UserNotFoundException(userDto);
        }
        Person user = userMapper.userDtoToPerson(userDto);
        log.info("Mapped user: {}", user);
        Person savedUser = userRepository.save(user);
        log.info("Saved user: {}", savedUser);

        return userMapper.personToUserDto(savedUser);
    }

    @Override
    public UserDto updateUser(@NotNull UserDto userDto) {
        log.debug("Got userDto update userMapper: {}", userDto);
        if (verificationUserDto(userDto)) {
            throw new UserNotFoundException(userDto);
        }
        Person person = userMapper.userDtoToPerson(userDto);
        log.debug("Mapped personDto: {}", person);
        Person checkedPerson = userRepository
                .findById(person.getId())
                .orElseThrow(() -> new UserNotFoundException(person));
        log.debug("Update personUpdated: {}", checkedPerson);
        Person personUpdated = userRepository.save(person);
        log.debug("Update personUpdated: {}", personUpdated);

        return userMapper.personToUserDto(personUpdated);
    }

    @Override
    public UserDto getUserById(Long id) {
        log.debug("Got id get user by id: {}", id);
        Person person = userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        log.debug("Get person by id: {}", person);

        return userMapper.personToUserDto(person);
    }

    @Override
    public void deleteUserById(Long id) {
        log.debug("Got id delete user by id: {}", id);
        Person person = userRepository
                .findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        log.debug("Got person delete person by id: {}", person);
        userRepository.deleteById(person.getId());
    }

    private boolean verificationUserDto(UserDto userDto) {
        return userDto.getFullName() == null
                || userDto.getTitle() == null
                || userDto.getAge() <= 0;
    }
}
