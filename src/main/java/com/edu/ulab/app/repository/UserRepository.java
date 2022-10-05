package com.edu.ulab.app.repository;

import com.edu.ulab.app.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.Optional;

/**
 * Interface for CRUD operations on a "Person" repository.
 */
public interface UserRepository extends JpaRepository<Person, Long> {

    /**
     * Return Person object for update person by id from database.
     * @param id - must not be null.
     * @return Optional Person.
     */
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Person p where p.id = :id")
    Optional<Person> findByIdForUpdate(long id);
}
