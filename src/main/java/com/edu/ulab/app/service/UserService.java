package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.UserDto;
import org.springframework.stereotype.Repository;
/**
 * Interface for CRUD operations for a User.
 */

@Repository
public interface UserService {

    /**
     * Create new UserEntity in database. Generate the id and insert new UserEntity in database.
     * @param userDto – must not be null.
     * @return userDto object with generated id
     */
    UserDto createUser(UserDto userDto);

    /**
     * Update UserEntity for given userDto in database. Update fields of an existing UserEntity in database.
     * @param userDto – must not be null.
     * @return userDto object with updated fields.
     */
    UserDto updateUser(UserDto userDto);

    /**
     * Return userDto object for given id from database.
     * @param id – must not be null.
     * @return userDto for given id
     */
    UserDto getUserById(Long id);

    /**
     * Delete UserEntity for given id from database.
     * @param id – must not be null.
     */
    void deleteUserById(Long id);
}
