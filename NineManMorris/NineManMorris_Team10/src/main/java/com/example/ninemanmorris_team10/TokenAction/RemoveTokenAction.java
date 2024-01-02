package com.example.ninemanmorris_team10.TokenAction;

import com.example.ninemanmorris_team10.*;
import com.example.ninemanmorris_team10.Enum.Capabilities;
import com.example.ninemanmorris_team10.Player.Player;

import java.util.ArrayList;

/**
 * Allows player to remove their token.
 */
public class RemoveTokenAction extends TokenAction {

    /***
     * Constructor for RemoveTokenAction.
     * @param game The game
     * @param player The player performing the action
     * @param position The position of the token
     * @param token The token to be removed, aka OPPONENT'S TOKEN
     */
    public RemoveTokenAction(Game game, Player player, Position position, Token token) {
        super(game, player, position, token);
    }

    /**
     * Checks to see if the token that is being removed is part of a mill.
     * A token that is part of a mill may be removed if and only if there are no other tokens left.
     *
     * @return True if is a valid move, false otherwise
     */
    public boolean checkValidRemove(){
        Player opponent = token.getBelongsToPlayer();
        ArrayList<Token> nonMills = opponent.getNonMills();

        if(nonMills.size() != 0 && token.isMill()){
            // There are non-mills, return true is token clicked is non-mill, otherwise false
            return false;
        }
        // Let player remove a token even if it is part of a mill.
        else{
            return true;
        }
    }

    /**
     * Removes a token from the board if the game rule allows it.
     * @return True if the token was removed, false otherwise
     */
    @Override
    public boolean execute() {

        if(checkValidRemove()){
            // Remove token from opponent
            Player opponent = token.getBelongsToPlayer();
            opponent.removeTokenFromList(token);

            // Remove token from view
            token.setPosition(null);

            // Convert tokens to be non-mill
            for (Position pos: game.getGameBoard().getArrPosition()){
                if (pos.isContainToken()){
                    pos.getToken().setMill(false);
                }
            }

            // Remove token from position
            position.removeToken();

            // Update labels
            game.updateTokenLabel();

            // Remove token from player's hand
            player.setHoldingToken(null);

            // Update statuses
            player.removeCapability(Capabilities.REMOVE);
            if (player.getNumOfTokensPlaced()<9){
                player.addCapability(Capabilities.PLACE);
            }
            else if (player.getMyTokens().size()<=3){
                player.addCapability(Capabilities.FLY);
            }
            else {
                player.addCapability(Capabilities.MOVE);
            }

            // Check of all mills on board to make sure mills are still coloured in red if they still belong to another mill after removing/moving token
            game.getGameBoard().checkBoardsMill(player, player.getOpponent());

            return true;
        }
        else{
            return false;
        }

    }
}

