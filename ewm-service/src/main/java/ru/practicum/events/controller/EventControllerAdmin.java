package ru.practicum.events.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.AdminSearchDto;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventRequestDto;
import ru.practicum.events.mapper.ParamMapper;
import ru.practicum.events.model.State;
import ru.practicum.events.service.EventService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/admin/events")
public class EventControllerAdmin {
    private final EventService eventService;

    @GetMapping
    public List<EventFullDto> getEventAdmin(@RequestParam(name = "users", required = false) List<Long> userIds,
                                            @RequestParam(name = "states", required = false) List<State> states,
                                            @RequestParam(name = "categories", required = false) List<Long> categories,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                            @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                            @Positive @RequestParam(name = "size", defaultValue = "10") int size) {
        AdminSearchDto param = ParamMapper.toAdminSearch(userIds, states, categories, rangeStart, rangeEnd);
        return eventService.getEventAdmin(param, from, size);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEventAdmin(@PathVariable Long eventId,
                                         @Validated @RequestBody EventRequestDto eventDtoRequest) {
        return eventService.updateEventAdmin(eventId, eventDtoRequest);
    }
}
