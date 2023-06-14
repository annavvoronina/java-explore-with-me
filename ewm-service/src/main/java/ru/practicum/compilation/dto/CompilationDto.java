package ru.practicum.compilation.dto;

import lombok.*;
import ru.practicum.events.dto.EventShortDto;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class CompilationDto {
    private Long id;
    private List<EventShortDto> events;
    private Boolean pinned;
    private String title;
}
