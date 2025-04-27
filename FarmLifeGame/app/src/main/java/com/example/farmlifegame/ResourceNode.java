package com.example.farmlifegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
<<<<<<< HEAD
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.io.Serializable;
=======
import android.graphics.Rect;
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
import java.util.HashMap;
import java.util.Map;

// Enum for different types of resources that can be gathered
enum ResourceType {
    STONE, COAL, IRON, GOLD, EMERALD, DIAMOND // Add others like WOOD, FISH later
}

<<<<<<< HEAD
// Represents a gatherable resource node in the game world.
// Consider making this Serializable or creating a separate ResourceNodeData class for saving.
public class ResourceNode {
    private static final String TAG = "ResourceNode";

    private int x, y; // Position on the map (in pixels)
    private ResourceType type;
    private transient Bitmap spriteSheet; // Use transient if ResourceNode becomes Serializable
    private transient Rect sourceRect; // Rect for the specific resource icon
    private int maxHealth;
    private int currentHealth;
    private boolean depleted = false;
    private long depletionTime = 0; // Time when the node was depleted
    private long respawnDelayMillis; // Time until respawn after depletion
=======
public class ResourceNode {
    private int x, y; // Position on the map (in tile coordinates or pixels)
    private ResourceType type;
    private Bitmap spriteSheet; // Using the icons spritesheet
    private Rect sourceRect; // Rect for the specific resource icon
    private int health; // How many hits it takes to break
    private boolean depleted = false;
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
    private int tileSize = 16; // Assuming 16x16 tiles/icons

    // Map to store icon rectangles for each resource type
    private static Map<ResourceType, Rect> resourceIconRects = new HashMap<>();
<<<<<<< HEAD
    // Map to store base health for each resource type
    private static Map<ResourceType, Integer> resourceBaseHealth = new HashMap<>();
    // Map to store respawn delays (in milliseconds)
    private static Map<ResourceType, Long> resourceRespawnDelays = new HashMap<>();

    // Static block to initialize resource properties
    static {
        int iconSize = 16; // Assuming 16x16 icons
        // Icon Rects (Verify coordinates based on spritesheet_icons.png)
        resourceIconRects.put(ResourceType.STONE, new Rect(1 * iconSize, 1 * iconSize, 2 * iconSize, 2 * iconSize));
        resourceIconRects.put(ResourceType.COAL, new Rect(0 * iconSize, 1 * iconSize, 1 * iconSize, 2 * iconSize)); // Placeholder: Coal icon?
        resourceIconRects.put(ResourceType.IRON, new Rect(2 * iconSize, 1 * iconSize, 3 * iconSize, 2 * iconSize));
        resourceIconRects.put(ResourceType.GOLD, new Rect(0 * iconSize, 2 * iconSize, 1 * iconSize, 3 * iconSize));
        resourceIconRects.put(ResourceType.EMERALD, new Rect(1 * iconSize, 2 * iconSize, 2 * iconSize, 3 * iconSize));
        resourceIconRects.put(ResourceType.DIAMOND, new Rect(3 * iconSize, 2 * iconSize, 4 * iconSize, 3 * iconSize));

        // Base Health
        resourceBaseHealth.put(ResourceType.STONE, 3);
        resourceBaseHealth.put(ResourceType.COAL, 3);
        resourceBaseHealth.put(ResourceType.IRON, 5);
        resourceBaseHealth.put(ResourceType.GOLD, 7);
        resourceBaseHealth.put(ResourceType.EMERALD, 10);
        resourceBaseHealth.put(ResourceType.DIAMOND, 15);

        // Respawn Delays (Example: 1 minute for stone, 5 for iron, etc.)
        resourceRespawnDelays.put(ResourceType.STONE, 60 * 1000L);
        resourceRespawnDelays.put(ResourceType.COAL, 90 * 1000L);
        resourceRespawnDelays.put(ResourceType.IRON, 5 * 60 * 1000L);
        resourceRespawnDelays.put(ResourceType.GOLD, 10 * 60 * 1000L);
        resourceRespawnDelays.put(ResourceType.EMERALD, 20 * 60 * 1000L);
        resourceRespawnDelays.put(ResourceType.DIAMOND, 30 * 60 * 1000L);
=======

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
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
    }

    public ResourceNode(Context context, int x, int y, ResourceType type) {
        this.x = x;
        this.y = y;
        this.type = type;
<<<<<<< HEAD
        this.maxHealth = resourceBaseHealth.getOrDefault(type, 3);
        this.currentHealth = this.maxHealth;
        this.respawnDelayMillis = resourceRespawnDelays.getOrDefault(type, 5 * 60 * 1000L); // Default 5 mins

        loadSprite(context);
    }

