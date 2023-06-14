package ru.practicum.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class ConflictException extends RuntimeException {
    public ConflictException(final String message) {
        super(message);
    }
}
