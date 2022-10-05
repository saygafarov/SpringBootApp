package com.edu.ulab.app.mapper;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.web.request.UpdateUserRequest;
import com.edu.ulab.app.web.request.UserRequest;
import org.mapstruct.Mapper;
/**
 * Interface for Person mapping operations.
 */

@Mapper(componentModel = "spring")
public interface UserMapper {

    /**
     *Map userRequest to userDto.
     * @param userRequest - must not be null.
     * @return UserDto object.
     */
    UserDto userRequestToUserDto(UserRequest userRequest);

    /**
     * Map userDto to userRequest.
     * @param userDto - must not be null.
     * @return UserRequest object.
     */
    UserRequest userDtoToUserRequest(UserDto userDto);

    /**
     * Map userDto to Person.
     * @param userDto - must not be null.
     * @return Person object.
     */
    Person userDtoToPerson(UserDto userDto);

    /**
     * Map person to UserDto.
     * @param person - must not be null.
     * @return UserDto object.
     */
    UserDto personToUserDto(Person person);

    /**
     * Map updateUserRequest to UserDto.
     * @param updateUserRequest - must not be null.
     * @return UserDto object.
     */
    UserDto updateUserRequestToUserDto(UpdateUserRequest updateUserRequest);

    /**
     * Map userDto to updateUserRequest.
     * @param userDto - must not be null.
     * @return UpdateUserRequest.
     */
    UpdateUserRequest userDtoToUpdateUserRequest(UserDto userDto);
}
