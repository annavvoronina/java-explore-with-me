package ru.practicum.events.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventRequestDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.NewEventDto;
import ru.practicum.events.service.EventService;
import ru.practicum.request.dto.EventRequestFullDto;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class EventControllerPrivate {
    private final EventService eventService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/events")
    public EventFullDto createEvent(@Validated @RequestBody NewEventDto newEventDto,
                                    @PathVariable Long userId) {
        return eventService.createEvent(newEventDto, userId);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEventUser(@PathVariable Long userId,
                                        @PathVariable Long eventId,
                                        @Validated @RequestBody EventRequestDto eventDtoRequest) {
        return eventService.updateEventUser(userId, eventId, eventDtoRequest);
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getAllByUserId(@PathVariable Long userId,
                                              @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                              @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        return eventService.getAllByUserId(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getAllByUserIdAndEventId(@PathVariable Long userId,
                                                 @PathVariable Long eventId) {
        return eventService.getAllByUserIdAndEventId(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<EventRequestFullDto> getAllRequests(@PathVariable Long userId,
                                                    @PathVariable Long eventId) {
        return eventService.getAllRequests(userId, eventId);
    }
}
