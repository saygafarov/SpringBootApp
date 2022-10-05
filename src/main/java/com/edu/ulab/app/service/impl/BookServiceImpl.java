package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.exception.BookNotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@Primary
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final BookMapper bookMapper;


    @Override
    public BookDto createBook(@NotNull BookDto bookDto) {
        log.debug("Got bookDto update bookMapper: {}", bookDto);
        Book book = bookMapper.bookDtoToBook(bookDto);
        log.info("Mapped book: {}", book);
        if (verificationBookDto(bookDto)) {
            throw new BookNotFoundException(book);
        }
        Book savedBook = bookRepository.save(book);
        log.info("Saved book: {}", savedBook);

        return bookMapper.bookToBookDto(savedBook);
    }

    @Override
    public BookDto updateBook(@NotNull BookDto bookDto) {
        log.debug("Got bookDto create Book: {}", bookDto);
        if (verificationBookDto(bookDto)) {
            throw new BookNotFoundException(bookDto.getId());
        }
        Book updateBook = bookRepository.findById(bookDto.getId())
                .map(book -> bookMapper.bookDtoToBook(bookDto))
                .map(bookRepository::save)
                .orElseThrow(() -> new BookNotFoundException(bookDto.getId()));
        log.debug("Updated  book: {}", updateBook);

        return bookMapper.bookToBookDto(updateBook);
    }

    @Override
    public BookDto getBookById(Long id) {
        log.debug("Got id get book by id: {}", id);
        Book book = bookRepository
                .findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        log.debug("Get optional book by id: {}", book);

        return bookMapper.bookToBookDto(book);
    }

    @Override
    public void deleteBookById(Long id) {
        log.debug("Got id delete book by id: {}", id);
        Book book = bookRepository
                .findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        log.debug("Got book delete book by id: {}", book);
        bookRepository.deleteById(book.getId());
    }

    public List<BookDto> getBooksByUserId(Long userId) {
        log.info("Received books by userId = {}", userId);
        return bookRepository
                .findAllByPersonId(userId)
                .stream()
                .filter(Objects::nonNull)
                .map(bookMapper::bookToBookDto)
                .toList();
    }

    private boolean verificationBookDto(BookDto bookDto) {
        return bookDto.getTitle() == null
                || bookDto.getAuthor() == null
                || bookDto.getPageCount() <= 0
                || bookDto.getUserId() <= 0;
    }
}
