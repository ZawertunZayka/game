package com.example.farmlifegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

import java.io.Serializable;

/**
 * Represents a single tile of farmable land.
 * Manages state like tilled, planted crop, growth, and watered status.
 */
public class FarmPlot {
    private static final String TAG = "FarmPlot";

    // Enum for plot states
    public enum PlotState { UNTILLED, TILLED, PLANTED }

    private int tileX, tileY; // Position in tile coordinates
    private PlotState state = PlotState.UNTILLED;
    private Crop plantedCrop = null;
    private long plantedTimestamp = 0; // Time when the crop was planted
    private boolean isWatered = false;
    private long lastWateredTimestamp = 0; // Time when the plot was last watered
    private int currentGrowthStage = 0;

    private transient Tilemap tilemap; // Reference to the tilemap for drawing/tile info
    private transient Bitmap farmingSpritesheet; // Spritesheet for tilled/watered states
    private transient Rect tilledRect; // Sprite for tilled soil
    private transient Rect wateredTilledRect; // Sprite for watered tilled soil
    private int tileSize = 16;

    public FarmPlot(Context context, Tilemap tilemap, int tileX, int tileY) {
        this.tilemap = tilemap;
        this.tileX = tileX;
        this.tileY = tileY;
        this.tileSize = tilemap.getTileSize();
        loadSprites(context);
    }

    // Load transient sprite data
    public void loadSprites(Context context) {
        if (farmingSpritesheet != null) return;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        try {
            // Assuming a spritesheet containing tilled/watered soil graphics
            // Let's reuse tileset_world for now, assuming tilled is tile 3, watered is 4
            farmingSpritesheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.tileset_world, options);
            if (farmingSpritesheet == null) {
                Log.e(TAG, "Failed to load tileset_world.png for FarmPlot sprites");
                return;
            }
            // TODO: Define actual sprite locations for tilled/watered soil
            int tilledTileIndex = 3; // Example index in tileset
            int wateredTileIndex = 4; // Example index
            int tilesetColumns = farmingSpritesheet.getWidth() / tileSize;

            int ty = tilledTileIndex / tilesetColumns;
            int tx = tilledTileIndex % tilesetColumns;
            tilledRect = new Rect(tx * tileSize, ty * tileSize, (tx + 1) * tileSize, (ty + 1) * tileSize);

            ty = wateredTileIndex / tilesetColumns;
            tx = wateredTileIndex % tilesetColumns;
            wateredTilledRect = new Rect(tx * tileSize, ty * tileSize, (tx + 1) * tileSize, (ty + 1) * tileSize);

        } catch (Exception e) {
            Log.e(TAG, "Error loading farm plot spritesheet", e);
        }

