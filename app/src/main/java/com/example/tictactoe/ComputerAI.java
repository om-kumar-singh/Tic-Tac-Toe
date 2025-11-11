package com.example.tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * ComputerAI class handles the computer's move logic
 * Implements a strategic AI that tries to win, block, or make optimal moves
 */
public class ComputerAI {
    private Random random;
    
    /**
     * Constructor
     */
    public ComputerAI() {
        random = new Random();
    }
    
    /**
     * Gets the best move for the computer
     * Strategy priority:
     * 1. Check if computer can win (make winning move)
     * 2. Block human from winning
     * 3. Take center if available
     * 4. Take corner if available
     * 5. Take any available move
     * 
     * @param board Current board state
     * @param computerPlayer PLAYER_X or PLAYER_O (the computer's player)
     * @param humanPlayer PLAYER_X or PLAYER_O (the human's player)
     * @return Position (0-8) for computer's move
     */
    public int getBestMove(int[] board, int computerPlayer, int humanPlayer) {
        // Get available moves
        List<Integer> availableMoves = getAvailableMoves(board);
        
        if (availableMoves.isEmpty()) {
            return -1; // No moves available
        }
        
        // Strategy 1: Check if computer can win
        int winningMove = findWinningMove(board, computerPlayer);
        if (winningMove != -1) {
            return winningMove;
        }
        
        // Strategy 2: Block human from winning
        int blockingMove = findWinningMove(board, humanPlayer);
        if (blockingMove != -1) {
            return blockingMove;
        }
        
        // Strategy 3: Take center if available
        if (board[4] == GameLogic.EMPTY) {
            return 4;
        }
        
        // Strategy 4: Take a corner if available
        int[] corners = {0, 2, 6, 8};
        List<Integer> availableCorners = new ArrayList<>();
        for (int corner : corners) {
            if (board[corner] == GameLogic.EMPTY) {
                availableCorners.add(corner);
            }
        }
        if (!availableCorners.isEmpty()) {
            return availableCorners.get(random.nextInt(availableCorners.size()));
        }
        
        // Strategy 5: Take any available move
        return availableMoves.get(random.nextInt(availableMoves.size()));
    }
    
    /**
     * Finds a winning move for the given player
     * @param board Current board state
     * @param player The player to check for (PLAYER_X or PLAYER_O)
     * @return Position of winning move, or -1 if no winning move exists
     */
    private int findWinningMove(int[] board, int player) {
        // Check each available position
        for (int i = 0; i < 9; i++) {
            if (board[i] == GameLogic.EMPTY) {
                // Try the move
                board[i] = player;
                
                // Check if this move results in a win
                if (isWinningMove(board, player)) {
                    board[i] = GameLogic.EMPTY; // Undo the move
                    return i;
                }
                
                // Undo the move
                board[i] = GameLogic.EMPTY;
            }
        }
        
        return -1; // No winning move found
    }
    
    /**
     * Checks if the current board state is a winning position for the given player
     * @param board Current board state
     * @param player The player to check for
     * @return true if winning position, false otherwise
     */
    private boolean isWinningMove(int[] board, int player) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i * 3] == player &&
                board[i * 3 + 1] == player &&
                board[i * 3 + 2] == player) {
                return true;
            }
        }
        
        // Check columns
        for (int i = 0; i < 3; i++) {
            if (board[i] == player &&
                board[i + 3] == player &&
                board[i + 6] == player) {
                return true;
            }
        }
        
        // Check diagonals
        if (board[0] == player && board[4] == player && board[8] == player) {
            return true;
        }
        if (board[2] == player && board[4] == player && board[6] == player) {
            return true;
        }
        
        return false;
    }
    
    /**
     * Gets list of available moves (empty positions)
     * @param board Current board state
     * @return List of available position indices
     */
    private List<Integer> getAvailableMoves(int[] board) {
        List<Integer> moves = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (board[i] == GameLogic.EMPTY) {
                moves.add(i);
            }
        }
        return moves;
    }
}

