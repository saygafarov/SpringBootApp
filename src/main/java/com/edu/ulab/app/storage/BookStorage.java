package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.Book;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class BookStorage implements StorageRepository<Book, Long> {

    private final Map<Long, Book> storage = new HashMap<>();

    @Override
    public Book save(Book book) {
        storage.put(book.getId(), book);
        return book;
    }

    @Override
    public Optional<Book> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Book> findAll() {
        return storage.values().stream().toList();
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }
}
