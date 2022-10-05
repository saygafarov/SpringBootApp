package com.edu.ulab.app.repository;

import com.edu.ulab.app.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

/**
 * Interface for CRUD operations on a "Book" repository.
 */

public interface BookRepository extends JpaRepository<Book, Long> {

    /**
     * Return Book object for update person by id from database.
     * @param id - must not be null.
     * @return Optional Book.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from Book b where b.id = :id")
    Optional<Book> findByIdForUpdate(long id);

    /**
     * Return list of Book object for person id from database.
     * @param id - must not be null.
     * @return list of Book objects for person id.
     */
    @Query(value = "select b from Book b where b.userId = ?1")
    List<Book> findAllByPersonId(Long id);
}
