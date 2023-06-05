package ru.practicum.events.dto;

import lombok.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.events.model.Location;
import ru.practicum.events.model.State;
import ru.practicum.users.dto.UserShortDto;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EventFullDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Long confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Long participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private State state;
    private String title;
    private Long views;
}
