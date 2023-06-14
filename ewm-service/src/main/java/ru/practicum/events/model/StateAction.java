package ru.practicum.events.model;

public enum StateAction {
    PUBLISH_EVENT, REJECT_EVENT, SEND_TO_REVIEW, CANCEL_REVIEW;

    public static StateAction stringToStateAction(String stringState) {
        StateAction state;
        try {
            state = StateAction.valueOf(stringState);
        } catch (Exception e) {
            state = SEND_TO_REVIEW;
        }

        return state;
    }
}
