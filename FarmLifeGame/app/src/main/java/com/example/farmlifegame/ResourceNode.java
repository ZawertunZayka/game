package com.example.farmlifegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import java.util.HashMap;
import java.util.Map;

// Enum for different types of resources that can be gathered
enum ResourceType {
    STONE, COAL, IRON, GOLD, EMERALD, DIAMOND // Add others like WOOD, FISH later
}

public class ResourceNode {
    private int x, y; // Position on the map (in tile coordinates or pixels)
    private ResourceType type;
    private Bitmap spriteSheet; // Using the icons spritesheet
    private Rect sourceRect; // Rect for the specific resource icon
    private int health; // How many hits it takes to break
    private boolean depleted = false;
    private int tileSize = 16; // Assuming 16x16 tiles/icons

    // Map to store icon rectangles for each resource type
    private static Map<ResourceType, Rect> resourceIconRects = new HashMap<>();

    // Static block to initialize icon rectangles (adjust coordinates based on spritesheet_icons.png)
    static {
        // Assuming 16x16 icons in the sheet
        // Row 1: Wood, Stone, Iron
        // Row 2: Gold, Emerald, Ruby (unused), Diamond
        // These coordinates need verification by looking at spritesheet_icons.png
        // Using the version *with* outline for now (left side of the sheet)
        int iconSize = 16; // Assuming 16x16 icons
        resourceIconRects.put(ResourceType.STONE, new Rect(1 * iconSize, 1 * iconSize, 2 * iconSize, 2 * iconSize)); // Stone icon at (1,1)?
        resourceIconRects.put(ResourceType.COAL, new Rect(0, 0, iconSize, iconSize)); // No specific coal icon, using placeholder (top-left wood?)
        resourceIconRects.put(ResourceType.IRON, new Rect(2 * iconSize, 1 * iconSize, 3 * iconSize, 2 * iconSize)); // Iron icon at (2,1)?
        resourceIconRects.put(ResourceType.GOLD, new Rect(0 * iconSize, 2 * iconSize, 1 * iconSize, 3 * iconSize)); // Gold icon at (0,2)?
        resourceIconRects.put(ResourceType.EMERALD, new Rect(1 * iconSize, 2 * iconSize, 2 * iconSize, 3 * iconSize)); // Emerald icon at (1,2)?
        resourceIconRects.put(ResourceType.DIAMOND, new Rect(3 * iconSize, 2 * iconSize, 4 * iconSize, 3 * iconSize)); // Diamond icon at (3,2)?
    }

    public ResourceNode(Context context, int x, int y, ResourceType type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.health = 3; // Example: takes 3 hits

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        spriteSheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.spritesheet_icons, options);

        this.sourceRect = resourceIconRects.getOrDefault(type, new Rect(0, 0, tileSize, tileSize)); // Default to top-left if type not found
    }

    public void hit() {
        if (!depleted) {
            health--;
            if (health <= 0) {
                depleted = true;
                // TODO: Trigger item drop logic
            }
        }
    }

    public void draw(Canvas canvas) {
        if (!depleted && spriteSheet != null && sourceRect != null) {
            // Draw the resource node icon at its position
            // Assuming x, y are pixel coordinates. Adjust if they are tile coordinates.
            Rect destRect = new Rect(x, y, x + tileSize, y + tileSize);
            canvas.drawBitmap(spriteSheet, sourceRect, destRect, null);
        }
    }

    public boolean isDepleted() {
        return depleted;
    }

    public ResourceType getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rect getBounds() {
        // Return the bounding box for interaction detection
        return new Rect(x, y, x + tileSize, y + tileSize);
    }
}

