package com.example.ninemanmorris_team10.BoardAction;

import com.example.ninemanmorris_team10.Enum.Capabilities;
import com.example.ninemanmorris_team10.Game;
import com.example.ninemanmorris_team10.Player.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.util.Duration;

/**
 * Alert player's invalid move.
 */
public class AlertAction extends BoardAction {
    Button btn;

    /**
     * Constructor for AlertAction.
     * @param popUpWindow JavaFX element that represents a pop-up window
     * @param label JavaFX element that represents the message to show on the pop-up window
     */
    public AlertAction(DialogPane popUpWindow, Label label, Button btn) {
        super(popUpWindow, label, "Alert");
        this.btn = btn;
    }

    /**
     * Show a popup window to alert player's invalid move.
     g  * @param alertMessage Alert message
     *
     * @return True
     */
    @Override
    public boolean execute(String alertMessage, Player currentPlayer) {
        currentPlayer.addCapability(Capabilities.BLOCK_ACTION);
        popUpWindow.setVisible(true);
        label.setText(alertMessage);

        // Countdown timer duration in seconds
        int countdownDuration = 3;

        // Create a timeline for the countdown
        Timeline timeline = new Timeline();

        for (int i = countdownDuration; i > 0; i--) {
            int count = i;
            KeyFrame keyFrame = new KeyFrame(
                    Duration.seconds(countdownDuration - count),
                    event -> btn.setText("OK (" + (count) + "s)" )
            );
            timeline.getKeyFrames().add(keyFrame);
        }

        // Schedule a timeline to hide the pop-up window after 2.3 seconds
        KeyFrame keyFrame = new KeyFrame(
                Duration.seconds(2.3),
                event ->  {
                    popUpWindow.setVisible(false);
                    currentPlayer.removeCapability(Capabilities.BLOCK_ACTION);
                }
        );
        timeline.getKeyFrames().add(keyFrame);
        timeline.play();

        return true;
    }
}
