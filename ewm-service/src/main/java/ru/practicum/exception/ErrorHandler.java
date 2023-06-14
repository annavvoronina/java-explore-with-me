package ru.practicum.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.ServletWebRequest;

import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {
    StringWriter stringWriter;

    ErrorHandler() {
        stringWriter = new StringWriter();
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse notCorrectState(final java.lang.IllegalStateException exception) {
        log.debug("Получен статус 400 Bad Request");
        log.debug(exception.getMessage());
        log.debug(stringWriter.toString());

        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse notCorrectState(final BadRequestException exception) {
        log.debug("Получен статус 400 Bad Request");
        log.debug(exception.getMessage());
        log.debug(stringWriter.toString());

        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ErrorResponse notCorrectState(final ConflictException exception) {
        log.debug("Получен статус 409 Conflict");
        log.debug(exception.getMessage());
        log.debug(stringWriter.toString());

        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException exception) {
        log.debug("Получен статус 400 Bad Request");
        log.debug(exception.getMessage());
        log.debug(stringWriter.toString());

        return new ResponseEntity<>(
                Map.of("error", exception.getMessage()),
                BAD_REQUEST
        );
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.debug("Получен статус 400 Bad Request");
        log.debug(exception.getMessage());
        log.debug(stringWriter.toString());

        BindingResult result = exception.getBindingResult();
        ApiError apiError = ApiError.builder()
                .errors(result.getFieldErrors())
                .status(BAD_REQUEST.getReasonPhrase())
                .message(exception.getMessage())
                .timestamp(LocalDateTime.now().withNano(0))
                .reason("Not readable content")
                .build();
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), BAD_REQUEST);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public void handleValidationException(ObjectNotFoundException exception,
                                          ServletWebRequest webRequest) throws IOException {
        log.debug("Получен статус 404 Not Found");
        log.debug(exception.getMessage());
        log.debug(stringWriter.toString());

        webRequest.getResponse().sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public void handleConstraintViolationException(DataIntegrityViolationException exception,
                                                   ServletWebRequest webRequest) throws IOException {
        log.debug("Получен статус 409 Conflict");
        log.debug(exception.getMessage());
        log.debug(stringWriter.toString());

        webRequest.getResponse().sendError(CONFLICT.value(), exception.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolationException(ConstraintViolationException exception,
                                                   ServletWebRequest webRequest) throws IOException {
        log.debug("Получен статус 400 Bad Request");
        log.debug(exception.getMessage());
        log.debug(stringWriter.toString());

        webRequest.getResponse().sendError(BAD_REQUEST.value(), exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse notCorrectState(final Throwable exception) {
        log.debug("Получен статус 500 Internal Server Error");
        log.debug(exception.getMessage());
        log.debug(stringWriter.toString());

        return new ErrorResponse(exception.getMessage());
    }
}
