package com.example.ninemanmorris_team10.ButtonAction;

import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Allows players to quit the game.
 */
public class QuitGameAction extends ButtonAction{
    /**
     * Constructor for QuitGameAction.
     * @param event UI event
     * @param stage JavaFX element
     */
    public QuitGameAction(Event event, Stage stage) {
        super(event, stage);
    }

    /**
     * Switch scene to main page.
     *
     * @return True
     * @throws IOException
     */
    @Override
    public boolean execute() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/com/example/ninemanmorris_team10/main-page.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        return true;
    }
}
