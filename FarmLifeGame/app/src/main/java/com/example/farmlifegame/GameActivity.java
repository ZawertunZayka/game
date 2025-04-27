package com.example.farmlifegame;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private GameView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create and set the GameView as the content view
        gameView = new GameView(this);
        setContentView(gameView);

        // Get loadSave flag from Intent (will be used later)
        // boolean loadSave = getIntent().getBooleanExtra("loadSave", false);
        // if (loadSave) {
        //     gameView.loadGame();
        // } else {
        //     gameView.newGame();
        // }
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}

