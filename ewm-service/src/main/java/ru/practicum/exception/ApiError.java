package ru.practicum.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ApiError {
    private List<FieldError> errors;
    private String message;
    private String reason;
    private String status;
    private LocalDateTime timestamp;
}
