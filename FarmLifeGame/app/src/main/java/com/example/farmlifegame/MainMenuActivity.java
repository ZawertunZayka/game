package com.example.farmlifegame;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast; // Import Toast for placeholder messages
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button playButton = findViewById(R.id.playButton);
        Button settingsButton = findViewById(R.id.settingsButton);
        Button aboutButton = findViewById(R.id.aboutButton);
        Button exitButton = findViewById(R.id.exitButton);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewLoadGameDialog();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement SettingsActivity
                Toast.makeText(MainMenuActivity.this, "Settings not implemented yet.", Toast.LENGTH_SHORT).show();
                // Intent intent = new Intent(MainMenuActivity.this, SettingsActivity.class);
                // startActivity(intent);
            }
        });

        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 showAboutDialog();
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity(); // Closes all activities and exits the app
            }
        });
    }

    private void showNewLoadGameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.play_game_title) // "Играть"
               .setItems(new CharSequence[]{getString(R.string.new_game), getString(R.string.load_game)}, new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int which) {
                       // The 'which' argument contains the index position of the selected item
                       Intent intent = new Intent(MainMenuActivity.this, GameActivity.class);
                       if (which == 0) { // New Game
                           // Start a new game - Intent to GameActivity
                           intent.putExtra("loadSave", false); // Indicate new game
                           startActivity(intent);
                       } else { // Load Game
                           // TODO: Load a saved game - Intent to GameActivity or Save Slot Selection
                           // For now, just start a new game as placeholder for load
                           Toast.makeText(MainMenuActivity.this, "Load Game not implemented yet, starting New Game.", Toast.LENGTH_SHORT).show();
                           intent.putExtra("loadSave", false); // Indicate new game (placeholder)
                           // intent.putExtra("loadSave", true); // Indicate load game (when implemented)
                           startActivity(intent);
                       }
                   }
               });
        builder.create().show();
    }

     private void showAboutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.about_author_title) // "Об авторе"
               .setMessage("by Zawertun") // TODO: Update with actual author info if needed
               .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                   public void onClick(DialogInterface dialog, int id) {
                       dialog.dismiss();
                   }
               });
        builder.create().show();
    }
}

