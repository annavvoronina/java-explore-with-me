package ru.practicum.request.service;

import ru.practicum.request.dto.EventRequestFullDto;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResultDto;

import java.util.List;

public interface RequestService {
    EventRequestFullDto createRequest(Long userId, Long eventId);

    List<EventRequestFullDto> getRequest(Long userId);

    EventRequestFullDto updateCancel(Long userId, Long request);

    EventRequestStatusUpdateResultDto updateRequestStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);
}
