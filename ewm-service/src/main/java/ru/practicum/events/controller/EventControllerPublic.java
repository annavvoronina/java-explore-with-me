package ru.practicum.events.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.events.dto.EventFullDto;
import ru.practicum.events.dto.EventShortDto;
import ru.practicum.events.dto.UserSearchDto;
import ru.practicum.events.mapper.ParamMapper;
import ru.practicum.events.service.EventService;
import ru.practicum.exception.BadRequestException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/events")
public class EventControllerPublic {
    private final EventService eventService;

    @GetMapping
    public List<EventShortDto> getAllEventsPublic(@RequestParam(name = "text", required = false) String text,
                                                  @RequestParam(name = "categories", required = false) List<Long> categories,
                                                  @RequestParam(name = "paid", required = false) Boolean paid,
                                                  @RequestParam(name = "onlyAvailable", defaultValue = "false") Boolean onlyAvailable,
                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
                                                  @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
                                                  @RequestParam(name = "sort", defaultValue = "EVENT_DATE") String sort,
                                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") int from,
                                                  @Positive @RequestParam(name = "size", defaultValue = "10") int size,
                                                  HttpServletRequest request) {
        UserSearchDto param = ParamMapper.toUserSearch(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort);

        if (param.getRangeStart() != null && param.getRangeEnd() != null
                && param.getRangeEnd().isBefore(param.getRangeStart())) {
            throw new BadRequestException("Дата начала не может быть позже даты окончания");
        }

        return eventService.getAllEventsPublic(param, from, size, request);
    }

    @GetMapping("/{id}")
    public EventFullDto getEventPublicById(@PathVariable Long id,
                                           HttpServletRequest request) {
        return eventService.getEventPublicById(id, request);
    }
}
