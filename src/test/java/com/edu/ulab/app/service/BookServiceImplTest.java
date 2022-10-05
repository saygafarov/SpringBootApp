package com.edu.ulab.app.service;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.entity.Person;
import com.edu.ulab.app.exception.BookNotFoundException;
import com.edu.ulab.app.mapper.BookMapper;
import com.edu.ulab.app.repository.BookRepository;
import com.edu.ulab.app.repository.UserRepository;
import com.edu.ulab.app.service.impl.BookServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

/**
 * Тестирование функционала {@link BookServiceImpl}.
 */
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@DisplayName("Testing book functionality.")
public class BookServiceImplTest {

    private Person person;
    private BookDto bookDto;
    private Book book;
    private Book savedBook;
    private BookDto result;
    @InjectMocks
    BookServiceImpl bookService;

    @Mock
    BookRepository bookRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    BookMapper bookMapper;

    @BeforeEach
    void setUp() {
        person  = new Person();
        person.setId(1L);

        bookDto = new BookDto();
        bookDto.setUserId(1L);
        bookDto.setAuthor("test author");
        bookDto.setTitle("test title");
        bookDto.setPageCount(1000);

        book = new Book();
        book.setPageCount(1000);
        book.setTitle("test title");
        book.setAuthor("test author");
        book.setUserId(person.getId());

        result = new BookDto();
        result.setId(1L);
        result.setUserId(1L);
        result.setAuthor("test author");
        result.setTitle("test title");
        result.setPageCount(1000);

        savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setPageCount(1000);
        savedBook.setTitle("test title");
        savedBook.setAuthor("test author");
        savedBook.setUserId(person.getId());
    }

    @Test
    @DisplayName("Создание книги. Должно пройти успешно.")
    void saveBook_Test() {

        //given

        //when
        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(result);

        //then
        BookDto bookDtoResult = bookService.createBook(bookDto);
        assertEquals(1L, bookDtoResult.getId());
    }

    @Test
    @DisplayName("Обновление книги. Должно пройти успешно")
    void updateBook_Test() {

        //given
        BookDto updateResult = new BookDto();
        updateResult.setId(1L);
        updateResult.setUserId(1L);
        updateResult.setAuthor("Author");
        updateResult.setTitle("Title");
        updateResult.setPageCount(5000);

        //when
        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(book);
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(updateResult);

        //then
        BookDto updateBookDtoResult = bookService.updateBook(bookDto);
        assertEquals(1L, updateBookDtoResult.getId());
        assertEquals("Author", updateBookDtoResult.getAuthor());
        assertEquals("Title", updateBookDtoResult.getTitle());
        assertEquals(5000,updateBookDtoResult.getPageCount());
    }

    @Test
    @DisplayName("Получение книги. Должно пройти успешно.")
    void getBook_Test() {

        //given
        Book book = new Book();
        book.setId(1L);
        book.setUserId(person.getId());
        book.setAuthor("Author");
        book.setTitle("Title");
        book.setPageCount(1000);

        Book savedBook = new Book();
        savedBook.setId(1L);
        savedBook.setUserId(person.getId());
        savedBook.setAuthor("Author");
        savedBook.setTitle("Title");
        savedBook.setPageCount(1000);

        BookDto result = new BookDto();
        result.setId(1L);
        result.setUserId(1L);
        result.setAuthor("Author");
        result.setTitle("Title");
        result.setPageCount(1000);

        //when
        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(book);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(result);

        //then
        BookDto bookById = bookService.getBookById(1L);
        assertEquals(1L, bookById.getId());
    }

