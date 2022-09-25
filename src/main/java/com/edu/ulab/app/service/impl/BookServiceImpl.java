package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.exception.BookNotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;

    private final BookMapper bookMapper;

    public BookServiceImpl(BookRepository bookRepository,
                           BookMapper bookMapper) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
    }

    @Override
    public BookDto createBook(@NotNull BookDto bookDto) {
        log.debug("Got bookDto update bookMapper: {}", bookDto);
        if (verificationBookDto(bookDto)) {
            throw new BookNotFoundException(bookDto);
        }
        Book book = bookMapper.bookDtoToBook(bookDto);
        log.info("Mapped book: {}", book);
        Book savedBook = bookRepository.save(book);
        log.info("Saved book: {}", savedBook);

        return bookMapper.bookToBookDto(savedBook);
    }

    @Override
    public BookDto updateBook(@NotNull BookDto bookDto) {
        log.debug("Got bookDto create Book: {}", bookDto);
        if (verificationBookDto(bookDto)) {
            throw new BookNotFoundException(bookDto);
        }
        Book book = bookMapper.bookDtoToBook(bookDto);
        log.debug("Mapped book: {}", book);
        Book checkedBook = bookRepository
                .findById(book.getId())
                .orElseThrow(() -> new BookNotFoundException(book));
        log.debug("Updated  book: {}", checkedBook);
        Book updatedBook = bookRepository.save(checkedBook);
        log.debug("Updated  book: {}", updatedBook);

        return bookMapper.bookToBookDto(updatedBook);
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

    public List<Long> getBookByUserId(Long userId) {
        return bookRepository
                .findByUserId(userId)
                .stream()
                .filter(Objects::nonNull)
                .map(Book::getId)
                .toList();
    }

    private boolean verificationBookDto(BookDto bookDto) {
        return bookDto.getTitle() == null
                || bookDto.getAuthor() == null
                || bookDto.getPageCount() <= 0
                || bookDto.getUserId() <= 0;
    }
}
