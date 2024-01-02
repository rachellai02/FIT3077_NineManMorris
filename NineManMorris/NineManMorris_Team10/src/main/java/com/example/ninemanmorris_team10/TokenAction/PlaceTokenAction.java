package com.example.ninemanmorris_team10.TokenAction;

import com.example.ninemanmorris_team10.Enum.Capabilities;
import com.example.ninemanmorris_team10.Game;
import com.example.ninemanmorris_team10.Player.Player;
import com.example.ninemanmorris_team10.Position;
import com.example.ninemanmorris_team10.Token;

/**
 * Allows player to place a token on the board.
 */
public class PlaceTokenAction extends TokenAction {

    /***
     * Constructor for PlaceTokenAction.
     * @param game The game
     * @param player The player performing the action
     * @param position The position of the token
     * @param token The token to be placed
     */
    public PlaceTokenAction(Game game, Player player, Position position, Token token){
        super(game, player, position, token);
    }

    /**
     * Places a token on a position on the board.
     *
     * @return True
     */
    @Override
    public boolean execute() {

        // Puts a token on a specified position
        position.putToken(token);

//        // Set token color
//        token.setColor();

        // Add token to player's token list
        player.addTokenToList(token);
        game.updateTokenLabel();

        // Check if players formed a mill
        if (position.checkMill(player)) {
            player.removeCapability(Capabilities.PLACE);
            player.addCapability(Capabilities.REMOVE);

            game.getGameCaretaker().saveState(); // save game state if player formed a mill
        }

        //game.getGameCaretaker().saveState();
        game.updateGameLabel();

        // check of all mills on board to make sure mills are still coloured in red if they still belong to another mill after removing/moving token
        game.getGameBoard().checkBoardsMill(player, player.getOpponent());
        return true;
    }
}

