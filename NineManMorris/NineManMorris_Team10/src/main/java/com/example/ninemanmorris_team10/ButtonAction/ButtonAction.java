package com.example.ninemanmorris_team10.ButtonAction;

import javafx.event.Event;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * An abstract ButtonAction class.
 * This parent class represents all available actions that can be done by players when they clicked on a button on the UI.
 */
public abstract class ButtonAction {
    protected Event event;
    protected Stage stage;

    /**
     * Constructor for ButtonAction.
     * @param event UI event
     * @param stage JavaFX element
     */
    public ButtonAction(Event event, Stage stage) {
        this.event = event;
        this.stage = stage;
    }

    /**
     * Execute a ButtonAction.
     *
     * @return True
     * @throws IOException
     */
    public abstract boolean execute() throws IOException;
}
