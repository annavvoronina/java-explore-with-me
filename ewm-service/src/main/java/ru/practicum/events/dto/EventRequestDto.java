package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.events.model.Location;
import ru.practicum.events.model.StateAction;

import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventRequestDto {
    @Size(min = 20, message = "Слишком короткая аннотация")
    @Size(max = 2000, message = "Слишком длинная аннотация")
    private String annotation;
    private Long category;
    @Size(min = 20, message = "Слишком короткое описание")
    @Size(max = 7000, message = "Слишком длинное описание")
    private String description;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    private Long participantLimit;
    private Boolean requestModeration;
    private StateAction stateAction;
    @Size(min = 3, message = "Слишком короткое название")
    @Size(max = 120, message = "Слишком длинное название")
    private String title;
}
