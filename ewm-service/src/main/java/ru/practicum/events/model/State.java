package ru.practicum.events.model;

public enum State {
    PENDING, PUBLISHED, CANCELED;

    public static State stringToState(String stringState) {
        State state;

        try {
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
        } catch (IllegalArgumentException exception) {
            state = State.PENDING;
        }

        return state;
    }

}
