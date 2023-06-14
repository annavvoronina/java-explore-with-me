package ru.practicum.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class IllegalStateException extends RuntimeException {
    public IllegalStateException(final String message) {
        super(message);
    }
}
