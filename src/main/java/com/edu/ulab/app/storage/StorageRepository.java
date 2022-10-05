package com.edu.ulab.app.storage;

import java.util.List;
import java.util.Optional;

/**
 * Interface for generic CRUD operations on a local storage for a specific type.
 */
public interface StorageRepository<E, I> {
    /**
     * Saves a given entity. Use the returned instance for further operations as the save operation
     * might have changed the entity instance completely.
     * @param entity – must not be null.
     * @return: the saved entity; will never be null.
     */
    E save(E entity);

    /**
     * Retrieves an entity by its id.
     * @param id – must not be null.
     * @return the entity with the given id or Optional#empty() if none found.
     */
    Optional<E> findById(I id);

    /**
     * Returns all instances of the type.
     * @return all entities.
     */
    List<E> findAll();

    /**
     * Deletes the entity with the given id.
     * @param id - must not be null.
     */
    void deleteById(I id);
}
