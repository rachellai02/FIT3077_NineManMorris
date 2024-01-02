package com.example.ninemanmorris_team10;

import com.example.ninemanmorris_team10.Enum.Capabilities;
import com.example.ninemanmorris_team10.Player.ComputerPlayer;
import com.example.ninemanmorris_team10.Player.HumanPlayer;
import com.example.ninemanmorris_team10.Player.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Map;

/**
 * Sets up the game. Controls the flow of the game.
 *
 */
public class Game {

    private final Board gameBoard;
    private Player player1;
    private Player player2;
    private Player winner;
    private String playerTurn;
    private final GameCaretaker gameCaretaker;
    private final Label player1_NumPiecesOnBoard, player2_NumPiecesOnBoard, playersTurnLabel, movesLabel;
    private final ArrayList<Circle> arrCircleToken, p1sideToken, p2sideToken;
    private boolean isCompGameMode;

    private boolean isReloaded;

    private ArrayList<GameState> history = new ArrayList<>();

    /**
     * Constructor of game class.
     *
     * @param board       Game board object
     * @param arrToken    Array of JavaFX Circle element that represents each token in the UI
     * @param labels      Array of JavaFX Label element that represents each label in the UI
     * @param p1sideToken Array of JavaFX Circle element that represents player1's tokens beside the game board in the UI
     * @param p2sideToken Array of JavaFX Circle element that represents player2's tokens beside the game board in the UI
     */
    public Game (Board board, ArrayList<Circle> arrToken, ArrayList<Label> labels, ArrayList<Circle> p1sideToken, ArrayList<Circle> p2sideToken, boolean isCompMode, boolean isReloaded, ArrayList<GameState> history){
        this.gameBoard = board;

        // create HumanPlayer object and set as player1
        this.player1 = new HumanPlayer("Player 1");
        this.player1.setColor(Color.WHITE);

        // create opponent player object and set as player2
        if (isCompMode){
            this.player2 = new ComputerPlayer("Computer");
        } else {
            this.player2 = new HumanPlayer("Player 2");
        }
        this.player2.setColor(Color.BLACK);

        this.player1.setOpponent(player2);
        this.player2.setOpponent(player1);

        // Start with player 1
        this.playerTurn = "1";
        this.isReloaded = isReloaded;
        this.history = history;

        // Initialize gameCareTaker
        this.gameCaretaker = new GameCaretaker(this);

        this.playersTurnLabel = labels.get(0);
        this.movesLabel = labels.get(1);
        this.player1_NumPiecesOnBoard = labels.get(2);
        this.player2_NumPiecesOnBoard = labels.get(3);

        this.p1sideToken=p1sideToken;
        this.p2sideToken=p2sideToken;

        this.isCompGameMode = isCompMode;

        this.arrCircleToken = arrToken;

        this.gameCaretaker.saveInitialState();

        if(isReloaded){
            if(!history.isEmpty()){
                this.recreateTokens();
                gameCaretaker.setHistory(history);
                GameState gs = gameCaretaker.getLatestHistory();
                this.undo(gs);
            }
        }
    }

    /**
     * Save current GameState.
     * @return current GameState
     */
    public GameState save() {
        return new GameState(playerTurn, gameBoard, player1, player2);
    }

    /**
     * Undo to previous GameState.
     * @param gameState previous GameState
     */
    public void undo(GameState gameState) {

        // Undo previous state
        player1.undoPreviousState(gameState.getPlayersState().get(0));
        player2.undoPreviousState(gameState.getPlayersState().get(1));
        gameBoard.undoPreviousState(gameState.getBoardState());
        this.playerTurn = gameState.getPlayerTurn();

        // Update label
        updateGameLabel();
        updateTokenLabel();
    }

    public GameCaretaker getGameCaretaker() {
        return gameCaretaker;
    }

    public void setWinner(Player winner) {
        this.winner = winner;
    }

    /**
     * Changes the player's turn.
     */
    public void changePlayerTurn(){
        if (this.playerTurn.equals("1")){
            this.playerTurn = "2";
        } else{
            this.playerTurn = "1";
        }
    }

    /**
     * Gets the current player.
     *
     * @return Current player.
     */
    public Player getCurrentPlayer(){
        if (this.playerTurn.equals("1")){
            return this.player1;
        }
        return this.player2;
    }

    public String getPlayerTurn() {
        return this.playerTurn;
    }

    public Board getGameBoard() {
        return gameBoard;
    }

    public ArrayList<Circle> getArrCircleToken() {
        return arrCircleToken;
    }

    public boolean isCompGameMode() {
        return isCompGameMode;
    }

    /**
     * Method to update labels about number of tokens in game's user interface.
     */
    public void updateTokenLabel(){
        // update player's number of tokens on board
        player1_NumPiecesOnBoard.setText(""+this.player1.getPlayersNumOfTokens());
        player2_NumPiecesOnBoard.setText("" + this.player2.getPlayersNumOfTokens());

        // update side token list when player places token
        for (int i=0; i<9; i++){
            if (i<player1.getNumOfTokensPlaced()){
                p1sideToken.get(i).setVisible(false);
            }
            else {
                p1sideToken.get(i).setVisible(true);
            }
            if (i<player2.getNumOfTokensPlaced()){
                p2sideToken.get(i).setVisible(false);
            }
            else {
                p2sideToken.get(i).setVisible(true);
            }
        }
    }

