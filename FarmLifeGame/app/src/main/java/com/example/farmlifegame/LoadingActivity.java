package com.example.farmlifegame;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class LoadingActivity extends AppCompatActivity {

    private static final int SPLASH_TIMEOUT = 3000; // 3 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // Use a Handler to delay the transition to the main menu
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start the main menu activity
                Intent intent = new Intent(LoadingActivity.this, MainMenuActivity.class);
                startActivity(intent);

                // Close this activity
                finish();
            }
        }, SPLASH_TIMEOUT);
    }
}

