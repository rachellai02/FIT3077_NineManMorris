package com.example.ninemanmorris_team10;

import com.example.ninemanmorris_team10.Enum.Capabilities;

import java.util.ArrayList;

/***
 * Responsible for managing the history of the game states and providing functionality to save and undo states.
 */
public class GameCaretaker {
    private GameState initialState;
    private ArrayList<GameState> history;
    private final Game game;

    /**
     * Constructor for GameCaretaker.
     * @param game the Game instance
     */
    public GameCaretaker(Game game) {
        this.history = new ArrayList<>();
        this.game = game;
    }

    /**
     * Save initial game state.
     */
    public void saveInitialState() {
        initialState = game.save();
    }

    /**
     * Save the current state of the game and add it to the history.
     */
    public void saveState() {
        GameState gameState = game.save();
        history.add(gameState);
    }

    /**
     * Undo the previous game state.
     *
     * If there is no move available,the game remains in the initial state.
     * Otherwise, revert to the previous state.
     *
     * Computer game mode remove history:
     *
     * c- computer, p- human player
     *
     * 1. Computer formed mill scenario:
     *
     *  current history stack:
     *  ----------------------
     *  | p move             |
     *  | c move             |
     *  | p move             |
     *  | c move & form mill |
     *  | c remove mill      |
     *  | p move 	         |
     *  | c move             |
     *  ----------------------
     * ====================================================================================
     * now it's p turn and p undo move (ie.execute GameCaretaker.undoState())
     *  ----------------------
     *  | p move             |
     *  | c move             |
     *  | p move             |
     *  | c move & form mill |
     *  | c remove mill      | <- restore this state
     *  | p move 	         | -> line 184 check p.capabilities != REMOVE, line 185 pop this
     *  | c move             | -> line 176 pop this
     *  ----------------------
     *
     * current history stack
     *  ----------------------
     *  | p move             |
     *  | c move             |
     *  | p move             |
     *  | c move & form mill |
     *  | c remove mill      |
     *  ----------------------
     * ====================================================================================
     * back to p turn and p undo
     *  ----------------------
     *  | p move             |
     *  | c move             | <- restore this state
     *  | p move             | -> line 184 check p.capabilities != REMOVE, line 185 pop this
     *  | c move & form mill | -> line 179 check c.capabilities == REMOVE, line 180 pop this
     *  | c remove mill      | -> line 176 pop this
     *  ----------------------
     *
     * current history stack
     *  ----------
     *  | p move |
     *  | c move |
     *  ----------
     * ====================================================================================
     * back to p turn again.
     * ====================================================================================
     *
     *
     *  2. Player formed mill scenario:
     *
     * current history stack:
     *  ----------------------
     *  | p move 	         |
     *  | c move             |
     *  | p move             |
     *  | c move             |
     *  | p move & form mill |
     *  | p remove mill      |
     *  | c move  	         |
     *  ----------------------
     * ==========================================================================
     * now it's p turn and p undo move (ie. execute GameCaretaker.undoState())
     *  ----------------------
     *  | p move             |
     *  | c move             |
     *  | p move 	         |
     *  | c move             |
     *  | p move & form mill | <- restore this state (so that p can remove c's token)
     *  | p remove mill      | -> line 184 check p.capabilities != REMOVE, line 185 pop this
     *  | c move  	         | -> line 176 pop this
     *  ----------------------
     *
     * current history stack
     *  ----------------------
     *  | p move             |
     *  | c move             |
     *  | p move 	         |
     *  | c move             |
     *  | p move & form mill |
     *  ----------------------
     * ================================================================================
     * back to p turn and p undo (p.capability == REMOVE)
     *  ----------------------
     *  | p move             |
     *  | c move             |
     *  | p move 	         |
     *  | c move             | -> line 184 check p.capability == REMOVE, line 185 doesn't pop this, restore this state
     *  | p move & form mill | -> line 176 pop this
     *  ----------------------
     *
     * current history stack
     *  ----------------------
     *  | p move             |
     *  | c move             |
     *  | p move 	         |
     *  | c move             |
     *  ----------------------
     * ================================================================================
     * back to p turn and p undo
     *  ----------------------
     *  | p move             |
     *  | c move             |
     *  | p move 	         | -> line 184 check p.capabilities != REMOVE, line 185 pop this
     *  | c move             | -> line 176 pop this
     *  ----------------------
     *
     * current history stack
     *  ----------
     *  | p move |
     *  | c move |
     *  ----------
     * =================================================================================
     * back to p turn again.
     * =================================================================================
     *
     */
    public void undoState() {
        GameState gameState;

        if ((game.isCompGameMode() && history.size() <= 2) || history.size() <= 1) {
            gameState = initialState;
            history.clear();
        } else {
            // remove current state
            history.remove(history.size()-1);

            // remove computer form mill move
            if (game.isCompGameMode() && history.get(history.size()-1).getPlayersState().get(1).get("capabilities") == Capabilities.REMOVE) {
                history.remove(history.size()-1);
            }

            // don't remove computer move (we have to restore this state)
            if (game.isCompGameMode() && !game.getCurrentPlayer().hasCapability(Capabilities.REMOVE)){
                history.remove(history.size()-1);
            }

            gameState = history.get(history.size()-1);
        }
        game.undo(gameState);
    }

    /**
     * Returns a string representation of the history of game states.
     *
     * @return a string representation of the history
     */
    @Override
    public String toString() {
        return "History{" + history + '}';
    }

    /**
     * Retrieves the history of game states.
     *
     * @return the ArrayList containing the game states history
     */
    public ArrayList<GameState> getHistory() {
        return history;
    }

    public void setHistory(ArrayList<GameState> history) {
        this.history = history;
    }

    public GameState getLatestHistory(){
        return history.get(history.size()-1);
    }
}
