package com.edu.ulab.app.service.impl;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.exception.BookNotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.service.BookService;
import com.edu.ulab.app.storage.BookStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class BookServiceImplStorage implements BookService {
    private final BookStorage bookStorage;
    private final BookMapper bookMapper;

    @Override
    public BookDto createBook(BookDto bookDto) {
        Book newBook = bookMapper.bookDtoToBook(bookDto);
        log.info("Mapped book: {}", newBook);
        Book savedBook = bookStorage.save(newBook);
        log.info("Saved book: {}", savedBook);
        return bookMapper.bookToBookDto(savedBook);
    }

    @Override
    public BookDto updateBook(BookDto bookDto) {
        Book updatedBook = bookStorage.findById(bookDto.getId())
                .map(book -> bookMapper.bookDtoToBook(bookDto))
                .map(bookStorage::save)
                .orElseThrow(() -> new BookNotFoundException(bookDto.getId()));
        log.info("Updated book: {}", updatedBook);
        return bookMapper.bookToBookDto(updatedBook);
    }

    @Override
    public BookDto getBookById(Long id) {
        Book book = bookStorage.findById(id)
                .orElseThrow(() -> new BookNotFoundException(id));
        log.info("Received book: {}", book);
        return bookMapper.bookToBookDto(book);
    }

    public List<BookDto> getBooksByUserId(Long userId) {
        return bookStorage.findAll()
                .stream()
                .filter(book -> book.getUserId().equals(userId))
                .map(bookMapper::bookToBookDto)
                .toList();
    }

    @Override
    public void deleteBookById(Long id) {
        bookStorage.deleteById(id);
    }
}

