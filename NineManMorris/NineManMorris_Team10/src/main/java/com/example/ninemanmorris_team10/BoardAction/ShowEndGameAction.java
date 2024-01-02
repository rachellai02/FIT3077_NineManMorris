package com.example.ninemanmorris_team10.BoardAction;

import com.example.ninemanmorris_team10.Enum.Capabilities;
import com.example.ninemanmorris_team10.Player.Player;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;

/**
 * Announce game result.
 */
public class ShowEndGameAction extends BoardAction{

    /**
     * Constructor for ShowEndGameAction.
     * @param popUpWindow JavaFX element that represents a pop-up window
     * @param label JavaFX element that represents the message to show on the pop-up window
     */
    public ShowEndGameAction(DialogPane popUpWindow, Label label) {
        super(popUpWindow, label, "ShowEndGame");
    }

    /**
     * Show a popup window to notify players the game result.
     * @param winner Name of the winner of the game.
     *
     * @return True
     */
    @Override
    public boolean execute(String winner, Player currentPlayer) {
        currentPlayer.addCapability(Capabilities.BLOCK_ACTION);
        popUpWindow.setVisible(true);
        label.setText(winner);
        return true;
    }
}
