package ru.practicum.events.dto;

import lombok.Builder;

@Builder
public class EventAdminRequest extends EventRequest {
    private StateAction stateAction;

    public enum StateAction {
        PUBLISH_EVENT,
        REJECT_EVENT
    }
}
