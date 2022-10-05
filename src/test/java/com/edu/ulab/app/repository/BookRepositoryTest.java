package com.edu.ulab.app.repository;

import com.edu.ulab.app.config.SystemJpaTest;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.BookNotFoundException;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.exception.UserNotFoundException;
import com.vladmihalcea.sql.SQLStatementCountValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.vladmihalcea.sql.SQLStatementCountValidator.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты репозитория {@link BookRepository}.
 */
@SystemJpaTest
public class BookRepositoryTest {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    UserRepository userRepository;

    @BeforeEach
    void setUp() {
        SQLStatementCountValidator.reset();
    }

    @DisplayName("Сохранить книгу. Число select должно равняться 2")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void createBook_thenAssertDmlCount() {

        //Given
        Person person = new Person();
        person.setAge(111);
        person.setTitle("reader");
        person.setFullName("Test Test");

        Person savedPerson = userRepository.saveAndFlush(person);

        Book book = new Book();
        book.setAuthor("Test Author");
        book.setTitle("test");
        book.setPageCount(1000);
        book.setUserId(savedPerson.getId());

        //When
        Book result = bookRepository.saveAndFlush(book);

        //Then
        assertThat(result.getPageCount()).isEqualTo(1000);
        assertThat(result.getTitle()).isEqualTo("test");
        assertSelectCount(2);
        assertInsertCount(2);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Обновить книгу. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void updateBook_thenAssertDmlCount() {

        //Given
        Book book = new Book();
        book.setId(2002L);
        book.setAuthor("test");
        book.setTitle("test");
        book.setPageCount(1000);

        //When
        Book updateResult = bookRepository.save(book);

        //Then
        assertThat(updateResult.getPageCount()).isEqualTo(1000);
        assertThat(updateResult.getTitle()).isEqualTo("test");
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Обновить книгу у пользователя. Числа select и update должны равняться 2 и 1 соответственно")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void updateBookTwo_thenAssertDmlCount() {
        //Given

        Long userId = 1001L;

        Person savedUser = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        Book updatedBook = new Book();
        updatedBook.setId(2002L);
        updatedBook.setAuthor("Updated Author");
        updatedBook.setTitle("Updated");
        updatedBook.setPageCount(500);
        updatedBook.setUserId(savedUser.getId());

        //When
        Book result = bookRepository.findById(updatedBook.getId())
                .map(book -> updatedBook)
                .map(bookRepository::saveAndFlush)
                .orElseThrow(() -> new BookNotFoundException(updatedBook));

        //Then
        assertThat(result.getPageCount()).isEqualTo(500);
        assertThat(result.getTitle()).isEqualTo("Updated");
        assertThat(result.getAuthor()).isEqualTo("Updated Author");
        assertThat(result.getUserId()).isEqualTo(savedUser.getId());
        assertSelectCount(2);
        assertInsertCount(0);
        assertUpdateCount(1);
        assertDeleteCount(0);
    }

    @DisplayName("Получить книгу по id. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getBookById_thenAssertDmlCount() {

        //Given

        //When
        Book result = bookRepository.findById(2002L).orElseThrow();

        //Then
        assertThat(result.getPageCount()).isEqualTo(5500);
        assertThat(result.getTitle()).isEqualTo("default book");
        assertThat(result.getAuthor()).isEqualTo("author");
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Получить книгу по id. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"})
    void getBookTwo_thenAssertDmlCount() {
        //Given

        Long bookId = 2002L;

        //When

        Book result = bookRepository.findById(bookId)
                .orElseThrow(() -> new NotFoundException("BookEntity not found"));

        //Then
        assertThat(result.getTitle()).isEqualTo("default book");
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Получить все книги. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void getAllBookByPersonId_thenAssertDmlCount() {

        //Given

        //When
        List<Book> books = bookRepository.findAllByPersonId(1001L);

        //Then
        assertThat(books.size()).isEqualTo(2);
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Получить книги по id пользователя. Число select должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"})
    void getBooksByUserIdTwo_thenAssertDmlCount() {
        //Given

        Long userId = 1001L;

        //When
        List<Book> result = bookRepository.findAllByPersonId(userId);

        //Then
        assertThat(result.get(0).getUserId()).isEqualTo(userId);
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(0);
    }

    @DisplayName("Удалить книгу. Число delete должно равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deleteBook_thenAssertDmlCount() {

        //Given
        Book book = bookRepository.findById(2002L).orElseThrow();

        //When
        bookRepository.deleteById(book.getId());
        Book result = bookRepository.findById(book.getId()).orElse(null);
        bookRepository.flush();

        //Then
        assertThat(result).isEqualTo(null);
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(1);
    }

    @DisplayName("Удалить книгу. Числа select и delete должны равняться 1")
    @Test
    @Rollback
    @Sql({"classpath:sql/1_clear_schema.sql",
            "classpath:sql/2_insert_person_data.sql",
            "classpath:sql/3_insert_book_data.sql"
    })
    void deleteBookTwo_thenAssertDmlCount() {
        //Given

        Long bookId = 2002L;

        //When
        bookRepository.deleteById(bookId);
        bookRepository.flush();

        //Then
        assertSelectCount(1);
        assertInsertCount(0);
        assertUpdateCount(0);
        assertDeleteCount(1);
    }
}
