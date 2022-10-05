package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.UserNotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    @Override
    public UserDto createUser(@NotNull UserDto userDto) {
        log.debug("Got userDto update userMapper: {}", userDto);
        Person user = userMapper.userDtoToPerson(userDto);
        log.info("Mapped user: {}", user);
        if (verificationUserDto(userDto)) {
            throw new UserNotFoundException(user);
        }
        Person savedUser = userRepository.save(user);
        log.info("Saved user: {}", savedUser);

        return userMapper.personToUserDto(savedUser);
    }

    @Override
    public UserDto updateUser(@NotNull UserDto userDto) {
        log.debug("Got userDto update userMapper: {}", userDto);
        Person person = userMapper.userDtoToPerson(userDto);
        log.debug("Mapped personDto: {}", person);
        if (verificationUserDto(userDto)) {
            throw new UserNotFoundException(person);
        }
        Person updatedPerson = userRepository.findById(userDto.getId())
                .map(user -> userMapper.userDtoToPerson(userDto))
                .map(userRepository::save)
                .orElseThrow(() -> new UserNotFoundException(userDto.getId()));
        log.info("Updated user: {}", updatedPerson);

        return userMapper.personToUserDto(updatedPerson);
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
