package com.example.tictactoe;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

/**
 * MainActivity - Main activity for the Tic Tac Toe game
 * Handles UI interactions, game state management, and computer moves
 */
public class MainActivity extends AppCompatActivity {
    
    // UI Components
    private GridLayout gridLayout;
    private Button[] buttons;
    private TextView statusText;
    private Button resetButton;
    private RadioGroup modeRadioGroup;
    private RadioButton twoPlayerRadio;
    private RadioButton singlePlayerRadio;
    
    // Game logic
    private GameLogic gameLogic;
    private ComputerAI computerAI;
    
    // Handler for delaying computer moves
    private Handler handler;
    
    // Flag to prevent moves during computer's turn
    private boolean isComputerTurn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // Initialize game logic
        gameLogic = new GameLogic();
        computerAI = new ComputerAI();
        handler = new Handler();
        isComputerTurn = false;
        
        // Initialize UI components
        initializeViews();
        
        // Set up button click listeners
        setupButtonListeners();
        
        // Set up mode selection listener
        setupModeSelectionListener();
        
        // Set up reset button listener
        setupResetButtonListener();
        
        // Update UI
        updateUI();
    }
    
    /**
     * Initializes all UI views
     */
    private void initializeViews() {
        gridLayout = findViewById(R.id.gridLayout);
        statusText = findViewById(R.id.statusText);
        resetButton = findViewById(R.id.resetButton);
        modeRadioGroup = findViewById(R.id.modeRadioGroup);
        twoPlayerRadio = findViewById(R.id.twoPlayerRadio);
        singlePlayerRadio = findViewById(R.id.singlePlayerRadio);
        
        // Initialize button array
        buttons = new Button[9];
        for (int i = 0; i < 9; i++) {
            String buttonID = "button" + i;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            buttons[i] = findViewById(resID);
        }
    }
    
    /**
     * Sets up click listeners for all game buttons
     */
    private void setupButtonListeners() {
        for (int i = 0; i < 9; i++) {
            final int position = i;
            buttons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onButtonClick(position);
                }
            });
        }
    }
    
    /**
     * Handles button click events
     * @param position The position (0-8) of the clicked button
     */
    private void onButtonClick(int position) {
        // Prevent moves during computer's turn
        if (isComputerTurn) {
            return;
        }
        
        // Prevent moves if game is over
        if (gameLogic.isGameOver()) {
            return;
        }
        
        // Make the move
        if (gameLogic.makeMove(position)) {
            updateUI();
            
            // Check if game is over
            if (gameLogic.isGameOver()) {
                handleGameEnd();
            } else {
                // If single player mode and it's computer's turn, make computer move
                if (gameLogic.getGameMode() == GameLogic.SINGLE_PLAYER &&
                    gameLogic.getCurrentPlayer() == GameLogic.PLAYER_O) {
                    makeComputerMove();
                }
            }
        }
    }
    
    /**
     * Makes the computer's move in single-player mode
     */
    private void makeComputerMove() {
        isComputerTurn = true;
        disableAllButtons();
        
        // Delay computer move for better UX
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int[] board = gameLogic.getBoard();
                int computerMove = computerAI.getBestMove(
                    board,
                    GameLogic.PLAYER_O,
                    GameLogic.PLAYER_X
                );
                
                if (computerMove != -1 && gameLogic.makeMove(computerMove)) {
                    updateUI();
                    
                    // Check if game is over
                    if (gameLogic.isGameOver()) {
                        handleGameEnd();
                    }
                }
                
                isComputerTurn = false;
                enableAllButtons();
            }
        }, 500); // 500ms delay
    }
    
    /**
     * Handles the end of the game
     */
    private void handleGameEnd() {
        disableAllButtons();
        
        int winner = gameLogic.getWinner();
        String message;
        
        if (winner == GameLogic.EMPTY) {
            message = "It's a Draw!";
        } else if (winner == GameLogic.PLAYER_X) {
            message = "Player X Wins!";
        } else {
            message = "Player O Wins!";
        }
        
        statusText.setText(message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    
    /**
     * Sets up the mode selection listener
     */
    private void setupModeSelectionListener() {
        modeRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.twoPlayerRadio) {
                    gameLogic.setGameMode(GameLogic.TWO_PLAYER);
                } else if (checkedId == R.id.singlePlayerRadio) {
                    gameLogic.setGameMode(GameLogic.SINGLE_PLAYER);
                }
                
                // Reset game when mode changes
                resetGame();
            }
        });
    }
    
    /**
     * Sets up the reset button listener
     */
    private void setupResetButtonListener() {
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetGame();
            }
        });
    }
    
    /**
     * Resets the game to initial state
     */
    private void resetGame() {
        // Cancel any pending computer moves
        handler.removeCallbacksAndMessages(null);
        isComputerTurn = false;
        
        // Reset game logic
        gameLogic.resetGame();
        
        // Update UI
        updateUI();
        enableAllButtons();
    }
    
    /**
     * Updates the UI to reflect current game state
     */
    private void updateUI() {
        // Update button texts
        int[] board = gameLogic.getBoard();
        for (int i = 0; i < 9; i++) {
            if (board[i] == GameLogic.PLAYER_X) {
                buttons[i].setText("X");
                buttons[i].setTextColor(ContextCompat.getColor(this, android.R.color.holo_blue_dark));
            } else if (board[i] == GameLogic.PLAYER_O) {
                buttons[i].setText("O");
                buttons[i].setTextColor(ContextCompat.getColor(this, android.R.color.holo_red_dark));
            } else {
                buttons[i].setText("");
            }
        }
        
        // Update status text
        if (!gameLogic.isGameOver()) {
            int currentPlayer = gameLogic.getCurrentPlayer();
            if (currentPlayer == GameLogic.PLAYER_X) {
                statusText.setText("Player X's Turn");
            } else {
                if (gameLogic.getGameMode() == GameLogic.SINGLE_PLAYER) {
                    statusText.setText("Computer's Turn");
                } else {
                    statusText.setText("Player O's Turn");
                }
            }
        }
        
        // Update button states
        if (!isComputerTurn) {
            enableAllButtons();
        }
    }
    
    /**
     * Disables all game buttons
     */
    private void disableAllButtons() {
        for (Button button : buttons) {
            button.setEnabled(false);
        }
    }
    
    /**
     * Enables all game buttons (if not occupied)
     */
    private void enableAllButtons() {
        int[] board = gameLogic.getBoard();
        for (int i = 0; i < 9; i++) {
            buttons[i].setEnabled(board[i] == GameLogic.EMPTY && !gameLogic.isGameOver());
        }
    }
}

