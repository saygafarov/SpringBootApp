package com.edu.ulab.app.mapper;

import com.edu.ulab.app.dto.BookDto;
import com.edu.ulab.app.entity.Book;
import com.edu.ulab.app.web.request.BookRequest;
import com.edu.ulab.app.web.request.UpdateBookRequest;
import org.mapstruct.Mapper;

/**
 * Interface for Book mapping operations.
 */

@Mapper(componentModel = "spring")
public interface BookMapper {

    /**
     * Map bookRequest to bookDto.
     * @param bookRequest – must not be null.
     * @return BookDto object.
     */
    BookDto bookRequestToBookDto(BookRequest bookRequest);

    /**
     * Map bookDto to bookRequest.
     * @param bookDto – must not be null.
     * @return BookRequest object.
     */
    BookRequest bookDtoToBookRequest(BookDto bookDto);

    /**
     * Map bookDto to book.
     * @param bookDto – must not be null.
     * @return Book object.
     */
    Book bookDtoToBook(BookDto bookDto);

    /**
     * Map book to bookDto.
     * @param book – object of Book. Must not be null.
     * @return BookDto object.
     */
    BookDto bookToBookDto(Book book);

    /**
     * Map updateBookRequest to bookDto.
     * @param updateBookRequest – object of Book. Must not be null.
     * @return BookDto object.
     */
    BookDto updateBookRequestToBookDto(UpdateBookRequest updateBookRequest);

    /**
     * Map bookDto to updateBookRequest.
     * @param bookDto – object of Book. Must not be null.
     * @return UpdateBookRequest object.
     */
    UpdateBookRequest bookDtoToUpdateBookRequest(BookDto bookDto);
}
