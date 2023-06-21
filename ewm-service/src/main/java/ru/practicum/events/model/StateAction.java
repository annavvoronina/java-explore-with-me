package ru.practicum.events.model;

public enum StateAction {
    PUBLISH_EVENT, REJECT_EVENT, SEND_TO_REVIEW, CANCEL_REVIEW;

    public static StateAction stringToStateAction(String stringState) {
        StateAction stateAction;

        try {
            stateAction = StateAction.valueOf(stringState);
        } catch (IllegalArgumentException exception) {
            stateAction = StateAction.SEND_TO_REVIEW;
        }

        return stateAction;
    }
}
