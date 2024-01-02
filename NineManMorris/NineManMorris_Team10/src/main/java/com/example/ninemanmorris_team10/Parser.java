package com.example.ninemanmorris_team10;

import com.example.ninemanmorris_team10.Enum.Capabilities;

import java.io.File;
import java.util.*;

public class Parser {

    private File file;
    private String playerTurn = "";
    private boolean againstComputer = false;
    private ArrayList<GameState> history = new ArrayList<>();

    /**
     * Creates a parser to read a text file that contains the history of the game.
     * @param file the file
     */
    public Parser(File file){
        this.file = file;
    }

    /**
     * Reads the file.
     */
    public boolean parse(){
        try {
            // Create a Scanner object to read the file
            Scanner scanner = new Scanner(file);

            // Set game mode
            againstComputer = scanner.nextLine().equals("COMPUTER");

            while (scanner.hasNextLine()) {
                ArrayList<Map<String, Object>> boardState = new ArrayList<>(); // [pos1, pos2, pos3, pos4, ...]
                ArrayList<Map<String, Object>> playersState =  new ArrayList<>(); // [player1, player2]

                // Read the first line as an integer
                playerTurn = scanner.nextLine();

                boardState = this.readBoardState(scanner);
                playersState = this.readPlayerState(scanner);
                GameState gs = new GameState(playerTurn, boardState, playersState);
                history.add(gs);

                // Skip the empty line between blocks
                if (scanner.hasNextLine()) {
                    scanner.nextLine();
                }
            }
            scanner.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * Reads 24 lines which represents the positions. Contains info such as containsToken. If true, additional info is appended such as the player the token belongs to,
     * and if the token was part of a mill.
     * @param scanner scanner
     *
     * @return a list of dictionaries. One dictionary holds the information one position
     */
    private ArrayList<Map<String, Object>> readBoardState(Scanner scanner){
        ArrayList<Map<String, Object>> boardState = new ArrayList<>();
        String line;
        // Read the next 24 lines and split each line by the delimiter "|"
        for (int i = 0; i < 24; i++) {
            Map<String, Object> map = new HashMap<>();

            line = scanner.nextLine();
            String[] positions = line.split("\\|");

            // Extract the values from the split line and store them in variables
            boolean isContainsToken = Boolean.parseBoolean(positions[1]);
            boolean isMill = false;
            String belongsToPlayer = "";

            if (isContainsToken) {
                isMill = Boolean.parseBoolean(positions[2]);
                belongsToPlayer = positions[3];
                map.put("posContainToken", isContainsToken);
                map.put("tokenIsMill", isMill);
                map.put("tokenBelongsToPlayer", belongsToPlayer);
            }
            else{
                map.put("posContainToken", isContainsToken);
            }

            boardState.add(map);
        }
        return boardState;
    }

    /**
     * Reads 2 lines which is both player's info. Has info such as player name, number of tokens placed and capabilities.
     * @param scanner scanner
     *
     * @return a list of dictionaries. One dictionary holds the information for one player
     */
    public ArrayList<Map<String, Object>> readPlayerState(Scanner scanner){
        String line;
        ArrayList<Map<String, Object>> playersState =  new ArrayList<>();
        // Read the next 2 lines and
        for (int i = 0; i < 2; i++) {
            Map<String, Object> map = new HashMap<>();
            line = scanner.nextLine();
            String[] playerInfo = line.split("\\|");
            String name = playerInfo[0];
            int numTokensPlaced = Integer.parseInt(playerInfo[1]);
            String capability = playerInfo[2];

            map.put("name", name);
            map.put("numOfTokensPlaced", numTokensPlaced);
            map.put("capabilities", convertToEnum(capability));
            playersState.add(map);
        }
        return playersState;
    }


    /**
     * Converts strings to enum.
     * @param capa a string representing a capability
     *
     * @return A set of enums
     */
    public Object convertToEnum(String capa){
        Object capabilities = Capabilities.PLACE; // Initialize with any value
        if(capa.equals("PLACE")){
            capabilities = Capabilities.PLACE;
        }
        else if (capa.equals("MOVE")) {
            capabilities = Capabilities.MOVE;
        }
        else if (capa.equals("FLY")) {
            capabilities = Capabilities.FLY;
        }
        else if (capa.equals("REMOVE")) {
            capabilities = Capabilities.REMOVE;
        }
        else if (capa.equals("BLOCK_ACTION")) {
            capabilities = Capabilities.BLOCK_ACTION;
        }
        return  capabilities;
    }

    public ArrayList<GameState> getHistory() {
        return history;
    }

    public boolean getAgainstComputer() {
        return againstComputer;
    }
}
