package ru.practicum.exception;

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
import java.time.LocalDateTime;
import java.util.Map;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse notCorrectState(final IllegalStateException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    public ErrorResponse notCorrectState(final BadRequestException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ErrorResponse notCorrectState(final ConflictException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException exception) {
        return new ResponseEntity<>(
                Map.of("error", exception.getMessage()),
                BAD_REQUEST
        );
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        ApiError apiError = ApiError.builder()
                .errors(result.getFieldErrors())
                .status(BAD_REQUEST.getReasonPhrase())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now().withNano(0))
                .reason("Not readable content")
                .build();
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), BAD_REQUEST);
    }

    @ExceptionHandler(ObjectNotFoundException.class)
    public void handleValidationException(ObjectNotFoundException exception,
                                          ServletWebRequest webRequest) throws IOException {
        webRequest.getResponse().sendError(HttpStatus.NOT_FOUND.value(), exception.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public void handleConstraintViolationException(DataIntegrityViolationException exception,
                                                   ServletWebRequest webRequest) throws IOException {
        webRequest.getResponse().sendError(CONFLICT.value(), exception.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public void handleConstraintViolationException(ConstraintViolationException exception,
                                                   ServletWebRequest webRequest) throws IOException {
        webRequest.getResponse().sendError(BAD_REQUEST.value(), exception.getMessage());
    }
}
