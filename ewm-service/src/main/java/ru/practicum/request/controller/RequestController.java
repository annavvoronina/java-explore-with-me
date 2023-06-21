package ru.practicum.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.request.dto.EventRequestFullDto;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResultDto;
import ru.practicum.request.service.RequestService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/users")
public class RequestController {
    private final RequestService requestService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{userId}/requests")
    public EventRequestFullDto createRequest(@PathVariable Long userId,
                                             @RequestParam(name = "eventId") Long eventId) {
        return requestService.createRequest(userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    public List<EventRequestFullDto> getRequest(@PathVariable Long userId) {
        return requestService.getRequest(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public EventRequestFullDto updateCancel(@PathVariable Long userId,
                                            @PathVariable Long requestId) {
        return requestService.updateCancel(userId, requestId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResultDto updateRequestStatus(@PathVariable Long userId,
                                                                 @PathVariable Long eventId,
                                                                 @Validated @RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
        return requestService.updateRequestStatus(userId, eventId, eventRequestStatusUpdateRequest);
    }
}
