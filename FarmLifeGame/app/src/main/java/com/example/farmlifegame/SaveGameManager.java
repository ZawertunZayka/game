package com.example.farmlifegame;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages saving and loading game state (GameData) to internal storage.
 */
public class SaveGameManager {

    private static final String TAG = "SaveGameManager";
    private static final String SAVE_FILE_PREFIX = "savegame_";
    private static final String SAVE_FILE_SUFFIX = ".dat";

    private Context context;

    public SaveGameManager(Context context) {
        this.context = context;
    }

    /**
     * Saves the given GameData object to a specified slot name.
     * @param data The GameData object to save.
     * @param slotName The name of the save slot (e.g., "slot1").
     * @return true if saving was successful, false otherwise.
     */
    public boolean saveGame(GameData data, String slotName) {
        if (data == null || slotName == null || slotName.isEmpty()) {
            Log.e(TAG, "Invalid data or slot name for saving.");
            return false;
        }

        String filename = SAVE_FILE_PREFIX + slotName + SAVE_FILE_SUFFIX;
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        boolean success = false;

        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(data);
            success = true;
            Log.i(TAG, "Game saved successfully to " + filename);
        } catch (IOException e) {
            Log.e(TAG, "Error saving game to " + filename, e);
        } finally {
            try {
                if (oos != null) oos.close();
                if (fos != null) fos.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing output streams.", e);
            }
        }
        return success;
    }

    /**
     * Loads GameData from a specified slot name.
     * @param slotName The name of the save slot to load from.
     * @return The loaded GameData object, or null if loading failed or file doesn't exist.
     */
    public GameData loadGame(String slotName) {
        if (slotName == null || slotName.isEmpty()) {
            Log.e(TAG, "Invalid slot name for loading.");
            return null;
        }

        String filename = SAVE_FILE_PREFIX + slotName + SAVE_FILE_SUFFIX;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        GameData loadedData = null;

        try {
            File file = context.getFileStreamPath(filename);
            if (!file.exists()) {
                Log.w(TAG, "Save file not found: " + filename);
                return null;
            }
            fis = context.openFileInput(filename);
            ois = new ObjectInputStream(fis);
            loadedData = (GameData) ois.readObject();
            Log.i(TAG, "Game loaded successfully from " + filename);
        } catch (IOException | ClassNotFoundException | ClassCastException e) {
            Log.e(TAG, "Error loading game from " + filename, e);
            // Consider deleting the corrupted save file?
        } finally {
            try {
                if (ois != null) ois.close();
                if (fis != null) fis.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing input streams.", e);
            }
        }
        return loadedData;
    }

    /**
     * Lists the names of available save slots.
     * @return A list of save slot names (e.g., ["slot1", "slot3"]).
     */
    public List<String> listSaveSlots() {
        List<String> slots = new ArrayList<>();
        File dir = context.getFilesDir();
        if (dir != null) {
            File[] files = dir.listFiles((d, name) -> name.startsWith(SAVE_FILE_PREFIX) && name.endsWith(SAVE_FILE_SUFFIX));
            if (files != null) {
                for (File file : files) {
                    String name = file.getName();
                    String slotName = name.substring(SAVE_FILE_PREFIX.length(), name.length() - SAVE_FILE_SUFFIX.length());
                    slots.add(slotName);
                }
            }
        }
        Log.d(TAG, "Found save slots: " + slots);
        return slots;
    }

    /**
     * Deletes a specific save slot.
     * @param slotName The name of the save slot to delete.
     * @return true if deletion was successful or file didn't exist, false otherwise.
     */
    public boolean deleteSave(String slotName) {
         if (slotName == null || slotName.isEmpty()) {
            Log.e(TAG, "Invalid slot name for deletion.");
            return false;
        }
        String filename = SAVE_FILE_PREFIX + slotName + SAVE_FILE_SUFFIX;
        boolean deleted = context.deleteFile(filename);
        if (deleted) {
            Log.i(TAG, "Deleted save file: " + filename);
        } else {
            // Check if file existed in the first place
            File file = context.getFileStreamPath(filename);
            if (!file.exists()) {
                Log.w(TAG, "Save file to delete not found: " + filename);
                return true; // Consider it success if file doesn't exist
            } else {
                Log.e(TAG, "Failed to delete save file: " + filename);
            }
        }
        return deleted;
    }
}

