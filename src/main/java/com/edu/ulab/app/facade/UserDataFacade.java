package com.edu.ulab.app.facade;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.dto.UserDto;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.mapper.UserMapper;

import com.edu.ulab.app.service.impl.BookServiceImpl;
import com.edu.ulab.app.service.impl.BookServiceImplTemplate;
import com.edu.ulab.app.service.impl.UserServiceImpl;
import com.edu.ulab.app.service.impl.UserServiceImplTemplate;
import com.edu.ulab.app.web.request.UpdateUserBookRequest;
import com.edu.ulab.app.web.request.UserBookRequest;
import com.edu.ulab.app.web.response.UserBookResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class UserDataFacade {
    private final UserServiceImplTemplate userService;
    private final BookServiceImplTemplate bookService;
    private final UserMapper userMapper;
    private final BookMapper bookMapper;

    public UserDataFacade(UserServiceImplTemplate userService,
                          BookServiceImplTemplate bookService,
                          UserMapper userMapper,
                          BookMapper bookMapper) {
        this.userService = userService;
        this.bookService = bookService;
        this.userMapper = userMapper;
        this.bookMapper = bookMapper;
    }

    public UserBookResponse createUserWithBooks(UserBookRequest userBookRequest) {
        log.info("Got user book create request: {}", userBookRequest);
        UserDto userDto = userMapper.userRequestToUserDto(userBookRequest.getUserRequest());
        log.info("Mapped user request: {}", userDto);
        UserDto createdUser = userService.createUser(userDto);
        log.info("Created user: {}", createdUser);

        List<Long> bookIdList = userBookRequest.getBookRequests()
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookRequestToBookDto)
                .peek(bookDto -> bookDto.setUserId(createdUser.getId()))
                .peek(mappedBookDto -> log.info("mapped book: {}", mappedBookDto))
                .map(bookService::createBook)
                .peek(createdBook -> log.info("Created book: {}", createdBook))
                .map(BookDto::getId)
                .toList();
        log.info("Collected book ids: {}", bookIdList);

        return UserBookResponse.builder()
                .userId(createdUser.getId())
                .booksIdList(bookIdList)
                .build();
    }

    public UserBookResponse updateUserWithBooks(UpdateUserBookRequest updateUserWithBookRequest) {
        log.debug("Got user book update request: {}", updateUserWithBookRequest);
        UserDto userDto = userMapper.updateUserRequestToUserDto(updateUserWithBookRequest.getUserRequest());
        log.debug("Mapped user request: {}", userDto);
        UserDto userUpdated = userService.updateUser(userDto);
        log.debug("Updated user: {}", userUpdated);

        List<Long> bookUpdated = updateUserWithBookRequest.getBookRequests()
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::updateBookRequestToBookDto)
                .peek(bookDto -> bookDto.setUserId(userUpdated.getId()))
                .peek(mappedBookDto -> log.debug("mapped book: {}", mappedBookDto))
                .map(bookService::updateBook)
                .peek(updateBook -> log.debug("Updated book: {}", updateBook))
                .map(BookDto::getId)
                .toList();
        log.debug("Collected book updated: {}", bookUpdated);

        return UserBookResponse.builder()
                .userId(userUpdated.getId())
                .booksIdList(bookUpdated)
                .build();
    }

    public UserBookResponse getUserWithBooks(Long userId) {
        log.debug("Got user id get user with books: {}", userId);
        UserDto userById = userService.getUserById(userId);
        log.debug("Get user by id: {}", userById);
        List<Long> books = bookService.getBookByUserId(userId);
        log.debug("Get books by user: {}", books);

        return UserBookResponse.builder()
                .userId(userById.getId())
                .booksIdList(books)
                .build();
    }

    public void deleteUserWithBooks(Long userId) {
        log.debug("Got id delete user by id: {}", userId);
        bookService.getBookByUserId(userId).forEach(bookService::deleteBookById);
        userService.deleteUserById(userId);
        log.debug("User was delete.");
    }
}