    /**
     * Method to update labels about player's turn and actions to perform in game's user interface.
     */
    public void updateGameLabel(){
        // update player' turn label
        playersTurnLabel.setText("Player "+ playerTurn + "'s turn");

        // player's number of tokens square will have green border when it's the player's turn
        if (this.playerTurn == "1"){
            player1_NumPiecesOnBoard.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
            player2_NumPiecesOnBoard.setBorder(null);
        }
        else {
            player1_NumPiecesOnBoard.setBorder(null);
            player2_NumPiecesOnBoard.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(2))));
        }

        // according to player's capabilities, display moves to be performed by player as a guidance
        if (getCurrentPlayer().hasCapability(Capabilities.REMOVE)) {
            movesLabel.setText("Mill formed. Please select your opponent's token to remove.");
        }
        else if (getCurrentPlayer().hasCapability(Capabilities.MOVE)){
            movesLabel.setText("Please select a token to move to adjacent positions.");
        }
        else if (getCurrentPlayer().hasCapability(Capabilities.FLY)){
            movesLabel.setText("Please select a token to move to any positions.");
        }
        else if (getCurrentPlayer().hasCapability(Capabilities.PLACE)){
            movesLabel.setText("Please place a token.");
        }
    }

    /**
     * Check whether the game should still run.
     *
     * @return True if end game condition is not met, else false
     */
    public boolean stillRunning() {
        boolean gameRunning = true;
        Player currentPlayer = this.getCurrentPlayer();
        Player opponent = currentPlayer.getOpponent();

        // end game when opponent has less than 2 tokens
        boolean cond1 = opponent.hasCapability(Capabilities.FLY)
                && opponent.getPlayersNumOfTokens() <= 2;

        // end game when opponent has no valid moves that can be performed
        boolean cond2 = opponent.hasCapability(Capabilities.MOVE)
                && !Board.checkValidMove(opponent);

        if (cond1 || cond2) {
            // game ended, the winner is the player that had just made a move
            gameRunning = false;
            this.setWinner(currentPlayer);
        }

        return gameRunning;
    }

    /**
     * Continue running the game after each action player performed.
     */
    public void run() {
        // check if end game condition is met
        if (this.stillRunning()) {

            // update player's status(capabilities)
            this.player1.updateStatusEachTurn();
            this.player2.updateStatusEachTurn();

            // change player turn
            this.changePlayerTurn();

            // update game instruction
            updateGameLabel();

            // save current game state
            this.gameCaretaker.saveState();

            // computer's turn
            if (this.isCompGameMode && this.getPlayerTurn() == "2") {
                ComputerPlayer cp = (ComputerPlayer) this.getCurrentPlayer();

                // Timeline is used to delay execution of code, so the movements made by computer is slow enough to be viewed by player.
                Timeline timeline = new Timeline();

                // computer places/moves token only 0.6 second later
                KeyFrame keyFrame1 = new KeyFrame(Duration.seconds(0.6), event -> {
                    if (cp.hasCapability(Capabilities.PLACE)) {
                        cp.placeRandomToken(this);
                    } else if (cp.hasCapability(Capabilities.MOVE) || cp.hasCapability(Capabilities.FLY)) {
                        cp.moveRandomToken(this);
                    }
                });

                // if computer needs to remove token, it is performed 1.5 second later, and the game continues to run
                KeyFrame keyFrame2 = new KeyFrame(Duration.seconds(1.5), event -> {
                    if (cp.hasCapability(Capabilities.REMOVE)) {
                        cp.removeRandomToken(this);
                    }
                    updateGameLabel();

                    this.run(); // game should continue to run after computer made a move
                });
                timeline.getKeyFrames().addAll(keyFrame1, keyFrame2);
                timeline.play();
            }
        }

        // if end game condition is met, show end game popup
        else {
            this.getCurrentPlayer().addCapability(Capabilities.BLOCK_ACTION);
            gameBoard.reset();
            Board.showPopUp("ShowEndGame", this.winner.getName(), getCurrentPlayer());
        }

    }

    /**
     * Recreate tokens based on latest game state. To be used when reloading saved game
     *
     */
    public void recreateTokens(){
        // Loop through games states in history
        for(GameState gs: history){
            ArrayList<Map<String, Object>> boardState = new ArrayList<>(); // [pos1, pos2, pos3, pos4, ...]
            boardState = gs.getBoardState();

            // Recreate tokens
            // Loop through 24 positions
            for (int i = 0; i < boardState.size(); i++) {
                ArrayList<Position> positionArrayList = this.gameBoard.getArrPosition();
                Position position = positionArrayList.get(i);
                ArrayList<Circle> arrCircleTokens = this.gameBoard.getArrCircleTokens();
                Circle tokenUI = arrCircleTokens.get(i);

                if((boolean) boardState.get(i).get("posContainToken")) {
                    String name = (String) boardState.get(i).get("tokenBelongsToPlayer");
                    if (name.equals("Player 1")) {
                        Token token = new Token(position, this.player1, tokenUI);
                        boardState.get(i).put("Token", token);
                    } else if (name.equals("Player 2") || name.equals("Computer")) {
                        Token token = new Token(position, this.player2, tokenUI);
                        boardState.get(i).put("Token", token);
                    }
                }
            }
        }
    }
}

