package com.example.ninemanmorris_team10.ButtonAction;

import com.example.ninemanmorris_team10.GameCaretaker;
import com.example.ninemanmorris_team10.GameState;
import javafx.event.Event;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Allows players to save game in .txt file.
 */
public class SaveGameAction extends ButtonAction{

    private final GameCaretaker gameCaretaker;
    private boolean playWithComputer;

    /**
     * Constructor for SaveGameAction.
     * @param event UI event
     * @param stage JavaFX element
     * @param gameCaretaker gameCaretaker
     */
    public SaveGameAction(Event event, Stage stage, GameCaretaker gameCaretaker, boolean playWithComputer) {
        super(event, stage);
        this.gameCaretaker = gameCaretaker;
        this.playWithComputer = playWithComputer;
    }

    /**
     * Allows player to select a .txt file (will overwrite the saved game state) or create a new .txt to save the game
     * state.
     *
     * @return true
     * @throws IOException
     */
    @Override
    public boolean execute() throws IOException {
        // Get history
        ArrayList<GameState> history = gameCaretaker.getHistory();

        // Create a file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select or Create a Text File");

        // Set the file extension filter to only allow text files
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text Files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show the file chooser dialog and get the selected file
        File selectedFile = fileChooser.showSaveDialog(stage);

        if (selectedFile != null) {
            // User selected a file
//            String content = history.toString();
            String filePath = selectedFile.getAbsolutePath();

            String content = "";
            if(playWithComputer){
                content += "COMPUTER\n";
            }
            else{
                content += "HUMAN\n";
            }

            for (int i = 0; i < history.size(); i++) {
                content += history.get(i).toFile();
            }
            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
                writer.write(content);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }
}
