package com.edu.ulab.app.exception;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;

public class BookNotFoundException extends RuntimeException {
    public BookNotFoundException(String message) { super(message);}

    public BookNotFoundException(Book book) {
        super("Not found book: " + book.toString());
    }

    public BookNotFoundException(Long id) {
        super("Not found book with id: " + id);
    }
}
