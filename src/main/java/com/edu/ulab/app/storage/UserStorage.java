package com.edu.ulab.app.storage;

import com.edu.ulab.app.entity.Person;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserStorage implements StorageRepository<Person, Long> {

    private final Map<Long, Person> storage = new ConcurrentHashMap<>();

    @Override
    public Person save(Person person) {
        storage.put(person.getId(), person);
        return person;
    }

    @Override
    public Optional<Person> findById(Long id) {
        return Optional.ofNullable(storage.get(id));
    }

    @Override
    public List<Person> findAll() {
        return storage.values().stream().toList();
    }

    @Override
    public void deleteById(Long id) {
        storage.remove(id);
    }
}



