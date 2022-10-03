package com.edu.ulab.app.exception;

import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.entity.Person;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) { super(message);}

    public UserNotFoundException(Person person) {
        super("Not found person: " + person.toString());
    }

    public UserNotFoundException(Long id) {
        super("Not found user with id: " + id);
    }
}
