package com.edu.ulab.app.web.handler;

import com.edu.ulab.app.exception.BookNotFoundException;
import com.edu.ulab.app.exception.NotFoundException;
import com.edu.ulab.app.exception.UserNotFoundException;
import com.edu.ulab.app.web.response.BaseWebResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.constraints.NotNull;


@Slf4j
@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({NotFoundException.class})
    protected ResponseEntity<BaseWebResponse> handleNotFoundExceptionException(
            @NonNull final NotFoundException exc) {
        log.error(exc.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new BaseWebResponse(createErrorMessage(exc)));
    }

    @ExceptionHandler({UserNotFoundException.class})
    public ResponseEntity<BaseWebResponse> handleUserNotFoundExceptionException(
            @NotNull final UserNotFoundException exc) {
        log.error(exc.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new BaseWebResponse(createErrorMessage(exc)));
    }

    @ExceptionHandler({BookNotFoundException.class})
    public ResponseEntity<BaseWebResponse> handleBookNotFoundExceptionException(
            @NotNull final BookNotFoundException exc) {
        log.error(exc.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new BaseWebResponse(createErrorMessage(exc)));
    }

    private String createErrorMessage(Exception exception) {
        final String message = exception.getMessage();
        log.error(ExceptionHandlerUtils.buildErrorMessage(exception));
        return message;
    }
}
