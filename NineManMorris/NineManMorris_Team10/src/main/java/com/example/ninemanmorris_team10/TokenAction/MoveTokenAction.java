package com.example.ninemanmorris_team10.TokenAction;

import com.example.ninemanmorris_team10.Enum.Capabilities;
import com.example.ninemanmorris_team10.Game;
import com.example.ninemanmorris_team10.Player.Player;
import com.example.ninemanmorris_team10.Position;
import com.example.ninemanmorris_team10.Token;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Allows player to move his token from one position to another.
 */
public class MoveTokenAction extends TokenAction {

    private final Circle prevToken, circleToken;

    /***
     * Constructor for MoveTokenAction.
     * @param game The game
     * @param player The player performing the action
     * @param circlet JavaFX element that indicates the token in new position
     * @param prevToken JavaFX element that indicates the token in previous position
     * @param position The new position of the token
     * @param token The token to be moved
     */
    public MoveTokenAction(Game game, Player player, Circle circlet, Circle prevToken,
                           Position position, Token token) {
        super(game, player, position, token);
        this.prevToken = prevToken;
        this.circleToken = circlet;
    }
    /**
     * Moves a token across the board.
     *
     * @return True if is a valid move, false otherwise
     */
    @Override
    public boolean execute() {
        // default true value when player can fly
        Boolean isInAdjPos=true;

        // if can't fly, check if clicked position is an adjacent position
        if (player.hasCapability(Capabilities.MOVE)){
            isInAdjPos=token.getPosition().getAdjacentPos().contains(position);
        }

        if (isInAdjPos && player.getHoldingToken() != null){

            token.setCircleToken(circleToken);

            // Convert tokens to be non-mill
            for (Position pos: game.getGameBoard().getArrPosition()){
                if (pos.isContainToken()){
                    pos.getToken().setMill(false);
                }
            }

            // remove green stroke around the holding token
            prevToken.setStroke(Color.BLACK);
            prevToken.setVisible(false); // remove token from previous position in view
            token.getPrevPosition().removeToken();

            token.setPosition(position); // set token position
//            token.setColor();

            position.putToken(token);
            game.updateTokenLabel();

            player.setHoldingToken(null);

            if (position.checkMill(player)){

                player.addCapability(Capabilities.REMOVE); //player formed a mill, need to remove opponent's token
                if (player.hasCapability(Capabilities.MOVE)){
                    player.removeCapability(Capabilities.MOVE);
                }
                else if (player.hasCapability(Capabilities.FLY)){
                    player.removeCapability(Capabilities.FLY);
                }

                game.getGameCaretaker().saveState(); // save game state if player formed a mill
            }

            //game.getGameCaretaker().saveState();
            game.updateGameLabel();

            // check of all mills on board to make sure mills are still coloured in red if they still belong to another mill after removing/moving token
            game.getGameBoard().checkBoardsMill(player, player.getOpponent());

            return true;
        }
        else {
            return false;
        }
    }
}

