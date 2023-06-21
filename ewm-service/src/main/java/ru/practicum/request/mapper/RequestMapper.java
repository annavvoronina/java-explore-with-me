package ru.practicum.request.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.request.dto.EventRequestFullDto;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.Request;

@UtilityClass
public class RequestMapper {
    public EventRequestFullDto toEventRequestDto(Request eventRequest) {
        return EventRequestFullDto.builder()
                .id(eventRequest.getId())
                .event(eventRequest.getEvent().getId())
                .requester(eventRequest.getRequester().getId())
                .status(eventRequest.getStatus())
                .created(eventRequest.getCreated().withNano(0))
                .build();
    }

    public ParticipationRequestDto toEventRequestDtoPart(Request eventRequest) {
        return ParticipationRequestDto.builder()
                .id(eventRequest.getId())
                .event(eventRequest.getEvent().getId())
                .requester(eventRequest.getRequester().getId())
                .status(String.valueOf(eventRequest.getStatus()))
                .created(eventRequest.getCreated().withNano(0))
                .build();
    }
}
