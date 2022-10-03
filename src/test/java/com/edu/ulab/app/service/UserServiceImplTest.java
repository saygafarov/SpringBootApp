package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.UserNotFoundException;
import com.edu.ulab.app.mapper.UserMapper;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * Тестирование функционала {@link UserServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing user functionality.")
public class UserServiceImplTest {

    private UserDto userDto;
    private Person person;
    private Person savedPerson;
    private UserDto result;
    @InjectMocks
    UserServiceImpl userService;

    @Mock
    UserRepository userRepository;

    @Mock
    UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userDto = new UserDto();
        userDto.setAge(11);
        userDto.setFullName("test name");
        userDto.setTitle("test title");

        person = new Person();
        person.setFullName("test name");
        person.setAge(11);
        person.setTitle("test title");

        savedPerson = new Person();
        savedPerson.setId(1L);
        savedPerson.setFullName("test name");
        savedPerson.setAge(11);
        savedPerson.setTitle("test title");

        result = new UserDto();
        result.setId(1L);
        result.setAge(11);
        result.setFullName("test name");
        result.setTitle("test title");
    }


    @Test
    @DisplayName("Создание пользователя. Должно пройти успешно.")
    void savePerson_Test() {

        //given

        //when
        when(userMapper.userDtoToPerson(userDto)).thenReturn(person);
        when(userRepository.save(person)).thenReturn(savedPerson);
        when(userMapper.personToUserDto(savedPerson)).thenReturn(result);

        //then
        UserDto userDtoResult = userService.createUser(userDto);
        assertEquals(1L, userDtoResult.getId());
    }


    @Test
    @DisplayName("Обновление пользователя. Должно пройти успешно.")
    void updatePerson_Test() {

        //given
        UserDto updateResult = new UserDto();
        updateResult.setId(1L);
        updateResult.setAge(20);
        updateResult.setFullName("test");
        updateResult.setTitle("test");

        //when
        when(userMapper.userDtoToPerson(userDto)).thenReturn(person);
        when(userRepository.findById(person.getId())).thenReturn(Optional.of(person));
        when(userRepository.save(person)).thenReturn(savedPerson);
        when(userMapper.personToUserDto(savedPerson)).thenReturn(updateResult);

        //then
        UserDto userDtoUpdateResult = userService.updateUser(userDto);
        assertEquals(1L, userDtoUpdateResult.getId());
        assertEquals("test", userDtoUpdateResult.getTitle());
        assertEquals("test", userDtoUpdateResult.getFullName());
        assertEquals(20, userDtoUpdateResult.getAge());
    }

    @Test
    @DisplayName("Получение пользователя. Должно пройти успешно.")
    void getPerson_Test() {

        //given
        Person person = new Person();
        person.setId(1L);
        person.setAge(10);
        person.setFullName("name");
        person.setTitle("title");

        Person savedPerson = new Person();
        savedPerson.setId(1L);
        savedPerson.setAge(10);
        savedPerson.setFullName("name");
        savedPerson.setTitle("title");

        UserDto savedResult = new UserDto();
        savedResult.setId(1L);
        savedResult.setAge(10);
        savedResult.setFullName("name");
        savedResult.setTitle("title");

        //when
        when(userMapper.userDtoToPerson(userDto)).thenReturn(person);
        when(userRepository.findById(1L)).thenReturn(Optional.of(person));
        when(userMapper.personToUserDto(savedPerson)).thenReturn(savedResult);

        //then
        UserDto userDtoById = userService.getUserById(1L);
        assertEquals(1L, userDtoById.getId());
    }

    @Test
    @DisplayName("Удаление пользователя. Должно пройти успешно.")
    void deletePersonal_Test() {

        //given

        //when
        doNothing().when(userRepository).delete(person);
        when(userRepository.findById(1L)).thenReturn(Optional.of(person));

        //then
        userService.deleteUserById(1L);
        UserDto userById = userService.getUserById(1L);
        assertNull(userById);
    }

    @Test()
    @DisplayName("Создание пользователя со значением null.")
    void createPersonByNull_thenExIsThrow() {

        doThrow(UserNotFoundException.class)
                .when(userRepository)
                .save(null);
    }

    @Test()
    @DisplayName("Удаление пользователя со значением null.")
    void deletePersonByNull_thenExIsThrow() {

        doThrow(UserNotFoundException.class)
                .when(userRepository)
                .delete(null);
    }

    @Test()
    @DisplayName("Удаление пользователя с несуществующим id.")
    void deletePersonByNonExistent_thenExIsThrow() {

        doThrow(UserNotFoundException.class)
                .when(userRepository)
                .deleteById(999L);
    }

    @Test()
    @DisplayName("Получение пользователя cо значение null.")
    void getPersonByNull_thenExIsThrow() {

        doThrow(UserNotFoundException.class)
                .when(userRepository)
                .findById(null);
    }

    @Test
    @DisplayName("Создание пользователя со значением null.")
    void createPersonByNull_FailedTest() {

        assertThatThrownBy(() -> userService.createUser(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Обновленипе пользователя cо знчение null")
    void updatePersonByNull_FailedTest() {

        assertThatThrownBy(() -> userService.updateUser(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Получение пользователя c несуществующим id.")
    void getPersonByNonExistentId_FailedTest() {

        assertThatThrownBy(() -> userService.getUserById(100L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    @DisplayName("Удаление пользователя c несуществующим id.")
    void deletePersonByNonExistentId_FailedTest() {

        assertThatThrownBy(() -> userService.deleteUserById(100L))
                .isInstanceOf(UserNotFoundException.class);
    }
}
