package com.example.farmlifegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.Log;

// Note: Tilemap might need to save its state (map array) if it can be modified (e.g., tilled soil)
// If so, the 'map' array should be part of GameData, not Tilemap itself, or Tilemap needs to be Serializable.
public class Tilemap {
    private static final String TAG = "Tilemap";

    private transient Bitmap tilesetBitmap; // Use transient if Tilemap becomes Serializable
    private int tileSize = 16; // Assuming 16x16 tiles
    private int mapWidth = 20; // Example map width
    private int mapHeight = 15; // Example map height
    private int tilesetColumns; // Number of columns in the tileset image

    // Example map data (0=grass, 1=dirt, 2=water, 3=tilled_dirt)
    // This needs to correspond to the actual tileset layout
    // Consider loading this from a file or GameData
    private int[][] map = {
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 1, 1, 1, 0, 0, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 1, 1, 1, 0, 0, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 1, 1, 1, 0, 0, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
    };

    // Define which tile types are solid for collision
    private static final int[] SOLID_TILES = {2}; // Example: Water is solid

    public Tilemap(Context context) {
        loadTileset(context);
        // TODO: Load map data from file or GameData instead of hardcoding
    }

    public void loadTileset(Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false; // Load the bitmap at its original size
        try {
            tilesetBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.tileset_world, options);
            if (tilesetBitmap != null) {
                tilesetColumns = tilesetBitmap.getWidth() / tileSize;
                Log.d(TAG, "Tileset loaded: " + tilesetBitmap.getWidth() + "x" + tilesetBitmap.getHeight() + ", Columns: " + tilesetColumns);
            } else {
                Log.e(TAG, "Failed to load tileset_world.png");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading tileset bitmap", e);
        }
    }

    public void draw(Canvas canvas) {
        if (tilesetBitmap == null) {
            Log.w(TAG, "Cannot draw tilemap, tileset not loaded.");
            return;
        }

        // TODO: Only draw tiles visible within the camera viewport
        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                int tileType = getTileType(x, y);
                if (tileType < 0) continue; // Skip invalid tiles

                Rect sourceRect = getTileRect(tileType); // Get the rect for the tile in the tileset
                Rect destRect = new Rect(x * tileSize, y * tileSize, (x + 1) * tileSize, (y + 1) * tileSize);
                canvas.drawBitmap(tilesetBitmap, sourceRect, destRect, null);
            }
        }
    }

    /**
     * Gets the Rect for a specific tile type ID from the tileset bitmap.
     * Assumes tiles are arranged sequentially left-to-right, top-to-bottom.
     * @param tileType The ID of the tile type.
     * @return Rect defining the tile's location in the tileset bitmap.
     */
    private Rect getTileRect(int tileType) {
        if (tilesetColumns <= 0) {
            // Return a default rect if tileset info is missing
            return new Rect(0, 0, tileSize, tileSize);
        }
        int tileY = tileType / tilesetColumns;
        int tileX = tileType % tilesetColumns;

        int left = tileX * tileSize;
        int top = tileY * tileSize;
        return new Rect(left, top, left + tileSize, top + tileSize);
    }

    /**
     * Checks if the tile at the given tile coordinates is solid.
     * @param tileX The x-coordinate in tiles.
     * @param tileY The y-coordinate in tiles.
     * @return true if the tile is solid, false otherwise.
     */
    public boolean isSolid(int tileX, int tileY) {
        int tileType = getTileType(tileX, tileY);
        if (tileType < 0) {
            return true; // Treat out-of-bounds as solid
        }
        for (int solidType : SOLID_TILES) {
            if (tileType == solidType) {
                return true;
            }
        }
        return false;
    }

    /**
     * Gets the type of tile at the given tile coordinates.
     * @param tileX The x-coordinate in tiles.
     * @param tileY The y-coordinate in tiles.
     * @return The tile type ID, or -1 if coordinates are out of bounds.
     */
    public int getTileType(int tileX, int tileY) {
        if (tileX >= 0 && tileX < mapWidth && tileY >= 0 && tileY < mapHeight) {
            return map[tileY][tileX];
        } else {
            return -1; // Out of bounds
        }
    }

    /**
     * Sets the type of tile at the given tile coordinates.
     * Used for actions like tilling soil.
     * @param tileX The x-coordinate in tiles.
     * @param tileY The y-coordinate in tiles.
     * @param tileType The new tile type ID.
     * @return true if the tile was set successfully, false if coordinates are out of bounds.
     */
    public boolean setTile(int tileX, int tileY, int tileType) {
        if (tileX >= 0 && tileX < mapWidth && tileY >= 0 && tileY < mapHeight) {
            map[tileY][tileX] = tileType;
            Log.d(TAG, "Set tile (" + tileX + "," + tileY + ") to type " + tileType);
            return true;
        } else {
            Log.w(TAG, "Cannot set tile, coordinates out of bounds: (" + tileX + "," + tileY + ")");
            return false;
        }
    }

    public int getTileSize() {
        return tileSize;
    }

    public int getMapWidthPixels() {
        return mapWidth * tileSize;
    }

    public int getMapHeightPixels() {
        return mapHeight * tileSize;
    }

    // Method to get map data for saving (if map is modified)
    public int[][] getMapData() {
        return map;
    }

    // Method to set map data when loading (if map is modified)
    public void setMapData(int[][] loadedMap) {
        if (loadedMap != null && loadedMap.length == mapHeight && loadedMap[0].length == mapWidth) {
            this.map = loadedMap;
            Log.d(TAG, "Map data loaded.");
        } else {
            Log.e(TAG, "Failed to load map data due to dimension mismatch or null data.");
        }
    }
}

