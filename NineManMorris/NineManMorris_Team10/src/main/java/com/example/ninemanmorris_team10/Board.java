package com.example.ninemanmorris_team10;

import com.example.ninemanmorris_team10.BoardAction.AlertAction;
import com.example.ninemanmorris_team10.BoardAction.BoardAction;
import com.example.ninemanmorris_team10.BoardAction.ShowEndGameAction;
import com.example.ninemanmorris_team10.Player.Player;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.shape.Circle;

import java.util.*;

/**
 * Represents the game board of 9MM.
 */
public class Board {

    private static ArrayList<Circle> arrCircleTokens;
    private static ArrayList<Position> arrPosition;
    private static ArrayList<BoardAction> boardActions;
    private static Board instance;

    private final List<List<Integer>> adjPosList = new ArrayList<>(Arrays.asList(List.of(1, 9), List.of(0, 2, 4),
            List.of(1, 14), List.of(4, 10), List.of(1, 3, 5, 7), List.of(4, 13), List.of(7, 11), List.of(4, 6, 8),
            List.of(7, 12), List.of(0, 10, 21), List.of(3, 9, 11, 18), List.of(6, 10, 15), List.of(8, 13, 17),
            List.of(5, 12, 14, 20), List.of(2, 13, 23), List.of(11, 16), List.of(15, 17, 19), List.of(12, 16),
            List.of(10, 19), List.of(16, 18, 20, 22), List.of(13, 19), List.of(9, 22), List.of(19, 21, 23),
            List.of(14, 22)));

    private final List<List<Integer>> millCombinations = new ArrayList<>(Arrays.asList(
            // horizontal mills
            List.of(0,1,2), List.of(3,4,5), List.of(6,7,8), List.of(9,10,11),
            List.of(12,13,14), List.of(15,16,17), List.of(18,19,20), List.of(21,22,23),
            // vertical mills
            List.of(0,9,21), List.of(1,4,7), List.of(2,14,23), List.of(3,10,18),
            List.of(6,11,15),  List.of(16,19,22), List.of(8,12,17), List.of(5,13,20)
    ));

    /**
     * Constructor of game Board class.
     * Private constructor to prevent instantiation outside the class
     * @param arrCirclePos Array of JavaFX circle element that represents each position on the board
     */
    private Board(ArrayList<Circle> arrCirclePos) {

        // create an array to store all Position object on the board
        arrPosition = new ArrayList<>();
        for (Circle c: arrCirclePos){
            Position pos = new Position(c);
            arrPosition.add(pos);
        }

        // initialize adjacent position list for each Position object
        for (int i=0; i<arrCirclePos.size(); i++) {
            for (Integer aP : adjPosList.get(i)) {
                Position adjP = arrPosition.get(aP);
                arrPosition.get(i).addAdjacentPos(adjP);
            }
        }

        // initialize mill object and add to millList for each Position object
        for (int i=0; i< arrPosition.size(); i++){
            for (int j=0; j<millCombinations.size(); j++){
                if (millCombinations.get(j).contains(i)){
                    ArrayList millList = new ArrayList<>();
                    for (int k=0; k<3; k++){
                        if (millCombinations.get(j).get(k)!=i){
                            millList.add(arrPosition.get(millCombinations.get(j).get(k)));
                        }
                    }
                    Mill mill = new Mill(millList);
                    arrPosition.get(i).addMillList(mill);
                }
            }
        }
    }

    /**
     * Method to initialize and get board instance as there is only one game board object initialized throughout the game.
     * @param arrCirclePos Array of JavaFX circle element that represents each position on the board
     * @param arrCircleToken Array of JavaFX circle element that represents each token on the board
     * @param popUpWindows Array of JavaFX pop up windows to display alerts
     * @param labels Array of JavaFX Label element that represents each label in the user interface
     *
     * @return Game board instance once the board have already been created
     */
    public static Board getInstance(ArrayList<Circle> arrCirclePos, ArrayList<Circle> arrCircleToken,
                                    ArrayList<DialogPane> popUpWindows, ArrayList<Label> labels,
                                    Button okBtn) {
        // if game board haven't been created yet, create one
        if (instance == null) {
            instance = new Board(arrCirclePos);
        }

        // reset board actions for game board
        setBoardActions(popUpWindows, labels, okBtn);

        // reset circle tokens for game board since JavaFX will recreate new Circle for each new game
        arrCircleTokens = arrCircleToken;

        return instance;
    }

