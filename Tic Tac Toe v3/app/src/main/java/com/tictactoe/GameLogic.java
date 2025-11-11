package com.tictactoe;

public class GameLogic {
    public static final int EMPTY = 0;
    public static final int PLAYER_X = 1;
    public static final int PLAYER_O = 2;
    
    private int[][] board;
    private int currentPlayer;
    private boolean gameOver;
    private int winner;
    
    public GameLogic() {
        board = new int[3][3];
        currentPlayer = PLAYER_X;
        gameOver = false;
        winner = EMPTY;
        resetBoard();
    }
    
    public void resetBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = EMPTY;
            }
        }
        currentPlayer = PLAYER_X;
        gameOver = false;
        winner = EMPTY;
    }
    
    public boolean makeMove(int row, int col) {
        if (gameOver || board[row][col] != EMPTY) {
            return false;
        }
        
        board[row][col] = currentPlayer;
        
        if (checkWin(row, col)) {
            gameOver = true;
            winner = currentPlayer;
            return true;
        }
        
        if (checkDraw()) {
            gameOver = true;
            winner = EMPTY;
            return true;
        }
        
        switchPlayer();
        return true;
    }
    
    private boolean checkWin(int row, int col) {
        int player = board[row][col];
        
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
    
    private boolean checkDraw() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == EMPTY) {
                    return false;
                }
            }
        }
        return true;
    }
    
    private void switchPlayer() {
        currentPlayer = (currentPlayer == PLAYER_X) ? PLAYER_O : PLAYER_X;
    }
    
    public int getCurrentPlayer() {
        return currentPlayer;
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public int getWinner() {
        return winner;
    }
    
    public int[][] getBoard() {
        return board;
    }
    
    public int getCell(int row, int col) {
        return board[row][col];
    }
    
    public void setCell(int row, int col, int player) {
        board[row][col] = player;
    }
    
    public boolean isValidMove(int row, int col) {
        return !gameOver && row >= 0 && row < 3 && col >= 0 && col < 3 && board[row][col] == EMPTY;
    }
    
    public void setCurrentPlayer(int player) {
        currentPlayer = player;
    }
}

