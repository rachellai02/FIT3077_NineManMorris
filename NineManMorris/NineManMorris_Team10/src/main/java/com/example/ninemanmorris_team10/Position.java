package com.example.ninemanmorris_team10;

import com.example.ninemanmorris_team10.Player.Player;
import javafx.scene.shape.Circle;

import java.util.ArrayList;


/**
 * Represents a position on the board where a token may be placed.
 */
public class Position {
    private final double X, Y;
    private final ArrayList<Position> adjacentPos;
    private ArrayList<Mill> millList;
    private boolean containToken = false;
    private Token token;

    /**
     * Constructor of Position class.
     * @param circle JavaFX circle element that represents each position on the game board ui
     */
    public Position(Circle circle){
        this.X = circle.getLayoutX();
        this.Y = circle.getLayoutY();
        this.adjacentPos = new ArrayList<>();
        this.millList = new ArrayList<>();
    }

    public double getX() {
        return X;
    }

    public double getY() {
        return Y;
    }

    public Token getToken(){
        return token;
    }

    public void setToken(Token token){
        this.token = token;
    }

    /**
     * Method to add an adjacent position into the adjacent position list of each position.
     * @param adjPos Adjacent position of the position
     */
    public void addAdjacentPos(Position adjPos) { this.adjacentPos.add(adjPos);}

    public void addMillList(Mill mill){
        this.millList.add(mill);
    }

    public ArrayList<Mill> getMillList() {
        return millList;
    }

    public ArrayList<Position> getAdjacentPos() {
        return this.adjacentPos;
    }

    public boolean isContainToken(){
        return this.containToken;
    }

    public void setContainToken(boolean bool){
        this.containToken = bool;
    }

    /**
     * Put a token on this position.
     * @param token Token to be put on this position
     */
    public void putToken(Token token){
        this.setToken(token);
        this.setContainToken(true);
        token.setColor();
        token.getCircleToken().setVisible(true);
    }

    /**
     * Remove a token from this position.
     */
    public void removeToken(){
        if (this.isContainToken()) {
            this.getToken().getCircleToken().setVisible(false);
        }
        this.setContainToken(false);
        this.setToken(null);
    }

    /**
     * Check whether the position is a mill.
     * @param currentPlayer Current player that is having his turn
     *
     * @return True if the position is a mill, else false
     */
    public boolean checkMill(Player currentPlayer){
        // millPos of position 0 = [Mill1,Mill2]
        // Mill1 = [1,2], Mill2 = [9,21]]
        boolean formedMill = false;
        for (int i=0; i<millList.size(); i++){
            if (this.isContainToken() &&
                    millList.get(i).getMill().get(0).isContainToken() &&
                    millList.get(i).getMill().get(1).isContainToken() ){
                if (currentPlayer == this.getToken().getBelongsToPlayer() &&
                        currentPlayer == millList.get(i).getMill().get(0).getToken().getBelongsToPlayer() &&
                        currentPlayer == millList.get(i).getMill().get(1).getToken().getBelongsToPlayer()){

                    // a mill is formed after token is placed/moved to this position
                    getToken().setMill(true);
                    millList.get(i).getMill().get(0).getToken().setMill(true);
                    millList.get(i).getMill().get(1).getToken().setMill(true);
                    formedMill = true;
                }
            }
        }
        return formedMill;
    }
}

