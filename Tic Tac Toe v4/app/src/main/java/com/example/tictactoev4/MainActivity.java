package com.example.tictactoev4;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button singlePlayerButton, twoPlayerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Link UI elements
        singlePlayerButton = findViewById(R.id.singlePlayerButton);
        twoPlayerButton = findViewById(R.id.twoPlayerButton);

        // When Single Player is chosen
        singlePlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("MODE", "SINGLE");
                startActivity(intent);
            }
        });

        // When Two Player is chosen
        twoPlayerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GameActivity.class);
                intent.putExtra("MODE", "TWO");
                startActivity(intent);
            }
        });
    }
}