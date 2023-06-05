package ru.practicum.events.model;

import ru.practicum.exception.IllegalStateException;

public enum StateAction {
    PUBLISH_EVENT, REJECT_EVENT, SEND_TO_REVIEW, CANCEL_REVIEW;

    public static StateAction stringToStateAction(String stringState) {
        StateAction state;
        try {
            state = StateAction.valueOf(stringState);
        } catch (Exception e) {
            throw new IllegalStateException("Unknown StateAction: " + stringState);
        }
        return state;
    }

    public static State stringToState(String stringState) {
        State state = State.PENDING;
        try {
            switch (StateAction.valueOf(stringState)) {
                case PUBLISH_EVENT:
                    state = State.PUBLISHED;
                    break;
                case CANCEL_REVIEW:
                case REJECT_EVENT:
                    state = State.CANCELED;
                    break;
            }

        } catch (Exception e) {
            throw new IllegalStateException("Unknown StateAction: " + stringState);
        }
        return state;
    }
}
