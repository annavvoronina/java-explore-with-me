package ru.practicum.events.model;

public enum State {
    PENDING, PUBLISHED, CANCELED;

    public static State stringToState(String stringState) {
        State state;

        try {
            state = State.valueOf(stringState);
        } catch (IllegalArgumentException exception) {
            state = State.PENDING;
        }

        return state;
    }

}
