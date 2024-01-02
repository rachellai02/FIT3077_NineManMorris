package com.example.ninemanmorris_team10.Player;

import com.example.ninemanmorris_team10.Enum.Capabilities;
import com.example.ninemanmorris_team10.Game;
import com.example.ninemanmorris_team10.Position;
import com.example.ninemanmorris_team10.Token;
import com.example.ninemanmorris_team10.TokenAction.MoveTokenAction;
import com.example.ninemanmorris_team10.TokenAction.PlaceTokenAction;
import com.example.ninemanmorris_team10.TokenAction.RemoveTokenAction;
import javafx.scene.shape.Circle;
import java.util.ArrayList;
import java.util.Random;

public class ComputerPlayer extends Player{

    private final Random randomNum;

    /**
     * Constructor for Computer Player.
     *
     * @param name      Name of Player
     */
    public ComputerPlayer(String name) {
        super(name);

        // added so that players cannot click the game board during computer's turn
        this.addCapability(Capabilities.BLOCK_ACTION);
        this.randomNum = new Random();
    }

    /**
     * Computer player randomly chooses an empty position to place token.
     * @param game current game
     */
    public void placeRandomToken(Game game){

        Position randomPos;
        int index;
        do{ // chooses random position to place token
            index = this.randomNum.nextInt(game.getGameBoard().getArrPosition().size());
            randomPos = game.getGameBoard().getArrPosition().get(index);
        } while(randomPos.isContainToken());

        Token token = new Token(randomPos, this, game.getArrCircleToken().get(index));
        PlaceTokenAction pta = new PlaceTokenAction(game, this, randomPos, token);
        pta.execute();
    }

    /**
     * Computer player randomly chooses one token from its token list, and move it to adjacent position;
     * if it can fly, the token is randomly moved to any empty position.
     * @param game current game
     */
    public void moveRandomToken(Game game){
        Position toPos = null;
        Token token;
        int index;

        if (this.hasCapability(Capabilities.FLY)){
            do { // computer can fly, hence it chooses a random token to move to a random position that is empty
                index = this.randomNum.nextInt(game.getGameBoard().getArrPosition().size());
                toPos = game.getGameBoard().getArrPosition().get(index);
                int x = this.randomNum.nextInt(this.getPlayersNumOfTokens());
                token = this.getMyTokens().get(x);
            } while(toPos.isContainToken());
        } else {
            do { // computer cannot fly yet, it chooses a random token to move to its adjacent position
                index = this.randomNum.nextInt(this.getPlayersNumOfTokens());
                token = this.getMyTokens().get(index);
                for (Position adjPos: token.getPosition().getAdjacentPos()) {
                    if (!adjPos.isContainToken()) {
                        toPos = adjPos;
                        break;
                    }
                }
            }while(toPos == null);
        }

        this.setHoldingToken(token);
        token.setPrevPosition(token.getPosition());
        int i = game.getGameBoard().getArrPosition().indexOf(toPos);
        Circle toToken = game.getArrCircleToken().get(i);
        MoveTokenAction mta = new MoveTokenAction(game, this, toToken,
                token.getCircleToken(), toPos, token);
        mta.execute();

    }

    /**
     * Computer player randomly removes opponent's token when it forms a mill
     * @param game
     **/
    public void removeRandomToken(Game game){
        Player opponent = game.getCurrentPlayer().getOpponent();
        ArrayList<Token> opponentTokens = opponent.getMyTokens();
        boolean removed = false;
        while(!removed){
            // choose opponent's random token to remove
            int index = this.randomNum.nextInt(opponentTokens.size());
            Token tokenToRemove = opponentTokens.get(index);
            RemoveTokenAction rta = new RemoveTokenAction(game, this, tokenToRemove.getPosition(), tokenToRemove);
            if (rta.checkValidRemove()){
                rta.execute();
                removed = true;
            }
        }
    }

}
