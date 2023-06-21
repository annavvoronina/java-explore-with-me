package ru.practicum.events.service;

import ru.practicum.events.dto.*;
import ru.practicum.request.dto.EventRequestFullDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface EventService {
    EventFullDto createEvent(NewEventDto newEventDto, Long userId);

    EventFullDto updateEventUser(Long userId, Long eventId, EventRequestDto eventDtoUser);

    List<EventShortDto> getAllByUserId(Long userId, int from, int size);

    EventFullDto getAllByUserIdAndEventId(Long userId, Long eventId);

    List<EventRequestFullDto> getAllRequests(Long userId, Long eventId);

    List<EventFullDto> getEventAdmin(AdminSearchDto adminSearch, int from, int size);

    EventFullDto updateEventAdmin(Long eventId, EventRequestDto eventDtoRequest);

    List<EventShortDto> getAllEventsPublic(UserSearchDto userSearch, int from, int size, HttpServletRequest request);

    EventFullDto getEventPublicById(Long id, HttpServletRequest request);
}
