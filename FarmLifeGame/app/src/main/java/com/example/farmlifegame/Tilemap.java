package com.example.farmlifegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Tilemap {
    private Bitmap tilesetBitmap;
    private int tileSize = 16; // Assuming 16x16 tiles
    private int mapWidth = 20; // Example map width
    private int mapHeight = 15; // Example map height

    // Example map data (0=grass, 1=dirt, 2=water)
    // This needs to correspond to the actual tileset layout
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

    public Tilemap(Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false; // Load the bitmap at its original size
        tilesetBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.tileset_world, options);
        // TODO: Handle potential errors loading the bitmap
    }

    public void draw(Canvas canvas) {
        if (tilesetBitmap == null) return;

        for (int y = 0; y < mapHeight; y++) {
            for (int x = 0; x < mapWidth; x++) {
                int tileType = map[y][x];
                Rect sourceRect = getTileRect(tileType); // Get the rect for the tile in the tileset
                Rect destRect = new Rect(x * tileSize, y * tileSize, (x + 1) * tileSize, (y + 1) * tileSize);
                canvas.drawBitmap(tilesetBitmap, sourceRect, destRect, null);
            }
        }
    }

    // Helper method to get the Rect for a specific tile type from the tileset
    // This needs to be adjusted based on the actual layout of tileset_world.png
    private Rect getTileRect(int tileType) {
        int tilesPerRow = tilesetBitmap.getWidth() / tileSize;
        int tileX = 0; // Default to first tile (grass?)
        int tileY = 0;

        // VERY basic mapping - needs proper implementation based on tileset_world.png layout
        switch (tileType) {
            case 0: // Grass - Assuming it's at (0, 0) in the tileset
                tileX = 0;
                tileY = 0;
                break;
            case 1: // Dirt - Assuming it's at (1, 0)
                tileX = 1;
                tileY = 0;
                break;
            case 2: // Water - Assuming it's at (0, 1)
                tileX = 0;
                tileY = 1;
                break;
            // Add more cases for other tile types
        }

        int left = tileX * tileSize;
        int top = tileY * tileSize;
        return new Rect(left, top, left + tileSize, top + tileSize);
    }

    // TODO: Add methods for collision detection, getting tile type at position, etc.
}

