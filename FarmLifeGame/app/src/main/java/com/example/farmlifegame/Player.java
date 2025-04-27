package com.example.farmlifegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class Player {
    private Bitmap spriteSheet;
    private int x, y; // Player position on the map (in pixels)
    private int width, height; // Width and height of a single sprite frame
    private int currentFrame = 0;
    private int direction = 0; // 0: down, 1: up, 2: left, 3: right
    private int walkFrames = 3; // Number of frames in walk animation
    private long lastFrameChangeTime = 0;
    private int frameLengthInMilliseconds = 150; // Animation speed

    private Rect[][] spriteFrames;
    private Inventory inventory; // Add inventory

    public Player(Context context, int startX, int startY) {
        this.x = startX;
        this.y = startY;

        // Initialize Inventory (example capacity)
        this.inventory = new Inventory(20);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        spriteSheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.spritesheet_characters, options);

        // Assuming the spritesheet has characters arranged in rows, 12 characters per row
        // And each character block is 3 frames wide (walk) and 4 frames high (directions)
        // Let's pick a character, e.g., the first male character (Warrior?)
        // Sprite dimensions are roughly 16 width, 18 height based on filename
        // Let's assume 16x18 for calculations, adjust if needed after viewing the sheet
        this.width = 16;
        this.height = 18; // Or maybe 16? Need to verify.
        int characterBlockWidth = width * walkFrames;
        int characterBlockHeight = height * 4; // 4 directions

        // Select the first character block (top-left)
        int characterStartX = 0; // Column index of the character block
        int characterStartY = 0; // Row index of the character block

        spriteFrames = new Rect[4][walkFrames]; // [direction][frame]
        for (int dir = 0; dir < 4; dir++) {
            for (int frame = 0; frame < walkFrames; frame++) {
                int left = characterStartX * characterBlockWidth + frame * width;
                int top = characterStartY * characterBlockHeight + dir * height;
                spriteFrames[dir][frame] = new Rect(left, top, left + width, top + height);
            }
        }
    }

    public void update() {
        // Update animation frame based on time
        long time = System.currentTimeMillis();
        if (time > lastFrameChangeTime + frameLengthInMilliseconds) {
            lastFrameChangeTime = time;
            currentFrame = (currentFrame + 1) % walkFrames;
        }
        // TODO: Update position based on velocity/input
    }

    public void draw(Canvas canvas) {
        if (spriteSheet == null || spriteFrames == null) return;

        // Get the correct frame based on direction and animation state
        Rect sourceRect = spriteFrames[direction][currentFrame];
        Rect destRect = new Rect(x, y, x + width, y + height);
        canvas.drawBitmap(spriteSheet, sourceRect, destRect, null);
    }

    // Getters and Setters for position, direction etc.
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setDirection(int direction) {
        if (direction >= 0 && direction < 4) {
            if (this.direction != direction) {
                 this.direction = direction;
                 this.currentFrame = 0; // Reset animation when direction changes
                 this.lastFrameChangeTime = System.currentTimeMillis();
            }
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    // TODO: Add methods for movement based on input
}

