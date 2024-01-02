package com.example.ninemanmorris_team10;

import com.example.ninemanmorris_team10.Player.Player;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Token class. Each player will have 9 tokens in this game.
 */
public class Token {

    private Circle circleToken;
    private Position prevPosition;
    private Position position;
    private boolean isMill;
    private Player belongsToPlayer;

    /**
     * Constructor of Token class.
     * @param position Position that the token is placed
     * @param player Player that the token belongs to
     * @param circleToken JavaFX Circle element that represents the token
     */
    public Token(Position position, Player player, Circle circleToken){
        this.setCircleToken(circleToken);
        this.setPosition(position);
        this.setBelongsToPlayer(player);
        this.setMill(false);
        this.setPrevPosition(null);
        this.setColor();
    }

    public Circle getCircleToken(){
        return circleToken;
    }

    public void setCircleToken(Circle circleToken){
        this.circleToken = circleToken;
    }

    public Position getPrevPosition() {
        return prevPosition;
    }

    public void setPrevPosition(Position prevPosition) {
        this.prevPosition = prevPosition;
    }

    public Position getPosition() {
        return position;
    }

    /**
     * When setting a position for the token, set its visibility too.
     * @param position Position to be set to the token
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isMill() {
        return isMill;
    }

    /**
     * Change token's stroke color when token belongs to a mill.
     * @param mill True if token belongs to a mill, else false
     */
    public void setMill(boolean mill) {
        isMill = mill;
        if (isMill){
            getCircleToken().setStroke(Color.RED);
        }
        else {
            getCircleToken().setStroke(Color.BLACK);
        }
    }

    public Player getBelongsToPlayer() {
        return belongsToPlayer;
    }

    public void setBelongsToPlayer(Player belongsToPlayer) {
        this.belongsToPlayer = belongsToPlayer;
    }

    /**
     * Set token's colour according to which player the token belongs to.
     */
    public void setColor() {
        this.circleToken.setFill(this.belongsToPlayer.getColor());
        this.circleToken.setStroke(Color.BLACK);
        this.circleToken.setStrokeWidth(3.0);
    }
}

