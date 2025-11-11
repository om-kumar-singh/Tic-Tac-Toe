package com.tictactoe;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;

public class MainActivity extends AppCompatActivity {

    private final int[] cellIds = {
            R.id.cell00, R.id.cell01, R.id.cell02,
            R.id.cell10, R.id.cell11, R.id.cell12,
            R.id.cell20, R.id.cell21, R.id.cell22
    };

    private GameLogic gameLogic;
    private AIPlayer aiPlayer;
    private MaterialButton[][] cells;
    private View welcomeContainer;
    private View boardCard;
    private TextView turnIndicator;
    private MaterialButton selectModeButton;
    private MaterialButton resetButton;
    private MaterialButton changeModeButton;

    private boolean isTwoPlayerMode = true;
    private AIPlayer.Difficulty currentDifficulty = AIPlayer.Difficulty.MEDIUM;
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();
        initializeGame();
        setupClickListeners();
        showWelcomeState();
    }

    private void initializeViews() {
        cells = new MaterialButton[3][3];
        for (int i = 0; i < 9; i++) {
            int row = i / 3;
            int col = i % 3;
            cells[row][col] = findViewById(cellIds[i]);
        }

        welcomeContainer = findViewById(R.id.welcomeContainer);
        boardCard = findViewById(R.id.boardCard);
        turnIndicator = findViewById(R.id.turnIndicator);
        selectModeButton = findViewById(R.id.selectModeButton);
        resetButton = findViewById(R.id.resetButton);
        changeModeButton = findViewById(R.id.changeModeButton);
    }

    private void initializeGame() {
        gameLogic = new GameLogic();
        updateTurnIndicator();
    }

    private void setupClickListeners() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                final int row = i;
                final int col = j;
                cells[i][j].setOnClickListener(v -> handleCellClick(row, col));
            }
        }

        selectModeButton.setOnClickListener(v -> showGameModeDialog());
        changeModeButton.setOnClickListener(v -> showGameModeDialog());
        resetButton.setOnClickListener(v -> resetBoardState());
    }

    private void handleCellClick(int row, int col) {
        if (gameLogic.isGameOver()) {
            return;
        }

        if (!isTwoPlayerMode && gameLogic.getCurrentPlayer() != GameLogic.PLAYER_X) {
            return;
        }

        if (gameLogic.makeMove(row, col)) {
            updateCell(row, col);
            checkGameState();

            if (!isTwoPlayerMode && !gameLogic.isGameOver() &&
                    gameLogic.getCurrentPlayer() == GameLogic.PLAYER_O) {
                mainHandler.postDelayed(this::makeAIMove, 500);
            }
        }
    }

    private void makeAIMove() {
        if (gameLogic.isGameOver() || aiPlayer == null) {
            return;
        }

        int[] move = aiPlayer.getMove(gameLogic);
        if (move != null && gameLogic.isValidMove(move[0], move[1])) {
            gameLogic.makeMove(move[0], move[1]);
            updateCell(move[0], move[1]);
            checkGameState();
        }
    }

    private void updateCell(int row, int col) {
        MaterialButton cell = cells[row][col];
        int player = gameLogic.getCell(row, col);

        if (player == GameLogic.PLAYER_X) {
            cell.setText("X");
            cell.setTextColor(ContextCompat.getColor(this, R.color.x_color));
            cell.setEnabled(false);
        } else if (player == GameLogic.PLAYER_O) {
            cell.setText("O");
            cell.setTextColor(ContextCompat.getColor(this, R.color.o_color));
            cell.setEnabled(false);
        }
    }

    private void checkGameState() {
        if (gameLogic.isGameOver()) {
            disableAllCells();
            int winner = gameLogic.getWinner();

            String message;
            if (winner == GameLogic.PLAYER_X) {
                message = getString(R.string.player_x_wins);
            } else if (winner == GameLogic.PLAYER_O) {
                message = getString(R.string.player_o_wins);
            } else {
                message = getString(R.string.draw);
            }

            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            mainHandler.postDelayed(this::resetBoardState, 800);
        } else {
            updateTurnIndicator();
        }
    }

    private void showGameModeDialog() {
        String[] modes = {getString(R.string.two_player), getString(R.string.single_player)};
        int selectedIndex = isTwoPlayerMode ? 0 : 1;

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.select_game_mode))
                .setSingleChoiceItems(modes, selectedIndex, (dialog, which) -> {
                    if (which == 0) {
                        dialog.dismiss();
                        startTwoPlayerGame();
                    } else {
                        dialog.dismiss();
                        showDifficultyDialog();
                    }
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    if (!isGameActive()) {
                        showWelcomeState();
                    }
                })
                .show();
    }

    private void showDifficultyDialog() {
        String[] difficulties = {
                getString(R.string.easy),
                getString(R.string.medium),
                getString(R.string.hard)
        };

        int selectedIndex;
        switch (currentDifficulty) {
            case EASY:
                selectedIndex = 0;
                break;
            case HARD:
                selectedIndex = 2;
                break;
            case MEDIUM:
            default:
                selectedIndex = 1;
                break;
        }

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.select_difficulty))
                .setSingleChoiceItems(difficulties, selectedIndex, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            currentDifficulty = AIPlayer.Difficulty.EASY;
                            break;
                        case 2:
                            currentDifficulty = AIPlayer.Difficulty.HARD;
                            break;
                        case 1:
                        default:
                            currentDifficulty = AIPlayer.Difficulty.MEDIUM;
                            break;
                    }
                    isTwoPlayerMode = false;
                    aiPlayer = new AIPlayer(currentDifficulty, GameLogic.PLAYER_O);
                    dialog.dismiss();
                    startGame();
                })
                .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
                    if (!isGameActive()) {
                        showWelcomeState();
                    }
                })
                .show();
    }

    private void startTwoPlayerGame() {
        isTwoPlayerMode = true;
        aiPlayer = null;
        startGame();
    }

    private void startGame() {
        showGameUI();
        resetBoardState();
    }

    private void resetBoardState() {
        gameLogic.resetBoard();
        clearBoard();
        enableAllCells();
        updateTurnIndicator();

        if (!isTwoPlayerMode) {
            if (aiPlayer == null) {
                aiPlayer = new AIPlayer(currentDifficulty, GameLogic.PLAYER_O);
            }
        }
    }

    private void showWelcomeState() {
        welcomeContainer.setVisibility(View.VISIBLE);
        turnIndicator.setVisibility(View.GONE);
        boardCard.setVisibility(View.GONE);
        resetButton.setVisibility(View.GONE);
        changeModeButton.setVisibility(View.GONE);
        clearBoard();
    }

    private void showGameUI() {
        welcomeContainer.setVisibility(View.GONE);
        turnIndicator.setVisibility(View.VISIBLE);
        boardCard.setVisibility(View.VISIBLE);
        resetButton.setVisibility(View.VISIBLE);
        changeModeButton.setVisibility(View.VISIBLE);
    }

    private boolean isGameActive() {
        return boardCard.getVisibility() == View.VISIBLE;
    }

    private void clearBoard() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j].setText("");
                cells[i][j].setEnabled(true);
            }
        }
    }

    private void enableAllCells() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j].setEnabled(true);
            }
        }
    }

    private void disableAllCells() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                cells[i][j].setEnabled(false);
            }
        }
    }

    private void updateTurnIndicator() {
        int currentPlayer = gameLogic.getCurrentPlayer();
        String playerName = (currentPlayer == GameLogic.PLAYER_X) ?
                getString(R.string.player_x) : getString(R.string.player_o);
        turnIndicator.setText(getString(R.string.turn, playerName));
    }
}

