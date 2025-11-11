package com.example.tictactoe;

/**
 * GameLogic class handles all the game logic for Tic Tac Toe
 * including board state, move validation, win/draw detection, and game mode management
 */
public class GameLogic {
    // Game mode constants
    public static final int TWO_PLAYER = 0;
    public static final int SINGLE_PLAYER = 1;
    
    // Player constants
    public static final int EMPTY = 0;
    public static final int PLAYER_X = 1;
    public static final int PLAYER_O = 2;
    
    // Board representation (0-8 positions)
    private int[] board;
    
    // Current player (PLAYER_X or PLAYER_O)
    private int currentPlayer;
    
    // Game mode (TWO_PLAYER or SINGLE_PLAYER)
    private int gameMode;
    
    // Game state
    private boolean gameOver;
    private int winner;
    
    /**
     * Constructor - Initializes the game
     */
    public GameLogic() {
        board = new int[9];
        currentPlayer = PLAYER_X;
        gameMode = TWO_PLAYER;
        gameOver = false;
        winner = EMPTY;
        resetGame();
    }
    
    /**
     * Resets the game to initial state
     */
    public void resetGame() {
        for (int i = 0; i < 9; i++) {
            board[i] = EMPTY;
        }
        currentPlayer = PLAYER_X;
        gameOver = false;
        winner = EMPTY;
    }
    
    /**
     * Makes a move at the specified position
     * @param position The position (0-8) where the move is to be made
     * @return true if move was successful, false otherwise
     */
    public boolean makeMove(int position) {
        // Check if position is valid
        if (position < 0 || position > 8) {
            return false;
        }
        
        // Check if cell is already occupied
        if (isCellOccupied(position)) {
            return false;
        }
        
        // Check if game is over
        if (gameOver) {
            return false;
        }
        
        // Make the move
        board[position] = currentPlayer;
        
        // Check for winner or draw
        if (checkWinner()) {
            gameOver = true;
            winner = currentPlayer;
        } else if (isDraw()) {
            gameOver = true;
            winner = EMPTY;
        } else {
            // Switch player
            currentPlayer = (currentPlayer == PLAYER_X) ? PLAYER_O : PLAYER_X;
        }
        
        return true;
    }
    
    /**
     * Checks if a cell is occupied
     * @param position The position to check (0-8)
     * @return true if occupied, false otherwise
     */
    public boolean isCellOccupied(int position) {
        if (position < 0 || position > 8) {
            return true; // Invalid position is considered occupied
        }
        return board[position] != EMPTY;
    }
    
    /**
     * Checks if there is a winner
     * @return true if there is a winner, false otherwise
     */
    public boolean checkWinner() {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i * 3] != EMPTY &&
                board[i * 3] == board[i * 3 + 1] &&
                board[i * 3 + 1] == board[i * 3 + 2]) {
                return true;
            }
        }
        
        // Check columns
        for (int i = 0; i < 3; i++) {
            if (board[i] != EMPTY &&
                board[i] == board[i + 3] &&
                board[i + 3] == board[i + 6]) {
                return true;
            }
        }
        
        // Check diagonals
        if (board[0] != EMPTY && board[0] == board[4] && board[4] == board[8]) {
            return true;
        }
        if (board[2] != EMPTY && board[2] == board[4] && board[4] == board[6]) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Checks if the game is a draw
     * @return true if draw, false otherwise
     */
    public boolean isDraw() {
        if (checkWinner()) {
            return false;
        }
        
        // Check if all cells are filled
        for (int i = 0; i < 9; i++) {
            if (board[i] == EMPTY) {
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Gets the list of available moves (empty positions)
     * @return Array of available position indices
     */
    public int[] getAvailableMoves() {
        int count = 0;
        for (int i = 0; i < 9; i++) {
            if (board[i] == EMPTY) {
                count++;
            }
        }
        
        int[] availableMoves = new int[count];
        int index = 0;
        for (int i = 0; i < 9; i++) {
            if (board[i] == EMPTY) {
                availableMoves[index++] = i;
            }
        }
        
        return availableMoves;
    }
    
    /**
     * Sets the game mode
     * @param mode TWO_PLAYER or SINGLE_PLAYER
     */
    public void setGameMode(int mode) {
        this.gameMode = mode;
    }
    
    /**
     * Gets the current game mode
     * @return Current game mode
     */
    public int getGameMode() {
        return gameMode;
    }
    
    /**
     * Gets the current player
     * @return Current player (PLAYER_X or PLAYER_O)
     */
    public int getCurrentPlayer() {
        return currentPlayer;
    }
    
    /**
     * Gets the board state
     * @return Copy of the board array
     */
    public int[] getBoard() {
        return board.clone();
    }
    
    /**
     * Checks if the game is over
     * @return true if game is over, false otherwise
     */
    public boolean isGameOver() {
        return gameOver;
    }
    
    /**
     * Gets the winner
     * @return PLAYER_X, PLAYER_O, or EMPTY (for draw)
     */
    public int getWinner() {
        return winner;
    }
}

