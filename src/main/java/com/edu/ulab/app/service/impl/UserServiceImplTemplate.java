package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.UserNotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

import static com.edu.ulab.app.web.constant.UserSQLConstant.DELETE_FROM_PERSON_ID;
import static com.edu.ulab.app.web.constant.UserSQLConstant.INSERT_INTO_PERSON_VALUES;
import static com.edu.ulab.app.web.constant.UserSQLConstant.SELECT_FROM_PERSON_ID;
import static com.edu.ulab.app.web.constant.UserSQLConstant.UPDATE_PERSON_ID;

@Slf4j
@Service
public class UserServiceImplTemplate implements UserService {
    private final JdbcTemplate jdbcTemplate;
    private final UserMapper userMapper;
    private final KeyHolder keyHolder;

    public UserServiceImplTemplate(JdbcTemplate jdbcTemplate, UserMapper userMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.userMapper = userMapper;
        this.keyHolder = new GeneratedKeyHolder();
    }

    @Override
    public UserDto createUser(@NotNull UserDto userDto) {
        log.debug("Got userDto create user: {}", userDto);
        if (verificationUserDto(userDto)) {
            throw new UserNotFoundException(userDto);
        }
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_INTO_PERSON_VALUES, new String[]{"id"});
                    ps.setString(1, userDto.getFullName());
                    ps.setString(2, userDto.getTitle());
                    ps.setLong(3, userDto.getAge());
                    return ps;
                }, keyHolder);

        userDto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return userDto;
    }


    @Override
    public UserDto updateUser(@NotNull UserDto userDto) {
        log.debug("Got userDto update user: {}", userDto);
        if (verificationUserDto(userDto)) {
            throw new UserNotFoundException(userDto);
        }
        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(UPDATE_PERSON_ID, new String[]{"id"});
                    ps.setString(1, userDto.getFullName());
                    ps.setString(2, userDto.getTitle());
                    ps.setLong(3, userDto.getAge());
                    ps.setLong(4, userDto.getId());
                    return ps;
                }, keyHolder);

        userDto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return userDto;
    }

    @Override
    public UserDto getUserById(Long id) {
        log.debug("Got id user to get: {}", id);
        List<Person> people = jdbcTemplate
                .query(SELECT_FROM_PERSON_ID, new BeanPropertyRowMapper<>(Person.class), id);
        log.debug("Query list people: {}", people);

        return userMapper.personToUserDto(people
                .stream()
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(() -> new UserNotFoundException(id)));
    }

    @Override
    public void deleteUserById(Long id) {
        log.debug("Got id user to delete: {}", id);

        if (getUserById(id) != null) {
            jdbcTemplate.update(DELETE_FROM_PERSON_ID, id);
        } else {
            throw new UserNotFoundException(id);
        }
    }

    private boolean verificationUserDto(UserDto userDto) {
        return userDto.getFullName() == null
                || userDto.getTitle() == null
                || userDto.getAge() <= 0;
    }
}