    @Test
    @DisplayName("Получение списка книг. Должно пройти успешно.")
    void getAllBooks_Test() {

        //given
        BookDto bookDtoOne = new BookDto();
        bookDtoOne.setUserId(1L);
        bookDtoOne.setAuthor("Author One");
        bookDtoOne.setTitle("Title One");
        bookDtoOne.setPageCount(1000);

        Book bookOne = new Book();
        bookOne.setId(1L);
        bookOne.setUserId(person.getId());
        bookOne.setAuthor("Author One");
        bookOne.setTitle("Title One");
        bookOne.setPageCount(1000);

        Book savedBookOne = new Book();
        savedBookOne.setId(1L);
        savedBookOne.setUserId(person.getId());
        savedBookOne.setAuthor("Author One");
        savedBookOne.setTitle("Title One");
        savedBookOne.setPageCount(1000);

        BookDto resultOne = new BookDto();
        resultOne.setId(1L);
        resultOne.setUserId(1L);
        resultOne.setAuthor("Author One");
        resultOne.setTitle("Title One");
        resultOne.setPageCount(1000);

        BookDto bookDtoTwo = new BookDto();
        bookDtoTwo.setId(2L);
        bookDtoTwo.setAuthor("Author Two");
        bookDtoTwo.setTitle("Title Two");
        bookDtoTwo.setPageCount(2000);

        Book bookTwo = new Book();
        bookTwo.setUserId(person.getId());
        bookTwo.setId(2L);
        bookTwo.setAuthor("Author Two");
        bookTwo.setTitle("Title Two");
        bookTwo.setPageCount(2000);

        Book savedBookTwo = new Book();
        savedBookTwo.setId(2L);
        savedBookTwo.setUserId(person.getId());
        savedBookTwo.setAuthor("Author Two");
        savedBookTwo.setTitle("Title Two");
        savedBookTwo.setPageCount(2000);

        BookDto resultTwo = new BookDto();
        resultTwo.setId(2L);
        resultTwo.setUserId(1L);
        resultTwo.setAuthor("Author Two");
        resultTwo.setTitle("Title Two");
        resultTwo.setPageCount(2000);

        List<Book> books = new ArrayList<>() {{
            add(bookOne);
            add(bookTwo);
        }};

        //when
        when(bookMapper.bookDtoToBook(bookDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(savedBook);
        when(bookMapper.bookToBookDto(savedBook)).thenReturn(result);
        when(bookMapper.bookDtoToBook(bookDtoTwo)).thenReturn(bookTwo);
        when(bookRepository.save(bookTwo)).thenReturn(savedBookTwo);
        when(bookMapper.bookToBookDto(savedBookTwo)).thenReturn(resultTwo);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(savedBook));
        when(bookRepository.findAllByPersonId(1L)).thenReturn(books);

        //then
        List<BookDto> bookByUserId = bookService.getBooksByUserId(1L);
        assertEquals(2, bookByUserId.size());
    }

    @Test
    @DisplayName("Удаление книги. Должно пройти успешно.")
    void deleteBook_Test() {

        //given

        //when
        doNothing().when(bookRepository).delete(book);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        //then
        bookService.deleteBookById(1L);
        BookDto bookById = bookService.getBookById(1L);
        assertNull(bookById);
    }

    @Test()
    @DisplayName("Создание книги со значением null.")
    void createBookByNull_thenExIsThrow() {

        doThrow(BookNotFoundException.class)
                .when(bookRepository)
                .save(null);
    }

    @Test()
    @DisplayName("Получение книги cо значение null.")
    void getBookByNull_thenExIsThrow() {

        doThrow(BookNotFoundException.class)
                .when(bookRepository)
                .findById(null);
    }

    @Test()
    @DisplayName("Удаление книги со значением null.")
    void deleteBookByNull_thenExIsThrow() {

        doThrow(BookNotFoundException.class)
                .when(bookRepository)
                .delete(null);
    }

    @Test()
    @DisplayName("Удаление книги с несуществующим id.")
    void deleteBookByNonExistent_thenExIsThrow() {

        doThrow(BookNotFoundException.class)
                .when(userRepository)
                .deleteById(999L);
    }

    @Test
    @DisplayName("Создание книги со значением null.")
    void createBookByNull_FailedTest() {

        assertThatThrownBy(() -> bookService.createBook(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Обновленипе книги cо знчение null")
    void updateBookByNull_FailedTest() {

        assertThatThrownBy(() -> bookService.updateBook(null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("Получение книги c несуществующим id.")
    void getBookByNonExistentId_FailedTest() {

        assertThatThrownBy(() -> bookService.getBookById(100L))
                .isInstanceOf(BookNotFoundException.class);
    }

    @Test
    @DisplayName("Удаление книги c несуществующим id.")
    void deleteBookByNonExistentId_FailedTest() {

        assertThatThrownBy(() -> bookService.deleteBookById(100L))
                .isInstanceOf(BookNotFoundException.class);
    }
}
