package com.example.ninemanmorris_team10.Enum;

/**
 * An enum class that stores all alert messages.
 */
public enum Alert {
    PLACE_OWN_TOKEN_CLICK(100, "Invalid move. You are not allowed to move yet"),
    PLACE_OPPONENT_TOKEN_CLICK(101, "Hey, don't touch your opponent's token"),

    MOVE_NON_ADJ_POS(200, "Token can be moved to adjacent positions only."),
    MOVE_NO_TOKEN_CLICK(202, "Please click a token to move."),

    REMOVE_MILL_TOKEN_CLICK(300, "Please remove a token that is not part of a mill"),
    REMOVE_OWN_TOKEN_CLICK(301, "You can't remove your own token."),
    REMOVE_EMPTY_POS(302, "Please remove opponent's token.");

    private final int code;
    private final String alertMessage;

    /** Constructor of Alert class.
     *
     * @param code Alert code (unique for each alert)
     * @param alertMessage Alert message for each alert
     */
    Alert(int code, String alertMessage) {
        this.code = code;
        this.alertMessage = alertMessage;
    }

    /**
     * Method to get alert message to be displayed in alert window.
     *
     * @return Alert message string
     */
    public String getAlertMessage() {
        return alertMessage;

    }
}
