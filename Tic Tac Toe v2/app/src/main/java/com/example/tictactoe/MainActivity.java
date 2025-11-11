package com.example.tictactoe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    // Game state constants
    private static final int PLAYER_X = 0;
    private static final int PLAYER_O = 1;
    private static final int EMPTY = 2;
    private static final int BOARD_SIZE = 9;

    // Game state variables
    private boolean gameActive = true;
    private int activePlayer = PLAYER_X;
    private int[] gameState = {EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY, EMPTY};
    private int moveCount = 0;

    // Winning positions (rows, columns, diagonals)
    private static final int[][] WINNING_POSITIONS = {
            {0, 1, 2}, // Top row
            {3, 4, 5}, // Middle row
            {6, 7, 8}, // Bottom row
            {0, 3, 6}, // Left column
            {1, 4, 7}, // Middle column
            {2, 5, 8}, // Right column
            {0, 4, 8}, // Diagonal top-left to bottom-right
            {2, 4, 6}  // Diagonal top-right to bottom-left
    };

    // UI Components
    private TextView statusTextView;
    private Button resetButton;
    private ImageView[] gameCells = new ImageView[BOARD_SIZE];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        initializeViews();

        // Set up window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set up reset button click listener
        resetButton.setOnClickListener(v -> resetGame());
    }

    /**
     * Initialize all UI components
     */
    private void initializeViews() {
        statusTextView = findViewById(R.id.status);
        resetButton = findViewById(R.id.resetButton);

        // Initialize game cell ImageViews
        gameCells[0] = findViewById(R.id.imageView1);
        gameCells[1] = findViewById(R.id.imageView2);
        gameCells[2] = findViewById(R.id.imageView3);
        gameCells[3] = findViewById(R.id.imageView4);
        gameCells[4] = findViewById(R.id.imageView5);
        gameCells[5] = findViewById(R.id.imageView6);
        gameCells[6] = findViewById(R.id.imageView7);
        gameCells[7] = findViewById(R.id.imageView8);
        gameCells[8] = findViewById(R.id.imageView9);
    }

    /**
     * Handle player tap on a game cell
     */
    public void playerTap(View view) {
        ImageView img = (ImageView) view;
        int tappedPosition = Integer.parseInt(img.getTag().toString());

        // If game is not active, reset on tap
        if (!gameActive) {
            resetGame();
            return;
        }

        // Check if cell is empty
        if (gameState[tappedPosition] == EMPTY) {
            // Update game state
            gameState[tappedPosition] = activePlayer;
            moveCount++;

            // Animate the move
            img.setTranslationY(-1000f);
            if (activePlayer == PLAYER_X) {
                img.setImageResource(R.drawable.cross);
                updateStatus("O's Turn - Tap to Play");
            } else {
                img.setImageResource(R.drawable.circle);
                updateStatus("X's Turn - Tap to Play");
            }
            img.animate().translationYBy(1000f).setDuration(300);

            // Check for win or draw
            if (checkForWin()) {
                gameActive = false;
                String winner = (activePlayer == PLAYER_X) ? "X" : "O";
                updateStatus(winner + " has Won! 🎉");
            } else if (moveCount == BOARD_SIZE) {
                // Draw condition
                gameActive = false;
                updateStatus("Game Draw! 🤝");
            } else {
                // Switch player
                activePlayer = (activePlayer == PLAYER_X) ? PLAYER_O : PLAYER_X;
            }
        }
    }

    /**
     * Check if the current player has won
     */
    private boolean checkForWin() {
        for (int[] winPosition : WINNING_POSITIONS) {
            if (gameState[winPosition[0]] == activePlayer &&
                    gameState[winPosition[1]] == activePlayer &&
                    gameState[winPosition[2]] == activePlayer) {
                return true;
            }
        }
        return false;
    }

    /**
     * Reset the game to initial state
     */
    private void resetGame() {
        gameActive = true;
        activePlayer = PLAYER_X;
        moveCount = 0;

        // Reset game state array
        for (int i = 0; i < BOARD_SIZE; i++) {
            gameState[i] = EMPTY;
        }

        // Clear all cell images
        for (ImageView cell : gameCells) {
            cell.setImageResource(0);
        }

        // Reset status text
        updateStatus("X's Turn - Tap to Play");
    }

    /**
     * Update the status text view
     */
    private void updateStatus(String message) {
        statusTextView.setText(message);
    }
}

