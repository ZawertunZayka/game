package com.example.farmlifegame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class GameActivity extends AppCompatActivity {

    private static final String TAG = "GameActivity";
    private GameView gameView;

    // ActivityResultLauncher for ShopActivity
    private ActivityResultLauncher<Intent> shopActivityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate called");

        // Initialize the ActivityResultLauncher
        shopActivityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    Log.d(TAG, "ShopActivity result received. Code: " + result.getResultCode());
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.hasExtra(ShopActivity.EXTRA_PLAYER_DATA)) {
                            ShopActivity.PlayerData updatedPlayerData = (ShopActivity.PlayerData) data.getSerializableExtra(ShopActivity.EXTRA_PLAYER_DATA);
                            if (gameView != null && updatedPlayerData != null) {
                                Log.d(TAG, "Applying updated player data from shop.");
                                gameView.applyShopResult(updatedPlayerData);
                            } else {
                                Log.w(TAG, "GameView or updatedPlayerData is null, cannot apply shop result.");
                            }
                        } else {
                            Log.w(TAG, "ShopActivity result OK, but data or EXTRA_PLAYER_DATA is missing.");
                        }
                    } else {
                        Log.d(TAG, "ShopActivity cancelled or failed.");
                    }
                });

        // Create and set the GameView as the content view
        gameView = new GameView(this);
        // Pass the launcher to GameView so it can start the ShopActivity
        gameView.setShopLauncher(shopActivityResultLauncher);
        setContentView(gameView);

        // Get loadSave flag from Intent
        boolean loadSave = getIntent().getBooleanExtra("loadSave", false);

        // Initialize game based on flag
        if (loadSave) {
            Log.d(TAG, "Loading saved game...");
            gameView.loadGame();
        } else {
            Log.d(TAG, "Starting new game...");
            gameView.newGame();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause called");
        if (gameView != null) {
            gameView.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        if (gameView != null) {
            gameView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy called");
        // Clean up resources if needed
        super.onDestroy();
        gameView = null; // Release reference
    }
}

