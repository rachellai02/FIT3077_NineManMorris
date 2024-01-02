package com.example.ninemanmorris_team10.Controller;

import com.example.ninemanmorris_team10.*;
import com.example.ninemanmorris_team10.ButtonAction.QuitGameAction;
import com.example.ninemanmorris_team10.ButtonAction.SaveGameAction;
import com.example.ninemanmorris_team10.Enum.Alert;
import com.example.ninemanmorris_team10.Enum.Capabilities;
import com.example.ninemanmorris_team10.Player.Player;
import com.example.ninemanmorris_team10.TokenAction.MoveTokenAction;
import com.example.ninemanmorris_team10.TokenAction.PlaceTokenAction;
import com.example.ninemanmorris_team10.TokenAction.RemoveTokenAction;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Contains all the front end components. Manages the user interface.
 */
public class GameController implements Initializable {
    @FXML
    private Stage stage;

    @FXML
    private Circle circle0, circle1, circle2, circle3, circle4, circle5, circle6, circle7, circle8, circle9, circle10,
            circle11, circle12, circle13, circle14, circle15, circle16, circle17, circle18, circle19, circle20,
            circle21, circle22, circle23;

    @FXML
    private Circle token0, token1, token2, token3, token4, token5, token6, token7, token8, token9, token10, token11,
            token12, token13, token14, token15, token16, token17, token18, token19, token20, token21,
            token22, token23;

    @FXML
    private Circle p1sideToken1, p1sideToken2, p1sideToken3, p1sideToken4, p1sideToken5, p1sideToken6, p1sideToken7,
            p1sideToken8, p1sideToken9, p2sideToken1, p2sideToken2, p2sideToken3, p2sideToken4, p2sideToken5,
            p2sideToken6, p2sideToken7, p2sideToken8, p2sideToken9;

    @FXML
    private Label winner, alertText, playersTurn, movesLabel, Player1_NumPiecesOnBoard, Player2_NumPiecesOnBoard;

    @FXML
    private DialogPane resultPopup, alertPopup, confirmationPopup, saveGamePopup;

    @FXML
    private Button alertOkBtn;

    private ArrayList<Circle> arrToken;

    private Board myBoard;
    private Game game;
    private Position prevPos;
    private Circle prevToken;

    private boolean playWithComputer;
    private ArrayList<GameState> history = new ArrayList<>();
    private boolean isReloaded = false;

    @FXML
    /**
     * Initialize game and game board.
     */
    public void initialize(URL location, ResourceBundle resources) {

        ArrayList<Circle> arrCircle = new ArrayList<>(Arrays.asList(circle0, circle1, circle2, circle3, circle4, circle5, circle6,
                circle7, circle8, circle9, circle10, circle11, circle12, circle13, circle14, circle15, circle16,
                circle17, circle18, circle19, circle20, circle21, circle22, circle23));

        arrToken = new ArrayList<>(Arrays.asList(token0, token1, token2, token3, token4, token5, token6, token7,
                token8, token9, token10, token11, token12, token13, token14, token15, token16, token17, token18,
                token19, token20, token21, token22, token23));

        ArrayList<Circle> arrp1SideToken = new ArrayList<>(Arrays.asList(p1sideToken1, p1sideToken2, p1sideToken3, p1sideToken4,
                p1sideToken5, p1sideToken6, p1sideToken7, p1sideToken8, p1sideToken9));
        ArrayList<Circle> arrp2SideToken = new ArrayList<>(Arrays.asList(p2sideToken1, p2sideToken2, p2sideToken3, p2sideToken4,
                p2sideToken5, p2sideToken6, p2sideToken7, p2sideToken8, p2sideToken9));

        ArrayList<DialogPane> arrPopup = new ArrayList<>(Arrays.asList(resultPopup, alertPopup));
        ArrayList<Label> arrPopupLabel = new ArrayList<>(Arrays.asList(winner, alertText));
        ArrayList<Label> arrGameLabel = new ArrayList<>(Arrays.asList(playersTurn, movesLabel, Player1_NumPiecesOnBoard,
                Player2_NumPiecesOnBoard));

        // initialize Game and Board
        myBoard = Board.getInstance(arrCircle, arrToken, arrPopup, arrPopupLabel, alertOkBtn);
        myBoard.reset();
        game = new Game(myBoard, arrToken, arrGameLabel, arrp1SideToken, arrp2SideToken, playWithComputer, isReloaded, history);
    }

