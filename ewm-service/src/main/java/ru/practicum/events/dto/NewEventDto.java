package ru.practicum.events.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.events.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewEventDto {
    @Size(min = 20, message = "минимальная длина 20 символов")
    @Size(max = 2000, message = "максимальная длина 2000 символов")
    @NotBlank
    private String annotation;
    @NotNull
    private Long category;
    @Size(min = 20, message = "минимальная длина 20 символов")
    @Size(max = 7000, message = "максимальная длина 2000 символов")
    @NotBlank
    private String description;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss", iso = DateTimeFormat.ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime eventDate;
    private Location location;
    private Boolean paid;
    @PositiveOrZero
    private Long participantLimit;
    private Boolean requestModeration;
    @Size(min = 3, message = "минимальная длина 3 символов")
    @Size(max = 120, message = "максимальная длина 120 символов")
    @NotBlank
    private String title;
}
