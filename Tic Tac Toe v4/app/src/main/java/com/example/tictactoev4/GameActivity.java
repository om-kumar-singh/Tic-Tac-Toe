package com.example.tictactoev4;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.view.View;
import android.content.Intent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    // UI Elements
    TextView statusText;
    Button resetButton, backButton;
    Button[] buttons = new Button[9];

    // Game state variables
    int[] board = new int[9]; // 0 = empty, 1 = X, 2 = O
    int currentPlayer = 1;    // Player X starts
    boolean gameActive = true;
    String mode;              // "SINGLE" or "TWO"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        // Load game mode from intent
        Intent intent = getIntent();
        mode = intent.getStringExtra("MODE");

        // Connect UI elements
        statusText = findViewById(R.id.statusText);
        resetButton = findViewById(R.id.resetButton);
        backButton = findViewById(R.id.backButton);

        // Initialize board buttons
        for (int i = 0; i < 9; i++) {
            String buttonID = "button" + i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            buttons[i] = findViewById(resID);

            final int index = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onCellClicked(index);
                }
            });
        }

        // Restart game
        resetButton.setOnClickListener(v -> resetGame());

        // Go back to mode selection screen
        backButton.setOnClickListener(v -> {
            finish(); // Closes GameActivity and returns to MainActivity
        });
    }

    // When a player clicks a cell
    private void onCellClicked(int index) {
        if (!gameActive || board[index] != 0) {
            return;
        }

        // Mark the move
        board[index] = currentPlayer;
        buttons[index].setText(currentPlayer == 1 ? "X" : "O");

        // Check for winner
        if (checkWinner()) {
            statusText.setText((currentPlayer == 1 ? "Player X" :
                    (mode.equals("SINGLE") ? "Computer" : "Player O")) + " Wins!");
            gameActive = false;
            return;
        }

        // Check for draw
        if (isDraw()) {
            statusText.setText("It's a Draw!");
            gameActive = false;
            return;
        }

        // Switch player
        currentPlayer = (currentPlayer == 1) ? 2 : 1;

        // Update status text
        if (mode.equals("SINGLE")) {
            if (currentPlayer == 2) {
                statusText.setText("Computer's Turn...");
                computerMove();
            } else {
                statusText.setText("Your Turn (X)");
            }
        } else {
            statusText.setText("Player " + (currentPlayer == 1 ? "X" : "O") + "'s Turn");
        }
    }

    // Simple AI for single-player mode
    private void computerMove() {
        if (!gameActive) return;

        List<Integer> available = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            if (board[i] == 0) available.add(i);
        }

        if (!available.isEmpty()) {
            // Delay slightly to make it feel natural
            resetButton.postDelayed(() -> {
                int choice = available.get(new Random().nextInt(available.size()));
                buttons[choice].performClick();
            }, 400);
        }
    }

    // Check all winning combinations
    private boolean checkWinner() {
        int[][] winPositions = {
                {0, 1, 2}, {3, 4, 5}, {6, 7, 8},
                {0, 3, 6}, {1, 4, 7}, {2, 5, 8},
                {0, 4, 8}, {2, 4, 6}
        };

        for (int[] pos : winPositions) {
            if (board[pos[0]] != 0 &&
                    board[pos[0]] == board[pos[1]] &&
                    board[pos[1]] == board[pos[2]]) {
                return true;
            }
        }
        return false;
    }

    // Check if all cells are filled and no winner
    private boolean isDraw() {
        for (int cell : board) {
            if (cell == 0) return false;
        }
        return true;
    }

    // Reset the game board
    private void resetGame() {
        for (int i = 0; i < 9; i++) {
            board[i] = 0;
            buttons[i].setText("");
        }
        currentPlayer = 1;
        gameActive = true;

        if (mode.equals("SINGLE")) {
            statusText.setText("Your Turn (X)");
        } else {
            statusText.setText("Player X's Turn");
        }
    }
}
