package ru.practicum.events.model;

public enum State {
    PENDING, PUBLISHED, CANCELED;

    public static State stringToState(String stringState) {
        State state;
        try {
            if (stringState == null) {
                state = State.PENDING;
            } else {
                state = State.valueOf(stringState);
            }
        } catch (Exception e) {
            throw new IllegalStateException("Unknown state: " + stringState);
        }
        return state;
    }
}