    @FXML
    /**
     * Close alert pop up window and allows players to resume the game.
     */
    void resumeGame() {
        alertPopup.setVisible(false);
        game.getCurrentPlayer().removeCapability(Capabilities.BLOCK_ACTION);
    }

    @FXML
    /**
     * Allows players to start a new game.
     */
    void startGame(Event event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(GameApplication.class.getResource("/com/example/ninemanmorris_team10/game-board.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    /**
     * Shows an exit confirmation popup window.
     */
    void quitBtnClicked(){
        if (!game.getCurrentPlayer().hasCapability(Capabilities.BLOCK_ACTION)) {
            confirmationPopup.setVisible(true);
            game.getCurrentPlayer().addCapability(Capabilities.BLOCK_ACTION);
        }
    }

    @FXML
    /**
     * Save game state in .txt file.
     */
    void saveBtnClicked(Event event) throws IOException {
        if (!game.getCurrentPlayer().hasCapability(Capabilities.BLOCK_ACTION)) {
            saveGame(event);
        }
    }

    @FXML
    /**
     * Undo to previous step.
     */
    void undoBtnClicked() {
        if (!game.getCurrentPlayer().hasCapability(Capabilities.BLOCK_ACTION)) {
            game.getGameCaretaker().undoState();
        }
    }

    @FXML
    /**
     *  Shows a save game popup window if player confirms to quit game.
     */
    void confirmQuitGame() {
        confirmationPopup.setVisible(false);
        saveGamePopup.setVisible(true);
    }

    @FXML
    /**
     * Close quit game confirmation pop up window and allows players to resume the game.
     */
    void notQuitGame() {
        confirmationPopup.setVisible(false);
        game.getCurrentPlayer().removeCapability(Capabilities.BLOCK_ACTION);
    }

    @FXML
    /**
     * Switch scene to main page to allow players to quit the game.
     */
    void quitGame(Event event) throws IOException{
        resultPopup.setVisible(false);
        saveGamePopup.setVisible(false);
        game.getCurrentPlayer().removeCapability(Capabilities.BLOCK_ACTION);
        QuitGameAction qga = new QuitGameAction(event, stage);
        qga.execute();
    }

    @FXML
    /**
     * Save game state in .txt file.
     */
    void saveGame(Event event) throws IOException {
        SaveGameAction sga = new SaveGameAction(event, stage, game.getGameCaretaker(), playWithComputer);
        sga.execute();
    }

    @FXML
    /**
     * Save game state in .txt file. and quit game.
     */
    void saveAndQuitGame(Event event) throws IOException {
        saveGame(event);
        quitGame(event);
    }

    /**
     * Set game mode based on player's click action, either playing as 2 players or playing with a computer
     * @param computerGameMode
     */
    void setGameMode(boolean computerGameMode){
        playWithComputer = computerGameMode;
    }

    public void setHistory(ArrayList<GameState> history) {
        this.history = history;
    }

    public void setReloaded(boolean reloaded) {
        isReloaded = reloaded;
    }

    @FXML
    /**
     * Allows players to place a move or move token to new position by clicking on an empty position.
     */
    void positionClicked(MouseEvent event) throws InterruptedException {
        // Get the UI of the position
        Circle circleClicked = (Circle) event.getSource();
        String circleId = circleClicked.getId().replace("circle", "");
        Circle circleToken = arrToken.get(Integer.parseInt(circleId));

        // Get current player
        Player currentPlayer = game.getCurrentPlayer();

        // Get position clicked
        Position posClicked = myBoard.getArrPosition().get(Integer.parseInt(circleId));

        Boolean isActionSuccess = true;
        String alertMessage = "";

        if (!currentPlayer.hasCapability(Capabilities.BLOCK_ACTION)) {
            // ALERT: Player forgot to remove opponent's token.
            if (currentPlayer.hasCapability(Capabilities.REMOVE)){
                Board.showPopUp("Alert", Alert.REMOVE_EMPTY_POS.getAlertMessage(), this.game.getCurrentPlayer());
            }

            // Let player place token.
            else if (currentPlayer.hasCapability(Capabilities.PLACE) && currentPlayer.getHoldingToken() == null) {
                Token token = new Token(posClicked, currentPlayer, circleToken);
                PlaceTokenAction pta = new PlaceTokenAction(game, currentPlayer, posClicked, token);
                isActionSuccess = pta.execute();
            }

            else if (currentPlayer.hasCapability(Capabilities.MOVE) || currentPlayer.hasCapability(Capabilities.FLY)) {
                // ALERT: Player does not select a token.
                if (currentPlayer.getHoldingToken() == null){
                    isActionSuccess = false;
                    alertMessage = Alert.MOVE_NO_TOKEN_CLICK.getAlertMessage();
                }

                // Let player move or fly token.
                else {
                    MoveTokenAction mta = new MoveTokenAction(game, currentPlayer, circleToken, prevToken, posClicked, currentPlayer.getHoldingToken());
                    isActionSuccess = mta.execute();
                    alertMessage = Alert.MOVE_NON_ADJ_POS.getAlertMessage();
                }
            }

            if (!currentPlayer.hasCapability(Capabilities.REMOVE)) {
                if (isActionSuccess) { // the game continues
                    game.run();
                } else {
                    Board.showPopUp("Alert", alertMessage, this.game.getCurrentPlayer());
                }
            }
        }
    }

    @FXML
    /**
     * Allows players to remove opponent's token or select token to move by clicking on the token.
     */
    void tokenClicked(MouseEvent event ) {
        // Get the UI of the token
        Circle circleClicked = (Circle) event.getSource();
        String circleId = circleClicked.getId().replace("token", "");

        // Get current player
        Player currentPlayer = game.getCurrentPlayer();

        // Get the token clicked via position
        Position posClicked = myBoard.getArrPosition().get(Integer.parseInt(circleId));
        Token tokenClicked = posClicked.getToken();

        Boolean isActionSuccess;

        if (!currentPlayer.hasCapability(Capabilities.BLOCK_ACTION)) {
            if(currentPlayer.hasCapability(Capabilities.REMOVE)){
                // ALERT: Click on player's own token.
                // Do not allow player to remove his own token.
                if(tokenClicked.getBelongsToPlayer() == currentPlayer){
                    Board.showPopUp("Alert", Alert.REMOVE_OWN_TOKEN_CLICK.getAlertMessage(),  this.game.getCurrentPlayer());
                }
                // Let player remove opponent's token
                else{
                    RemoveTokenAction rta = new RemoveTokenAction(game, currentPlayer, posClicked, tokenClicked);
                    isActionSuccess = rta.execute();
                    if(isActionSuccess){
                        game.run();
                    } else {
                        Board.showPopUp("Alert", Alert.REMOVE_MILL_TOKEN_CLICK.getAlertMessage(), this.game.getCurrentPlayer());
                    }
                }
            }

            // ALERT: Click opponent's token if player has no remove capability.
            // Do not allow player to remove opponent's token.
            else if (tokenClicked.getBelongsToPlayer() != currentPlayer){
                Board.showPopUp("Alert", Alert.PLACE_OPPONENT_TOKEN_CLICK.getAlertMessage(), this.game.getCurrentPlayer());
            }

            // ALERT: Click on player's own token when placing.
            // Do not allow player to select token to move.
            else if (currentPlayer.hasCapability(Capabilities.PLACE)){
                Board.showPopUp("Alert", Alert.PLACE_OWN_TOKEN_CLICK.getAlertMessage(), this.game.getCurrentPlayer());
            }

            // Click on player's own token when can move or fly, let player hold token in order to move token
            else {
                prevPos = tokenClicked.getPosition();
                tokenClicked.setPrevPosition(prevPos);

                // player was holding a token, would like to select/hold other tokens
                if (currentPlayer.getHoldingToken() != null) {
                    // if previously held token is part of mill, need set to red
                    if (currentPlayer.getHoldingToken().isMill()) {
                        prevToken.setStroke(Color.RED);
                    } else {
                        prevToken.setStroke(Color.BLACK);
                    }
                }
                currentPlayer.setHoldingToken(tokenClicked);
                prevToken = tokenClicked.getCircleToken();
                currentPlayer.getHoldingToken().getCircleToken().setStroke(Color.GREEN);
            }
        }
    }
}