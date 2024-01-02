package com.example.ninemanmorris_team10.Player;

import com.example.ninemanmorris_team10.Enum.Capabilities;
import com.example.ninemanmorris_team10.Token;
import javafx.scene.paint.Color;

import java.util.*;

/**
 * An abstract Player class.
 */
public abstract class Player {

    protected String name;
    protected int numOfTokensPlaced;
    protected Color color;
    protected Token holdingToken;
    protected Player opponent;
    protected ArrayList<Token> myTokens = new ArrayList<>();
    protected Set<Object> capabilities = new HashSet<>();

    /**
     * Constructor for Player.
     * @param name Name of Player
     */
    public Player(String name){
        this.numOfTokensPlaced = 0;
        this.setName(name);
        this.addCapability(Capabilities.PLACE);
    }

    public String getName() { return this.name; }
    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return this.color;
    }
    public void setColor(Color color) {
        this.color = color;
    }

    public Token getHoldingToken() {
        return this.holdingToken;
    }
    public void setHoldingToken(Token holdingToken) {
        this.holdingToken = holdingToken;
    }

    public Player getOpponent() {
        return opponent;
    }

    public void setOpponent(Player opponent) {
        this.opponent = opponent;
    }

    public int getNumOfTokensPlaced() {
        return this.numOfTokensPlaced;
    }
    public void setNumOfTokensPlaced(int numOfTokensPlaced) {
        this.numOfTokensPlaced = numOfTokensPlaced;
    }

    public ArrayList<Token> getMyTokens() {
        return this.myTokens;
    }
    public void setMyTokens(ArrayList<Token> tokens) {
        this.myTokens = tokens;
    }

    /**
     * Gets the number of tokens that a player currently has.
     *
     * @return Number of tokens
     */
    public int getPlayersNumOfTokens() {
        return myTokens.size();
    }

    /**
     * Adds a token to the player's collection upon placing a token on the board
     * @param token Token
     */
    public void addTokenToList(Token token){
        myTokens.add(token);
        numOfTokensPlaced+=1;
    }

    /**
     * Removes a token from the player.
     * @param token Token
     */
    public void removeTokenFromList(Token token){
        myTokens.remove(token);
    }

    /**
     * Updates the status of a player based on game rules.
     */
    public void updateStatusEachTurn() {
        if (numOfTokensPlaced < 9){
            this.addCapability(Capabilities.PLACE);
        }
        else if (numOfTokensPlaced == 9) {
            this.addCapability(Capabilities.MOVE);
            this.removeCapability(Capabilities.PLACE);
        }
        if (this.hasCapability(Capabilities.MOVE) && myTokens.size() == 3){
            this.removeCapability(Capabilities.MOVE);
            this.addCapability(Capabilities.FLY);
        }
    }

    /**
     * Checks for the tokens that are not part of a mill.
     *
     * @return A list of tokens that are not part of a mill
     */
    public ArrayList<Token> getNonMills() {
        ArrayList<Token> tokens = new ArrayList<>();
        for (Token token : myTokens) {
            if (!token.isMill()) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    /**
     * Checks if a player has the capability
     * @param capability Player's capability
     */
    public boolean hasCapability(Enum<?> capability) {
        return capabilities.contains(capability);
    }

    /**
     * Add player's capability
     * @param capability Player's capability
     */
    public void addCapability(Enum<?> capability) {
        if (!this.hasCapability(capability)){
            capabilities.add(capability);
        }
    }

    /**
     * Remove player's capability
     * @param capability Player's capability
     */
    public void removeCapability(Enum<?> capability) {
        capabilities.remove(capability);
    }

    /**
     * Get player's current capability from capabilities list (used for undo and restore).
     * Get the capability that is not BLOCK_ACTION.
     *
     * @return a capability object
     */
    public Object getCapability() {
        Object cap=Capabilities.PLACE;
        for (Object capability: capabilities){
            if (capability != Capabilities.BLOCK_ACTION){
                cap=capability;
            }
        }
        return cap;
    }

    public Set<Object> getCapabilities() {
        return capabilities;
    }

    /**
     * Undo player state.
     * @param previousState player's previous state
     */
    public void undoPreviousState(Map<String, Object> previousState) {
        this.reset();
        this.setNumOfTokensPlaced((int) previousState.get("numOfTokensPlaced"));
        this.capabilities.add(previousState.get("capabilities"));
        if (this.getName()=="Computer"){
            this.capabilities.add(Capabilities.BLOCK_ACTION);
        }
    }

    public void reset() {
        this.setNumOfTokensPlaced(0);
        this.getMyTokens().clear();
        this.capabilities.clear();
    }

}

