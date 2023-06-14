package ru.practicum.events.dto;

import lombok.Builder;

@Builder
public class EventUserRequest extends EventRequest {
    private StateAction stateAction;

    public enum StateAction {
        SEND_TO_REVIEW,
        CANCEL_REVIEW
    }
}
