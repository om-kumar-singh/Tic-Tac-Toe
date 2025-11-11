package com.tictactoe;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AIPlayer {
    public enum Difficulty {
        EASY, MEDIUM, HARD
    }
    
    private Difficulty difficulty;
    private int aiPlayer;
    private int humanPlayer;
    private Random random;
    
    public AIPlayer(Difficulty difficulty, int aiPlayer) {
        this.difficulty = difficulty;
        this.aiPlayer = aiPlayer;
        this.humanPlayer = (aiPlayer == GameLogic.PLAYER_X) ? GameLogic.PLAYER_O : GameLogic.PLAYER_X;
        this.random = new Random();
    }
    
    public int[] getMove(GameLogic gameLogic) {
        int[][] board = gameLogic.getBoard();
        
        switch (difficulty) {
            case EASY:
                return getEasyMove(board);
            case MEDIUM:
                return getMediumMove(board, gameLogic);
            case HARD:
                return getHardMove(board, gameLogic);
            default:
                return getEasyMove(board);
        }
    }
    
    private int[] getEasyMove(int[][] board) {
        List<int[]> availableMoves = getAvailableMoves(board);
        if (availableMoves.isEmpty()) {
            return null;
        }
        return availableMoves.get(random.nextInt(availableMoves.size()));
    }
    
    private int[] getMediumMove(int[][] board, GameLogic gameLogic) {
        // Check for winning move
        int[] winningMove = findWinningMove(board, aiPlayer);
        if (winningMove != null) {
            return winningMove;
        }
        
        // Check for blocking move
        int[] blockingMove = findWinningMove(board, humanPlayer);
        if (blockingMove != null) {
            return blockingMove;
        }
        
        // Otherwise random move
        return getEasyMove(board);
    }
    
    private int[] getHardMove(int[][] board, GameLogic gameLogic) {
        // Use minimax algorithm
        int[] bestMove = minimax(board, aiPlayer, true);
        return new int[]{bestMove[1], bestMove[2]};
    }
    
    private int[] findWinningMove(int[][] board, int player) {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == GameLogic.EMPTY) {
                    board[i][j] = player;
                    if (checkWin(board, i, j, player)) {
                        board[i][j] = GameLogic.EMPTY;
                        return new int[]{i, j};
                    }
                    board[i][j] = GameLogic.EMPTY;
                }
            }
        }
        return null;
    }
    
    private boolean checkWin(int[][] board, int row, int col, int player) {
        // Check row
        if (board[row][0] == player && board[row][1] == player && board[row][2] == player) {
            return true;
        }
        
        // Check column
        if (board[0][col] == player && board[1][col] == player && board[2][col] == player) {
            return true;
        }
        
        // Check main diagonal
        if (row == col && board[0][0] == player && board[1][1] == player && board[2][2] == player) {
            return true;
        }
        
        // Check anti-diagonal
        if (row + col == 2 && board[0][2] == player && board[1][1] == player && board[2][0] == player) {
            return true;
        }
        
        return false;
    }
    
    private int[] minimax(int[][] board, int player, boolean isMaximizing) {
        List<int[]> availableMoves = getAvailableMoves(board);
        
        // Check for terminal states
        int winner = checkWinner(board);
        if (winner == aiPlayer) {
            return new int[]{10, -1, -1};
        } else if (winner == humanPlayer) {
            return new int[]{-10, -1, -1};
        } else if (availableMoves.isEmpty()) {
            return new int[]{0, -1, -1};
        }
        
        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            int bestRow = -1;
            int bestCol = -1;
            
            for (int[] move : availableMoves) {
                board[move[0]][move[1]] = aiPlayer;
                int[] result = minimax(board, humanPlayer, false);
                board[move[0]][move[1]] = GameLogic.EMPTY;
                
                if (result[0] > bestScore) {
                    bestScore = result[0];
                    bestRow = move[0];
                    bestCol = move[1];
                }
            }
            
            return new int[]{bestScore, bestRow, bestCol};
        } else {
            int bestScore = Integer.MAX_VALUE;
            int bestRow = -1;
            int bestCol = -1;
            
            for (int[] move : availableMoves) {
                board[move[0]][move[1]] = humanPlayer;
                int[] result = minimax(board, aiPlayer, true);
                board[move[0]][move[1]] = GameLogic.EMPTY;
                
                if (result[0] < bestScore) {
                    bestScore = result[0];
                    bestRow = move[0];
                    bestCol = move[1];
                }
            }
            
            return new int[]{bestScore, bestRow, bestCol};
        }
    }
    
    private int checkWinner(int[][] board) {
        // Check rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] != GameLogic.EMPTY && 
                board[i][0] == board[i][1] && 
                board[i][1] == board[i][2]) {
                return board[i][0];
            }
        }
        
        // Check columns
        for (int j = 0; j < 3; j++) {
            if (board[0][j] != GameLogic.EMPTY && 
                board[0][j] == board[1][j] && 
                board[1][j] == board[2][j]) {
                return board[0][j];
            }
        }
        
        // Check main diagonal
        if (board[0][0] != GameLogic.EMPTY && 
            board[0][0] == board[1][1] && 
            board[1][1] == board[2][2]) {
            return board[0][0];
        }
        
        // Check anti-diagonal
        if (board[0][2] != GameLogic.EMPTY && 
            board[0][2] == board[1][1] && 
            board[1][1] == board[2][0]) {
            return board[0][2];
        }
        
        return GameLogic.EMPTY;
    }
    
    private List<int[]> getAvailableMoves(int[][] board) {
        List<int[]> moves = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == GameLogic.EMPTY) {
                    moves.add(new int[]{i, j});
                }
            }
        }
        return moves;
    }
    
    public void setDifficulty(Difficulty difficulty) {
        this.difficulty = difficulty;
    }
}