    /**
     * Set board actions for game board.
     * @param popUpWindows Array of JavaFX pop up windows to display alerts
     * @param labels Array of JavaFX Label element that represents each label in the user interface
     */
    private static void setBoardActions(ArrayList<DialogPane> popUpWindows, ArrayList<Label> labels, Button okBtn) {
        DialogPane resultPopUp = popUpWindows.get(0);
        DialogPane alertPopUp = popUpWindows.get(1);

        Label winnerLabel = labels.get(0);
        Label alertLabel = labels.get(1);

        // Add BoardActions to board
        boardActions = new ArrayList<>();
        boardActions.add(new ShowEndGameAction(resultPopUp, winnerLabel));
        boardActions.add(new AlertAction(alertPopUp, alertLabel, okBtn));
    }

    public ArrayList<Position> getArrPosition(){
        return arrPosition;
    }

    public ArrayList<Circle> getArrCircleTokens() {
        return arrCircleTokens;
    }




    public List<List<Integer>> getAdjPosList() {
        return adjPosList;
    }

    /**
     * Check whether the player still have valid moves to perform.
     * @param player Player to be checked
     *
     * @return True is player has valid moves to perform, i.e. adjacent position is empty, else false
     */
    public static boolean checkValidMove(Player player) {
        // loop through player's token list and check is there valid moves for all tokens
        ArrayList<Token> tokens = player.getMyTokens();
        for (Token t: tokens) {
            Position pos = t.getPosition();
            for (int i = 0; i < pos.getAdjacentPos().size(); i++) {
                if (!pos.getAdjacentPos().get(i).isContainToken()) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method to execute BoardActions to show popup.
     * @param actionId Action id, unique for each board actions
     * @param text Text to be displayed in the popup
     */
    public static void showPopUp(String actionId, String text, Player currentPlayer) {
        for (BoardAction b: boardActions) {
            if (b.getActionId() == actionId) {
                b.execute(text, currentPlayer);
            }
        }
    }

    /**
     * Method to check all positions on the board and make sure tokens that belongs to mill are coloured in red.
     * @param player current player which is having his turn
     * @param opponent current player's opponent
     */
    public void checkBoardsMill(Player player, Player opponent){
        for (Position p: this.getArrPosition()){
            if(p.isContainToken()){
                if(p.getToken().getBelongsToPlayer() == player){
                    p.checkMill(player);
                } else{
                    p.checkMill(opponent);
                }
            }
        }
    }

    /**
     * Remove all tokens from board.
     */
    public void reset() {
        for (Position pos: arrPosition) {
            pos.removeToken();
        }
    }

    /**
     * Undo Board state to previous state.
     * @param previousState previous Board state
     */
    public void undoPreviousState(ArrayList<Map<String, Object>> previousState) {
        Token token = null;

        for (int i = 0; i < arrPosition.size(); i++) {
            Position position = arrPosition.get(i);

            // Remove current Token from Position
            position.removeToken();

            // Set containToken to previous value
            position.setContainToken((Boolean) previousState.get(i).get("posContainToken"));

            if (position.isContainToken()) {
                // Previous Token
                Token prevToken = (Token) previousState.get(i).get("Token");

                // Recreate Token
                token = new Token(position, prevToken.getBelongsToPlayer(), arrCircleTokens.get(i));

                // Assign Token to Player Token list
                token.getBelongsToPlayer().getMyTokens().add(token);

                // Place Token on Position
                position.putToken(token);

                // Convert tokens to be non-mill
                position.getToken().setMill(false);
            }
        }
        if (token != null){
            // Check of all mills on board to make sure mills are still coloured in red if they still belong to another mill after removing/moving token
            checkBoardsMill(token.getBelongsToPlayer(), token.getBelongsToPlayer().getOpponent());
        }
    }

    /**
     * Return a Board description.
     * @return String describing the Board state.
     */
    @Override
    public String toString() {

        String str = "Board: ";
        for (Position pos: arrPosition) {
            str += "{ Pos x: " + pos.getX() + ", Pos y: " + pos.getY();
            str += ", Pos contain token: " + pos.isContainToken();

            if (pos.isContainToken()) {
                str += ", Pos token: " + pos.getToken();
            }
            str += " }";
        }
        return str;
    }
}

