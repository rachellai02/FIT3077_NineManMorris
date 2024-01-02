package com.example.ninemanmorris_team10;

import com.example.ninemanmorris_team10.Player.Player;

import java.util.*;

/**
 * Responsible for saving game state.
 */
public class GameState {
    private final String playerTurn;
    private final ArrayList<Map<String, Object>> boardState; // [pos1, pos2, pos3, pos4, ...]
    private final ArrayList<Map<String, Object>> playersState; // [player1, player2]

    /**
     * GameState constructor.
     * @param playerTurn current player turn
     * @param board current board state
     * @param player1 current player1 state
     * @param player2 current player2 state
     */
    public GameState(String playerTurn, Board board, Player player1, Player player2) {
        this.playerTurn = playerTurn;
        this.boardState = saveBoardState(board);
        this.playersState = savePlayersState(player1, player2);
    }

    /**
     * Constructor to recreate game states when uploading saved game.
     * @param playerTurn players turn
     * @param boardState the state of the board
     * @param playersState the state of the players
     */
    public GameState(String playerTurn, ArrayList<Map<String, Object>> boardState, ArrayList<Map<String, Object>> playersState){
        this.playerTurn = playerTurn;
        this.boardState = boardState;
        this.playersState = playersState;
    }

    /**
     * Save current Board state (ie. Position and Token information) in a list.
     *
     * Board information:
     * - Position: X,Y,containToken
     * - Token: isMill, belongsToPlayer
     *
     * newBoardState array schema:
     * [<'Position'=, 'posContainToken'= True, 'Token'=, 'TokenIsMill'=, 'tokenBelongsToPlayer'= >,
     *  <'Position'=, 'posContainToken'= False > ... ]
     *
     * @param board gameBoard
     *
     * @return current Board state
     */
    private ArrayList<Map<String, Object>> saveBoardState(Board board) {
        ArrayList<Map<String, Object>> newBoardState = new ArrayList<>();

        // Save each Position and Token (if any) information in a Map object
        for (Position pos: board.getArrPosition()) {
            Map<String, Object> positionState = new HashMap<>();

            positionState.put("Position", pos);
            positionState.put("posContainToken", pos.isContainToken());

            if (pos.isContainToken()) {
                Token token = pos.getToken();
                positionState.put("Token", token);
                positionState.put("tokenIsMill", token.isMill());
                positionState.put("tokenBelongsToPlayer", token.getBelongsToPlayer().getName());
            }

            newBoardState.add(positionState);
        }

        return newBoardState;
    }


    /**
     * Save current Players state in a list.
     *
     * Player information: name, color, numOfTokensPlaced, holdingToken, tokenList, capabilities
     *
     * newPlayerState array schema:
     * [<'Player'=, 'name'=, 'numOfTokensPlaced'=, 'capabilities'=, 'tokensList'=, 'holdingToken'=>,
     * <'Player'=, 'name'=, 'numOfTokensPlaced'=, 'capabilities'=, 'tokensList'=, 'holdingToken'= >]
     *
     * @param player1 player
     * @param player2 player
     *
     * @return current Player state
     */
    private ArrayList<Map<String, Object>> savePlayersState(Player player1, Player player2) {
        ArrayList<Map<String, Object>> newPlayersState = new ArrayList<>();

        // Save each Player information in a Map object
        for (Player player: Arrays.asList(player1, player2)) {
            Map<String, Object> playerState = new HashMap<>();

            playerState.put("Player", player);
            playerState.put("name", player.getName());
            playerState.put("numOfTokensPlaced", player.getNumOfTokensPlaced());
            playerState.put("capabilities", player.getCapability());
            playerState.put("holdingToken", player.getHoldingToken());

            newPlayersState.add(playerState);
        }

        return newPlayersState;
    }

    public ArrayList<Map<String, Object>> getBoardState() {
        return boardState;
    }

    public ArrayList<Map<String, Object>> getPlayersState() {
        return playersState;
    }

    public String getPlayerTurn() {
        return playerTurn;
    }

    /**
     * GameState : { playerTurn =, boardState = [{pos1}, {pos2}, {pos3}, ...], playerState = [{player1}, {player2}] }
     *
     * @return String describing game state
     */
    @Override
    public String toString() {
        return "GameState: {" +
                "playerTurn = " + playerTurn + "," +
                "boardState = " + boardState + "," +
                "playersState = " + playersState + "}";
    }

    public String toFile() {
        String finalOutput = "";
        finalOutput += playerTurn + "\n";
        for (int i = 0; i < boardState.size(); i++) { // Loop through array list to get dict
            String s = "";
            Map<String, Object> map = new HashMap<>();
            map = boardState.get(i);
            s += i + "|";
            s += map.get("posContainToken") + "|";
            s += map.get("tokenIsMill") + "|";
            s += map.get("tokenBelongsToPlayer");
            finalOutput += s + "\n";
        }

        for (int i = 0; i < playersState.size(); i++) {
            String s = "";
            Map<String, Object> map = new HashMap<>();
            map = playersState.get(i);
            s += map.get("name") + "|";
            s += map.get("numOfTokensPlaced") + "|";
            s += map.get("capabilities");
            finalOutput += s + "\n";
        }

        return finalOutput + "\n";


    }
}
