package com.example.ninemanmorris_team10.Controller;

import com.example.ninemanmorris_team10.GameApplication;
import com.example.ninemanmorris_team10.GameState;
import com.example.ninemanmorris_team10.Parser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Controls the main page of game.
 */
public class MainPageController {

    private boolean playWithComputer = false;
    private ArrayList<GameState> history = new ArrayList<>();

    private boolean isReloaded = false;
    @FXML
    private Stage stage;

    @FXML
    private DialogPane gameModePopup;

    @FXML
    private DialogPane filePopup;

    @FXML
    void chooseGameMode(){
        gameModePopup.setVisible(true);
    }

    @FXML
    void quitSelectGameMode(MouseEvent event) {
        gameModePopup.setVisible(false);
    }

    @FXML
    void quitFileUpload(MouseEvent event) {
        filePopup.setVisible(false);
    }

    @FXML
    void computerMode(ActionEvent event) throws IOException {
        startNewGame(event, true);
    }

    @FXML
    void humanMode(ActionEvent event) throws IOException {
        startNewGame(event, false);
    }



    @FXML
    void chooseFile(ActionEvent event) throws IOException {

        // Create a file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file to restore");

        // Set the file extension filter to only allow text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show the file saver dialog and get the selected file
        File selectedFile = fileChooser.showOpenDialog(stage);

        // Read the text file
        if (selectedFile != null) {
            Parser parser = new Parser(new File(selectedFile.getAbsolutePath()));
            this.isReloaded = parser.parse();
            if(isReloaded){
                this.history = parser.getHistory();
                startNewGame(event, parser.getAgainstComputer());
            }
            else{
                filePopup.setVisible(true);
            }
        }
    }

    /**
     * Switch to game board interface to start game.
     */
    public void startNewGame(ActionEvent event, boolean computerGameMode) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("/com/example/ninemanmorris_team10/game-board.fxml"));
        GameController gc = new GameController();
        gc.setGameMode(computerGameMode);
        gc.setHistory(this.history);
        gc.setReloaded(this.isReloaded);
        this.isReloaded = false; // Important Reset
        fxmlLoader.setControllerFactory(c -> gc);
        Scene scene = new Scene(fxmlLoader.load());
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}
