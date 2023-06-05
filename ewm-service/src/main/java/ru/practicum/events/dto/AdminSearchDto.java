package ru.practicum.events.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.events.model.State;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminSearchDto {
    private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    private List<Long> users;
    private List<State> states;
    private List<Long> categories;
    @DateTimeFormat(pattern = DATETIME_PATTERN)
    private LocalDateTime rangeStart;
    @DateTimeFormat(pattern = DATETIME_PATTERN)
    private LocalDateTime rangeEnd;
}
