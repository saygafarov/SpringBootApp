package com.edu.ulab.app.repository;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.vladmihalcea.sql.SQLStatementCountValidator.assertDeleteCount;
import static com.vladmihalcea.sql.SQLStatementCountValidator.assertInsertCount;
import static com.vladmihalcea.sql.SQLStatementCountValidator.assertSelectCount;
import static com.vladmihalcea.sql.SQLStatementCountValidator.assertUpdateCount;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты репозитория {@link UserRepository}.
 */
@SystemJpaTest
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Autowired
    BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
    }

    @DisplayName("Сохранить пользователя. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void insertPerson_thenAssertDmlCount() {

        //Given
        Person person = new Person();
        person.setAge(111);
        person.setTitle("reader");
        person.setFullName("Test Test");

        //When
        Person savedResult = userRepository.save(person);

        //Then
        assertThat(savedResult.getAge()).isEqualTo(111);
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Обновить пользователя. Число select должно равняться 2")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void updatePerson_thenAssertDmlCount() {

        //Given
        Person person = new Person();
        person.setId(1001L);
        person.setAge(20);
        person.setTitle("title");
        person.setFullName("name");

        //When
        Person updateResult = userRepository.save(person);

        //Then
        assertThat(updateResult.getAge()).isEqualTo(20);
        assertThat(updateResult.getTitle()).isEqualTo("title");
        assertThat(updateResult.getFullName()).isEqualTo("name");
        assertThat(updateResult.getBookSet().size()).isEqualTo(2);
        assertSelectCount(2);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Получить пользователя. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getPerson_thenAssertDmlCount() {

        //given

        //When
        Person getPerson = userRepository.findById(1001L).orElseThrow();

        //Then
        assertThat(getPerson.getAge()).isEqualTo(55);
        assertThat(getPerson.getFullName()).isEqualTo("default user");
        assertThat(getPerson.getTitle()).isEqualTo("reader");
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Получить пользователя и его книги. Число select должно равняться 2")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getAllPerson_thenAssertDmlCount() {

        //given

        //When
        Person getPerson = userRepository.findById(1001L).orElseThrow();
        List<Book> books = bookRepository.findAllByPersonId(1001L);

        //Then
        assertThat(getPerson.getAge()).isEqualTo(55);
        assertThat(books.size()).isEqualTo(2);
        assertSelectCount(2);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Удалить пользователя. Число delete должно равняться 3")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deletePerson_thenAssertDmlCount() {

        //Given
        Person person = new Person();
        person.setId(1001L);

        //When
        bookRepository.deleteAll(bookRepository.findAllByPersonId(person.getId()));
        userRepository.delete(person);
        List<Book> books = bookRepository.findAllByPersonId(person.getId());

        //Then
        assertThat(books.size()).isEqualTo(0);
        assertSelectCount(4);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(3);
    }

    @DisplayName("Удалить пользователя. Число delete должно равняться 3")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deletePersonFailTest_thenAssertDmlCount() {

        //Given
        Person person = new Person();
        person.setId(1001L);

        //When
        userRepository.delete(person);

        //Then
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }
}