    // Separate method to load transient sprite data
    public void loadSprite(Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        try {
            spriteSheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.spritesheet_icons, options);
            if (spriteSheet == null) {
                 Log.e(TAG, "Failed to load spritesheet_icons.png for ResourceNode");
                 return;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading resource node spritesheet", e);
            return;
        }
        this.sourceRect = resourceIconRects.getOrDefault(type, new Rect(0, 0, tileSize, tileSize)); // Default to top-left if type not found
    }

    /**
     * Reduces the node's health based on the damage dealt.
     * @param damage The amount of damage dealt (e.g., based on tool power).
     */
    public void hit(int damage) {
        if (!depleted && damage > 0) {
            currentHealth -= damage;
            Log.d(TAG, type + " hit for " + damage + " damage. Health: " + currentHealth + "/" + maxHealth);
            if (currentHealth <= 0) {
                deplete();
            }
            // TODO: Add visual feedback (shake, particle effect)
        }
    }

    private void deplete() {
        depleted = true;
        currentHealth = 0;
        depletionTime = System.currentTimeMillis();
        Log.i(TAG, type + " node at (" + x + "," + y + ") depleted.");
        // Item drop logic is handled in GameView after depletion
    }

    /**
     * Checks if the node should respawn based on the respawn timer.
     * Call this periodically in the game update loop.
     */
    public void update() {
        if (depleted) {
            long timeSinceDepletion = System.currentTimeMillis() - depletionTime;
            if (timeSinceDepletion >= respawnDelayMillis) {
                respawn();
=======
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
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
            }
        }
    }

<<<<<<< HEAD
    private void respawn() {
        depleted = false;
        currentHealth = maxHealth;
        depletionTime = 0;
        Log.i(TAG, type + " node at (" + x + "," + y + ") respawned.");
    }

    public void draw(Canvas canvas) {
        if (!depleted && spriteSheet != null && sourceRect != null) {
            // Draw the resource node icon at its position
            Rect destRect = new Rect(x, y, x + tileSize, y + tileSize);
            canvas.drawBitmap(spriteSheet, sourceRect, destRect, null);

            // Optional: Draw health bar
            if (currentHealth < maxHealth) {
                Paint healthPaint = new Paint();
                float healthPercent = (float) currentHealth / maxHealth;
                healthPaint.setColor(healthPercent > 0.5f ? Color.GREEN : (healthPercent > 0.2f ? Color.YELLOW : Color.RED));
                int barWidth = tileSize;
                int barHeight = 2;
                int barY = y + tileSize + 1; // Below the icon
                canvas.drawRect(x, barY, x + (barWidth * healthPercent), barY + barHeight, healthPaint);
            }
        }
        // Optionally draw a depleted state (e.g., grayed out, different sprite)
    }

    // --- Getters --- (and Setters if needed for loading state)

=======
    public void draw(Canvas canvas) {
        if (!depleted && spriteSheet != null && sourceRect != null) {
            // Draw the resource node icon at its position
            // Assuming x, y are pixel coordinates. Adjust if they are tile coordinates.
            Rect destRect = new Rect(x, y, x + tileSize, y + tileSize);
            canvas.drawBitmap(spriteSheet, sourceRect, destRect, null);
        }
    }

>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
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

<<<<<<< HEAD
    public int getCurrentHealth() {
        return currentHealth;
    }

    public long getDepletionTime() {
        return depletionTime;
    }

=======
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
    public Rect getBounds() {
        // Return the bounding box for interaction detection
        return new Rect(x, y, x + tileSize, y + tileSize);
    }
<<<<<<< HEAD

    // --- Methods for Saving/Loading State --- (if not using a separate Data class)

    // Example: Get state to save
    public ResourceNodeState getState() {
        return new ResourceNodeState(x, y, type, currentHealth, depleted, depletionTime);
    }

    // Example: Apply loaded state
    public void applyState(ResourceNodeState state) {
        if (state != null && this.x == state.x && this.y == state.y && this.type == state.type) {
            this.currentHealth = state.currentHealth;
            this.depleted = state.depleted;
            this.depletionTime = state.depletionTime;
        } else {
            Log.w(TAG, "Failed to apply state, mismatch detected.");
        }
    }

    // Simple Serializable class to hold ResourceNode state for saving
    public static class ResourceNodeState implements Serializable {
        private static final long serialVersionUID = 1L;
        int x, y;
        ResourceType type;
        int currentHealth;
        boolean depleted;
        long depletionTime;

        // Constructor
        public ResourceNodeState(int x, int y, ResourceType type, int currentHealth, boolean depleted, long depletionTime) {
            this.x = x;
            this.y = y;
            this.type = type;
            this.currentHealth = currentHealth;
            this.depleted = depleted;
            this.depletionTime = depletionTime;
        }
    }
=======
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
}

