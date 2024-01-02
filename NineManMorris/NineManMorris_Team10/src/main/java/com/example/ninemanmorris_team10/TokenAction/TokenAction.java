package com.example.ninemanmorris_team10.TokenAction;

import com.example.ninemanmorris_team10.Game;
import com.example.ninemanmorris_team10.Player.Player;
import com.example.ninemanmorris_team10.Position;
import com.example.ninemanmorris_team10.Token;
import javafx.scene.control.Label;

/**
 * TokenAction is the base class for actions that players to place/move/remove their tokens
 */
public abstract class TokenAction {

    protected Game game;

    protected Player player;
    protected Position position;
    protected Token token;

    /**
     * Constructor for TokenAction.
     * @param game The game
     * @param player The player that is executing this action
     * @param position The position where token is placed/moved/removed
     * @param token The selected token
     */
    public TokenAction(Game game, Player player, Position position, Token token){
        this.setGame(game);
        this.setPlayer(player);
        this.setPosition(position);
        this.setToken(token);
    }

    /**
     * Executes a TokenAction.
     *
     * @return True if is a valid action, false otherwise
     */
    public abstract boolean execute();

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Token getToken() { return token; }

    public void setToken(Token token) {this.token = token;}
}

