package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.exception.BookNotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
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

import static com.edu.ulab.app.web.constant.BookSQLConstant.DELETE_FROM_BOOK_ID;
import static com.edu.ulab.app.web.constant.BookSQLConstant.INSERT_INTO_BOOK_VALUES;
import static com.edu.ulab.app.web.constant.BookSQLConstant.SELECT_FROM_BOOK_ID;
import static com.edu.ulab.app.web.constant.BookSQLConstant.SELECT_FROM_BOOK_USER_ID;
import static com.edu.ulab.app.web.constant.BookSQLConstant.UPDATE_BOOK_ID;

@Slf4j
@Service
public class BookServiceImplTemplate implements BookService {

    private final JdbcTemplate jdbcTemplate;
    private final BookMapper bookMapper;
    private final KeyHolder keyHolder;

    public BookServiceImplTemplate(JdbcTemplate jdbcTemplate, BookMapper bookMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.bookMapper = bookMapper;
        this.keyHolder = new GeneratedKeyHolder();
    }

    @Override
    public BookDto createBook(@NotNull BookDto bookDto) {
        log.debug("Got bookDto create book: {}", bookDto);
        if (verificationBookDto(bookDto)) {
            throw new BookNotFoundException(bookMapper.bookDtoToBook(bookDto));
        }

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(INSERT_INTO_BOOK_VALUES, new String[]{"id"});
                    ps.setString(1, bookDto.getTitle());
                    ps.setString(2, bookDto.getAuthor());
                    ps.setLong(3, bookDto.getPageCount());
                    ps.setLong(4, bookDto.getUserId());
                    return ps;
                }, keyHolder);

        bookDto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return bookDto;
    }

    @Override
    public BookDto updateBook(@NotNull BookDto bookDto) {
        log.debug("Got bookDto update book: {}", bookDto);
        if (verificationBookDto(bookDto)) {
            throw new BookNotFoundException(bookMapper.bookDtoToBook(bookDto));
        }

        jdbcTemplate.update(
                connection -> {
                    PreparedStatement ps = connection.prepareStatement(UPDATE_BOOK_ID, new String[]{"id"});
                    ps.setString(1, bookDto.getTitle());
                    ps.setString(2, bookDto.getAuthor());
                    ps.setLong(3, bookDto.getPageCount());
                    ps.setLong(4, bookDto.getUserId());
                    ps.setLong(5, bookDto.getId());
                    return ps;
                }, keyHolder);

        bookDto.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());

        return bookDto;
    }

    @Override
    public BookDto getBookById(Long id) {
        log.debug("Got id user to get: {}", id);

        List<Book> books = jdbcTemplate
                .query(SELECT_FROM_BOOK_ID, new BeanPropertyRowMapper<>(Book.class), id);
        log.debug("Query list books: {}", books);

        return bookMapper.bookToBookDto(books
                .stream()
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(() -> new BookNotFoundException(id)));
    }

    @Override
    public void deleteBookById(Long id) {
        log.debug("Got id book to delete: {}", id);
        if (getBookById(id) != null) {
            jdbcTemplate.update(DELETE_FROM_BOOK_ID, id);
        } else {
            throw new BookNotFoundException(id);
        }
    }

    public List<Long> getBookByUserId(Long userId) {
        log.debug("Got id user to get: {}", userId);

        List<Book> books = jdbcTemplate
                .query(SELECT_FROM_BOOK_USER_ID, new BeanPropertyRowMapper<>(Book.class), userId);
        log.debug("Get books: {}", books);

        return books.stream()
                .filter(Objects::nonNull)
                .map(Book::getId)
                .peek(book -> log.debug("Book id: {}", book))
                .toList();
    }

    private boolean verificationBookDto(BookDto bookDto) {
        return bookDto.getAuthor() == null
                || bookDto.getTitle() == null
                || bookDto.getPageCount() <= 0
                || bookDto.getUserId() <= 0;
    }
}
