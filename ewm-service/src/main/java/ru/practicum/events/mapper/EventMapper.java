package ru.practicum.events.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.category.mapper.CategoryMapper;
import ru.practicum.events.dto.*;
import ru.practicum.events.model.Event;
import ru.practicum.events.model.Location;
import ru.practicum.events.model.State;
import ru.practicum.users.mapper.UserMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EventMapper {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static Event toEvent(NewEventDto newEventDto) {
        Event event = new Event();
        event.setAnnotation(newEventDto.getAnnotation());
        event.setDescription(newEventDto.getDescription());
        event.setEventDate(newEventDto.getEventDate());
        event.setCreatedOn(LocalDateTime.now().withNano(0));
        event.setPaid(newEventDto.isPaid());
        event.setParticipantLimit(newEventDto.getParticipantLimit());
        event.setRequestModeration(newEventDto.isRequestModeration());
        event.setTitle(newEventDto.getTitle());
        event.setLat(newEventDto.getLocation().getLat());
        event.setLon(newEventDto.getLocation().getLon());
        event.setState(State.PENDING);
        return event;
    }

    public static Event toEvent(Event event, EventRequestDto eventAdminRequest) {
        Optional.ofNullable(eventAdminRequest.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(eventAdminRequest.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(eventAdminRequest.getEventDate()).ifPresent(event::setEventDate);
        Optional.ofNullable(eventAdminRequest.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(eventAdminRequest.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        Optional.ofNullable(eventAdminRequest.getRequestModeration()).ifPresent(event::setRequestModeration);
        Optional.ofNullable(eventAdminRequest.getTitle()).ifPresent(event::setTitle);
        return event;
    }

    public static Event toEventUserUpdate(Event event, EventRequestDto eventUserRequest) {
        Optional.ofNullable(eventUserRequest.getAnnotation()).ifPresent(event::setAnnotation);
        Optional.ofNullable(eventUserRequest.getDescription()).ifPresent(event::setDescription);
        Optional.ofNullable(eventUserRequest.getEventDate()).ifPresent(event::setEventDate);
        Optional.ofNullable(eventUserRequest.getPaid()).ifPresent(event::setPaid);
        Optional.ofNullable(eventUserRequest.getParticipantLimit()).ifPresent(event::setParticipantLimit);
        event.setRequestModeration(eventUserRequest.getRequestModeration());
        Optional.ofNullable(eventUserRequest.getTitle()).ifPresent(event::setTitle);
        return event;
    }

    public static EventFullDto toEventFullDto(Event event) {
        return new EventFullDto(event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests() != null ? event.getConfirmedRequests() : 0,
                event.getCreatedOn().format(DATE_TIME_FORMATTER),
                event.getDescription(),
                event.getEventDate() != null ? event.getEventDate().format(DATE_TIME_FORMATTER) : null,
                UserMapper.toUserShortDto(event.getInitiator()),
                new Location(event.getLat(), event.getLon()),
                event.getPaid(),
                event.getParticipantLimit() != null ? event.getParticipantLimit() : 0,
                event.getPublishedOn() != null ? event.getPublishedOn().format(DATE_TIME_FORMATTER) : null,
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews() != null ? event.getViews() : 0);
    }

    public static EventShortDto toEventShortDto(Event event) {
        return new EventShortDto(event.getId(),
                event.getAnnotation(),
                CategoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getEventDate() != null ? event.getEventDate().format(DATE_TIME_FORMATTER) : null,
                UserMapper.toUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                event.getViews());
    }
}
