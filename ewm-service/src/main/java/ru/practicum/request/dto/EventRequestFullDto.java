package ru.practicum.request.dto;

import lombok.*;
import ru.practicum.request.model.RequestStatus;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class EventRequestFullDto {
    private Long id;
    private Long requester;
    private Long event;
    private RequestStatus status;
    private LocalDateTime created;
}
