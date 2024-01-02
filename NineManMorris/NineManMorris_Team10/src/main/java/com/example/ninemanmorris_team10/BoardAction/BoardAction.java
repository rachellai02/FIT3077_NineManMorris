package com.example.ninemanmorris_team10.BoardAction;

import com.example.ninemanmorris_team10.Player.Player;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;

/**
 * An abstract BoardAction class.
 * This parent class represents all actions that will be performed by the game board.
 */
public abstract class BoardAction {
    protected String actionId;
    protected DialogPane popUpWindow;
    protected Label label;

    /**
     * Constructor for BoardAction.
     * @param popUpWindow JavaFX element that represents a pop-up window
     * @param label JavaFX element that represents the message to show on the pop-up window
     */
    public BoardAction(DialogPane popUpWindow, Label label, String actionId){
        this.popUpWindow = popUpWindow;
        this.label = label;
        this.actionId = actionId;
    }

    /**
     * Execute a BoardAction.
     * @param text Message to display
     *
     * @return True
     */
    public abstract boolean execute(String text, Player currentPlayer);

    public String getActionId() { return this.actionId;}

}
