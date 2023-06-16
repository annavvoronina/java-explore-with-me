package ru.practicum.events.model;

public enum StateAction {
    PUBLISH_EVENT, REJECT_EVENT, SEND_TO_REVIEW, CANCEL_REVIEW;

    public static StateAction stringToStateAction(String stringState) {
        return StateAction.valueOf(stringState);
    }
}
