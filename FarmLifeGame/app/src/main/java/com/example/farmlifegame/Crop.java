package com.example.farmlifegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.io.Serializable;

/**
 * Represents a specific type of crop that can be grown.
 * Contains information about growth stages, times, sprites, and items.
 */
public class Crop implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final String TAG = "Crop";

    private String name;
    private Item seedItem; // The seed item required to plant this crop
    private Item harvestedItem; // The item obtained when harvested
    private int[] growthStageTimes; // Milliseconds required to reach each stage (e.g., [0, 1day, 2days, 3days])
    private int totalGrowthStages; // Number of distinct visual stages (including planted seed)
    private transient Bitmap spriteSheet; // Spritesheet containing crop stages (e.g., R.drawable.spritesheet_crops)
    private transient Rect[] spriteRects; // Rectangles for each growth stage sprite
    private int spriteWidth = 16; // Example width
    private int spriteHeight = 16; // Example height
    private int spriteSheetRow; // Which row in the spritesheet this crop uses

    // Constructor
    public Crop(String name, Item seedItem, Item harvestedItem, int[] growthStageTimes, int spriteSheetRow) {
        this.name = name;
        this.seedItem = seedItem;
        this.harvestedItem = harvestedItem;
        this.growthStageTimes = growthStageTimes;
        this.totalGrowthStages = growthStageTimes.length;
        this.spriteSheetRow = spriteSheetRow;

        if (seedItem == null || harvestedItem == null) {
            Log.e(TAG, "Seed or Harvested item cannot be null for crop: " + name);
            // Handle error appropriately
        }
    }

    // Method to load transient sprite data
    public void loadSprites(Context context) {
        if (spriteSheet != null) return; // Already loaded

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        try {
            // Assuming a common spritesheet for all crops
            spriteSheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.spritesheet_crops, options);
            if (spriteSheet == null) {
                Log.e(TAG, "Failed to load spritesheet_crops.png");
                return;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading crop spritesheet", e);
            return;
        }

        // Initialize spriteRects based on totalGrowthStages and spriteSheetRow
        spriteRects = new Rect[totalGrowthStages];
        for (int stage = 0; stage < totalGrowthStages; stage++) {
            int left = stage * spriteWidth;
            int top = spriteSheetRow * spriteHeight;
            // Basic bounds check
            if (left + spriteWidth <= spriteSheet.getWidth() && top + spriteHeight <= spriteSheet.getHeight()) {
                spriteRects[stage] = new Rect(left, top, left + spriteWidth, top + spriteHeight);
            } else {
                Log.e(TAG, "Crop sprite calculation out of bounds for crop=" + name + ", stage=" + stage);
                spriteRects[stage] = new Rect(0, 0, spriteWidth, spriteHeight); // Default
            }
        }
        Log.d(TAG, "Loaded sprites for crop: " + name);
    }

    /**
     * Gets the required time in milliseconds to reach a specific growth stage.
     * @param stage The target growth stage index.
     * @return Time in milliseconds, or -1 if stage is invalid.
     */
    public long getTimeForStage(int stage) {
        if (stage >= 0 && stage < totalGrowthStages) {
            return growthStageTimes[stage];
        }
        return -1;
    }

    /**
     * Gets the current growth stage based on the time elapsed since planting.
     * @param timeElapsedMillis Time elapsed since planting.
     * @return The current growth stage index (0 to totalGrowthStages - 1).
     */
    public int getStageForTime(long timeElapsedMillis) {
        int currentStage = 0;
        for (int i = totalGrowthStages - 1; i >= 0; i--) {
            if (timeElapsedMillis >= growthStageTimes[i]) {
                currentStage = i;
                break;
            }
        }
        return currentStage;
    }

    /**
     * Checks if the crop is fully grown based on the time elapsed.
     * @param timeElapsedMillis Time elapsed since planting.
     * @return true if fully grown, false otherwise.
     */
    public boolean isFullyGrown(long timeElapsedMillis) {
        return getStageForTime(timeElapsedMillis) == totalGrowthStages - 1;
    }

    /**
     * Draws the crop at a specific stage.
     * @param canvas The canvas to draw on.
     * @param destRect The destination rectangle on the canvas.
     * @param stage The growth stage to draw.
     */
    public void draw(Canvas canvas, Rect destRect, int stage) {
        if (spriteSheet == null || spriteRects == null) {
            Log.w(TAG, "Cannot draw crop " + name + ", sprites not loaded.");
            // Optionally draw placeholder
            return;
        }
        if (stage >= 0 && stage < totalGrowthStages) {
            Rect sourceRect = spriteRects[stage];
            canvas.drawBitmap(spriteSheet, sourceRect, destRect, null);
        } else {
            Log.w(TAG, "Invalid stage requested for drawing crop " + name + ": " + stage);
        }
    }

    // --- Getters ---
    public String getName() {
        return name;
    }

    public Item getSeedItem() {
        return seedItem;
    }

    public Item getHarvestedItem() {
        return harvestedItem;
    }

    public int getTotalGrowthStages() {
        return totalGrowthStages;
    }
}

