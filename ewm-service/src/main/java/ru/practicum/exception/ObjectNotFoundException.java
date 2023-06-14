package ru.practicum.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus
public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(final String message) {
        super(message);
    }
}
