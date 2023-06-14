package ru.practicum.events.model;

public enum StateAction {
    PUBLISH_EVENT, REJECT_EVENT, SEND_TO_REVIEW, CANCEL_REVIEW;

    public static StateAction stringToStateAction(String stringState) {
        StateAction state;

        switch (StateAction.valueOf(stringState)) {
            case PUBLISH_EVENT:
                state = PUBLISH_EVENT;
                break;
            case CANCEL_REVIEW:
                state = CANCEL_REVIEW;
                break;
            case REJECT_EVENT:
                state = REJECT_EVENT;
                break;
            default:
                state = SEND_TO_REVIEW;
        }

        return state;
    }
}