        // Ensure associated crop sprites are loaded if a crop is planted
        if (plantedCrop != null) {
            plantedCrop.loadSprites(context);
        }
    }

    /**
     * Updates the growth state of the planted crop.
     * Needs to be called periodically (e.g., once per game day or more often).
     */
    public void update() {
        if (state == PlotState.PLANTED && plantedCrop != null) {
            // Growth only happens if watered (implement daily check or continuous check)
            // Simple check: Assume growth requires watering within the last 24 hours (example)
            long timeSinceWatered = System.currentTimeMillis() - lastWateredTimestamp;
            boolean canGrow = isWatered; // Basic: needs to be watered *now*
            // More complex: if (timeSinceWatered < 24 * 60 * 60 * 1000) { canGrow = true; }

            if (canGrow) {
                long timeElapsed = System.currentTimeMillis() - plantedTimestamp;
                int newStage = plantedCrop.getStageForTime(timeElapsed);
                if (newStage != currentGrowthStage) {
                    currentGrowthStage = newStage;
                    Log.d(TAG, "Crop " + plantedCrop.getName() + " at (" + tileX + "," + tileY + ") grew to stage " + currentGrowthStage);
                }
            }\n            // Reset watered state daily? (Needs game time implementation)
            // if (isNewDay) { isWatered = false; }
        }
    }

    /**
     * Draws the farm plot, including tilled state and crop.
     * @param canvas The canvas to draw on.
     */
    public void draw(Canvas canvas) {
        int drawX = tileX * tileSize;
        int drawY = tileY * tileSize;
        Rect destRect = new Rect(drawX, drawY, drawX + tileSize, drawY + tileSize);

        // Draw base tile (e.g., dirt) - Handled by Tilemap drawing

        // Draw tilled/watered state overlay
        if (state != PlotState.UNTILLED && farmingSpritesheet != null) {
            Rect source = isWatered ? wateredTilledRect : tilledRect;
            if (source != null) {
                canvas.drawBitmap(farmingSpritesheet, source, destRect, null);
            }
        }

        // Draw planted crop
        if (state == PlotState.PLANTED && plantedCrop != null) {
            plantedCrop.draw(canvas, destRect, currentGrowthStage);
        }
    }

    /**
     * Tills the plot if it's untilled.
     * @return true if successfully tilled, false otherwise.
     */
    public boolean till() {
        if (state == PlotState.UNTILLED) {
            // TODO: Check if underlying tile is tillable (e.g., dirt, grass)
            // int baseTileType = tilemap.getTileType(tileX, tileY);
            // if (baseTileType == 1 /* dirt */ || baseTileType == 0 /* grass */) {
                state = PlotState.TILLED;
                isWatered = false; // Tilling removes watered state
                Log.d(TAG, "Plot at (" + tileX + "," + tileY + ") tilled.");
                return true;
            // }
        }
        Log.d(TAG, "Cannot till plot at (" + tileX + "," + tileY + "). State: " + state);
        return false;
    }

    /**
     * Plants a crop on the plot if it's tilled.
     * @param crop The Crop object to plant.
     * @param context Context needed to load crop sprites.
     * @return true if successfully planted, false otherwise.
     */
    public boolean plant(Crop crop, Context context) {
        if (state == PlotState.TILLED && crop != null) {
            this.plantedCrop = crop;
            this.plantedTimestamp = System.currentTimeMillis();
            this.currentGrowthStage = 0;
            this.state = PlotState.PLANTED;
            this.isWatered = false; // Planting requires re-watering
            this.plantedCrop.loadSprites(context); // Ensure sprites are loaded
            Log.d(TAG, "Planted " + crop.getName() + " at (" + tileX + "," + tileY + ").");
            return true;
        }
         Log.d(TAG, "Cannot plant crop at (" + tileX + "," + tileY + "). State: " + state + ", Crop: " + (crop != null ? crop.getName() : "null"));
        return false;
    }

    /**
     * Waters the plot if it's tilled or planted.
     * @return true if successfully watered, false otherwise.
     */
    public boolean water() {
        if (state == PlotState.TILLED || state == PlotState.PLANTED) {
            if (!isWatered) {
                isWatered = true;
                lastWateredTimestamp = System.currentTimeMillis();
                Log.d(TAG, "Plot at (" + tileX + "," + tileY + ") watered.");
                return true;
            } else {
                 Log.d(TAG, "Plot at (" + tileX + "," + tileY + ") already watered.");
                 return false; // Already watered
            }
        }
        Log.d(TAG, "Cannot water plot at (" + tileX + "," + tileY + "). State: " + state);
        return false;
    }

    /**
     * Harvests the crop if it's planted and fully grown.
     * @return The harvested Item, or null if not ready or nothing planted.
     */
    public Item harvest() {
        if (state == PlotState.PLANTED && plantedCrop != null) {
            long timeElapsed = System.currentTimeMillis() - plantedTimestamp;
            if (plantedCrop.isFullyGrown(timeElapsed)) {
                Item harvestedItem = plantedCrop.getHarvestedItem();
                Log.d(TAG, "Harvested " + harvestedItem.getName() + " from (" + tileX + "," + tileY + ").");
                // Reset plot state
                plantedCrop = null;
                plantedTimestamp = 0;
                currentGrowthStage = 0;
                state = PlotState.TILLED; // Leaves plot tilled after harvest
                isWatered = false;
                return harvestedItem;
            } else {
                 Log.d(TAG, "Crop at (" + tileX + "," + tileY + ") not fully grown.");
            }
        } else {
             Log.d(TAG, "Nothing to harvest at (" + tileX + "," + tileY + "). State: " + state);
        }
        return null;
    }

    // --- Getters --- (and Setters for loading state)
    public PlotState getState() {
        return state;
    }

    public Crop getPlantedCrop() {
        return plantedCrop;
    }

    public boolean isWatered() {
        return isWatered;
    }

    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }

    // --- Methods/Class for Saving/Loading State ---

    public FarmPlotState getSaveState() {
        return new FarmPlotState(tileX, tileY, state, isWatered, plantedCrop != null ? plantedCrop.getName() : null, plantedTimestamp, lastWateredTimestamp);
    }

    public void applySaveState(FarmPlotState saveState, Map<String, Crop> cropRegistry, Context context) {
        if (saveState != null && this.tileX == saveState.tileX && this.tileY == saveState.tileY) {
            this.state = saveState.state;
            this.isWatered = saveState.isWatered;
            this.plantedTimestamp = saveState.plantedTimestamp;
            this.lastWateredTimestamp = saveState.lastWateredTimestamp;
            if (saveState.plantedCropName != null) {
                this.plantedCrop = cropRegistry.get(saveState.plantedCropName);
                if (this.plantedCrop != null) {
                    this.plantedCrop.loadSprites(context); // Load sprites for the loaded crop
                    // Recalculate current stage based on loaded time
                    long timeElapsed = System.currentTimeMillis() - this.plantedTimestamp;
                    this.currentGrowthStage = this.plantedCrop.getStageForTime(timeElapsed);
                } else {
                    Log.e(TAG, "Could not find crop in registry: " + saveState.plantedCropName + " while loading state.");
                    this.state = PlotState.TILLED; // Revert to tilled if crop is missing
                }
            } else {
                this.plantedCrop = null;
                this.currentGrowthStage = 0;
            }
        } else {
             Log.w(TAG, "Failed to apply FarmPlot state, mismatch detected.");
        }
    }

    // Serializable class to hold FarmPlot state
    public static class FarmPlotState implements Serializable {
        private static final long serialVersionUID = 1L;
        int tileX, tileY;
        PlotState state;
        boolean isWatered;
        String plantedCropName; // Save crop name instead of object
        long plantedTimestamp;
        long lastWateredTimestamp;

        public FarmPlotState(int tileX, int tileY, PlotState state, boolean isWatered, String plantedCropName, long plantedTimestamp, long lastWateredTimestamp) {
            this.tileX = tileX;
            this.tileY = tileY;
            this.state = state;
            this.isWatered = isWatered;
            this.plantedCropName = plantedCropName;
            this.plantedTimestamp = plantedTimestamp;
            this.lastWateredTimestamp = lastWateredTimestamp;
        }
    }
}

