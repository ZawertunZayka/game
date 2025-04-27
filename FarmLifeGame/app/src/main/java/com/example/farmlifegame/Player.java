package com.example.farmlifegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

// Note: Player class itself might not need to be Serializable if GameData handles its state.
// However, making fields accessible for GameData is important.
public class Player {
    private static final String TAG = "Player";

    private transient Bitmap spriteSheet; // Use transient for non-serializable fields if Player becomes Serializable
    private int x, y; // Player position on the map (in pixels)
    private int width, height; // Width and height of a single sprite frame
    private int currentFrame = 0;
    private int direction = 0; // 0: down, 1: up, 2: left, 3: right
    private int walkFrames = 3; // Number of frames in walk animation
    private long lastFrameChangeTime = 0;
    private int frameLengthInMilliseconds = 150; // Animation speed
    private boolean isMoving = false; // Track if player is actively moving for animation

    private transient Rect[][] spriteFrames; // Transient if Player becomes Serializable
    private Inventory inventory; // Add inventory
    private int currency = 0;
    private int experience = 0;
    private int level = 1;
    private int xpToNextLevel = 100; // XP needed for level 2
    private ItemStack equippedTool = null; // Reference to the equipped tool stack
    private int interactionRange = 16; // How far the player can interact (in pixels)

    public Player(Context context, int startX, int startY) {
        this.x = startX;
        this.y = startY;

        // Initialize Inventory (example capacity)
        this.inventory = new Inventory(20); // Ensure Inventory is Serializable
        calculateXpForLevel(this.level); // Calculate initial XP requirement

        loadSprites(context);
    }

    // Separate method to load non-serializable resources
    public void loadSprites(Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        try {
            spriteSheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.spritesheet_characters, options);
            if (spriteSheet == null) {
                 Log.e(TAG, "Failed to load spritesheet_characters.png");
                 return;
            }
        } catch (Exception e) {
            Log.e(TAG, "Error loading player spritesheet", e);
            return;
        }

        this.width = 16;
        this.height = 18;
        int characterBlockWidth = width * walkFrames;
        int characterBlockHeight = height * 4; // 4 directions

        int characterStartX = 0; // Column index of the character block
        int characterStartY = 0; // Row index of the character block

        spriteFrames = new Rect[4][walkFrames]; // [direction][frame]
        for (int dir = 0; dir < 4; dir++) {
            for (int frame = 0; frame < walkFrames; frame++) {
                int left = characterStartX * characterBlockWidth + frame * width;
                int top = characterStartY * characterBlockHeight + dir * height;
                if (spriteSheet != null && left + width <= spriteSheet.getWidth() && top + height <= spriteSheet.getHeight()) {
                    spriteFrames[dir][frame] = new Rect(left, top, left + width, top + height);
                } else {
                    Log.e(TAG, "Sprite frame calculation out of bounds for dir=" + dir + ", frame=" + frame);
                    spriteFrames[dir][frame] = new Rect(0, 0, width, height); // Default to top-left frame
                }
            }
        }
    }

    public void update() {
        if (isMoving) {
            long time = System.currentTimeMillis();
            if (time > lastFrameChangeTime + frameLengthInMilliseconds) {
                lastFrameChangeTime = time;
                currentFrame = (currentFrame + 1) % walkFrames;
            }
        } else {
            currentFrame = 0; // Stand still frame
        }
        isMoving = false; // Reset moving flag, will be set by GameView if joystick is active
    }

    public void draw(Canvas canvas) {
        if (spriteSheet == null || spriteFrames == null) {
             Log.w(TAG, "Cannot draw player, sprites not loaded.");
             Paint p = new Paint(); p.setColor(Color.MAGENTA);
             canvas.drawRect(x, y, x + width, y + height, p);
             return;
        }

        Rect sourceRect = spriteFrames[direction][currentFrame];
        Rect destRect = new Rect(x, y, x + width, y + height);
        canvas.drawBitmap(spriteSheet, sourceRect, destRect, null);
    }

    public Rect getInteractionBounds() {
        Rect bounds = new Rect(x, y, x + width, y + height);
        switch (direction) {
            case 0: bounds.top = bounds.bottom; bounds.bottom += interactionRange; break;
            case 1: bounds.bottom = bounds.top; bounds.top -= interactionRange; break;
            case 2: bounds.right = bounds.left; bounds.left -= interactionRange; break;
            case 3: bounds.left = bounds.right; bounds.right += interactionRange; break;
        }
        if (bounds.top > bounds.bottom) { int temp = bounds.top; bounds.top = bounds.bottom; bounds.bottom = temp; }
        if (bounds.left > bounds.right) { int temp = bounds.left; bounds.left = bounds.right; bounds.right = temp; }
        return bounds;
    }

    // --- Getters and Setters ---

    public int getX() { return x; }
    public int getY() { return y; }
    public void setPosition(int x, int y) { this.x = x; this.y = y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public int getDirection() { return direction; }
    public Inventory getInventory() { return inventory; }
    public void setInventory(Inventory inventory) { this.inventory = inventory; }
    public int getCurrency() { return currency; }
    public void setCurrency(int currency) { this.currency = Math.max(0, currency); }
    public void addCurrency(int amount) { this.currency = Math.max(0, this.currency + amount); Log.d(TAG, "Currency updated: " + this.currency); }
    public int getExperience() { return experience; }
    public void setExperience(int experience) { this.experience = Math.max(0, experience); checkLevelUp(); /* Check level after setting XP, e.g., on load */ }
    public int getLevel() { return level; }
    public void setLevel(int level) { this.level = Math.max(1, level); calculateXpForLevel(this.level); /* Recalculate needed XP */ }
    public int getXpToNextLevel() { return xpToNextLevel; }
    public ItemStack getEquippedTool() { return equippedTool; }

    public void setDirection(int direction) {
        if (direction >= 0 && direction < 4) {
            if (this.direction != direction) {
                 this.direction = direction;
                 this.currentFrame = 0;
                 this.lastFrameChangeTime = System.currentTimeMillis();
            }
        }
    }

    public void setMoving(boolean moving) {
        this.isMoving = moving;
    }

    public void setEquippedTool(ItemStack equippedTool) {
        if (equippedTool == null || equippedTool.getItem().getType() == ItemType.TOOL) {
            this.equippedTool = equippedTool;
            Log.d(TAG, "Equipped tool: " + (equippedTool != null ? equippedTool.getItem().getName() : "None"));
        } else {
            Log.w(TAG, "Attempted to equip non-tool item: " + equippedTool.getItem().getName());
        }
    }

    // --- XP and Leveling ---

    public void addExperience(int amount) {
        if (amount > 0) {
            this.experience += amount;
            Log.d(TAG, "Gained " + amount + " XP. Total: " + this.experience + " / " + this.xpToNextLevel);
            checkLevelUp();
        }
    }

    private void checkLevelUp() {
        while (this.experience >= this.xpToNextLevel) {
            this.level++;
            this.experience -= this.xpToNextLevel; // Subtract threshold, keep remainder
            calculateXpForLevel(this.level);
            Log.i(TAG, "*** LEVEL UP! Reached Level " + this.level + " ***");
            // TODO: Add level up effects (e.g., stat increase, new recipes)
            // For now, just log it.
        }
    }

    // Simple formula for XP required for the *next* level
    private void calculateXpForLevel(int currentLevel) {
        // Example formula: 100 * (level ^ 1.5)
        this.xpToNextLevel = (int) (100 * Math.pow(currentLevel, 1.5));
        Log.d(TAG, "XP needed for Level " + (currentLevel + 1) + ": " + this.xpToNextLevel);
    }
}

