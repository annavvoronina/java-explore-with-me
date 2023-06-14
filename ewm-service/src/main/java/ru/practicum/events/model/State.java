package ru.practicum.events.model;

public enum State {
    PENDING, PUBLISHED, CANCELED;

    public static State stringToState(String stringState) {
        State state;

        switch (StateAction.valueOf(stringState)) {
            case PUBLISH_EVENT:
                state = State.PUBLISHED;
                break;
            case CANCEL_REVIEW:
            case REJECT_EVENT:
                state = State.CANCELED;
                break;
            default:
                state = State.PENDING;
        }

        return state;
    }

}
