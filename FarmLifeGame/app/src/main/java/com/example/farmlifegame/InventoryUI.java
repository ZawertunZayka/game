package com.example.farmlifegame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import java.util.HashMap; // Import HashMap
import java.util.List;    // Import List
import java.util.Map;     // Import Map

// Simple UI component to display the inventory
public class InventoryUI {

    private Inventory inventory;
    private int x, y; // Top-left position of the UI
    private int slotSize = 60; // Size of each inventory slot square
    private int padding = 10; // Padding between slots
    private int columns = 5; // Number of columns in the grid
    private Paint backgroundPaint;
    private Paint slotPaint;
    private Paint textPaint;
    private Bitmap itemSpriteSheet; // Using the icons spritesheet for item icons

    // Map to store icon rectangles for each item (similar to ResourceNode)
    // This needs to be expanded and refined based on spritesheet_icons.png
    private static Map<String, Rect> itemIconRects = new HashMap<>();

    static {
        // Placeholder rects - NEED TO BE CORRECTLY MAPPED from spritesheet_icons.png
        int iconSize = 16;
        // Assuming the same layout as ResourceNode for ores/gems for now
        itemIconRects.put("Stone", new Rect(1 * iconSize, 1 * iconSize, 2 * iconSize, 2 * iconSize));
        itemIconRects.put("Coal", new Rect(0, 0, iconSize, iconSize)); // Placeholder
        itemIconRects.put("Iron Ore", new Rect(2 * iconSize, 1 * iconSize, 3 * iconSize, 2 * iconSize));
        itemIconRects.put("Gold Ore", new Rect(0 * iconSize, 2 * iconSize, 1 * iconSize, 3 * iconSize));
        itemIconRects.put("Emerald", new Rect(1 * iconSize, 2 * iconSize, 2 * iconSize, 3 * iconSize));
        itemIconRects.put("Diamond", new Rect(3 * iconSize, 2 * iconSize, 4 * iconSize, 3 * iconSize));
        // Add rects for tools, seeds, crops etc. later
    }


    public InventoryUI(Context context, Inventory inventory, int x, int y) {
        this.inventory = inventory;
        this.x = x;
        this.y = y;

        backgroundPaint = new Paint();
        backgroundPaint.setColor(Color.DKGRAY);
        backgroundPaint.setAlpha(200);

        slotPaint = new Paint();
        slotPaint.setColor(Color.GRAY);
        slotPaint.setStyle(Paint.Style.STROKE);
        slotPaint.setStrokeWidth(2);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(20);
        textPaint.setTextAlign(Paint.Align.RIGHT);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        itemSpriteSheet = BitmapFactory.decodeResource(context.getResources(), R.drawable.spritesheet_icons, options);
    }

    public void draw(Canvas canvas) {
        int rows = (int) Math.ceil((double) inventory.getCapacity() / columns);
        int totalWidth = columns * (slotSize + padding) + padding;
        int totalHeight = rows * (slotSize + padding) + padding;

        // Draw background
        canvas.drawRect(x, y, x + totalWidth, y + totalHeight, backgroundPaint);

        List<ItemStack> items = inventory.getItems();
        for (int i = 0; i < inventory.getCapacity(); i++) {
            int row = i / columns;
            int col = i % columns;

            int slotX = x + padding + col * (slotSize + padding);
            int slotY = y + padding + row * (slotSize + padding);

            // Draw slot background/border
            canvas.drawRect(slotX, slotY, slotX + slotSize, slotY + slotSize, slotPaint);

            // Draw item icon and quantity if slot is occupied
            if (i < items.size()) {
                ItemStack stack = items.get(i);
                Item item = stack.getItem();
                int quantity = stack.getQuantity();

                // Draw item icon
                Rect sourceRect = itemIconRects.getOrDefault(item.getName(), null); // Get icon rect
                if (itemSpriteSheet != null && sourceRect != null) {
                    Rect destRect = new Rect(slotX + 5, slotY + 5, slotX + slotSize - 5, slotY + slotSize - 5); // Icon slightly smaller than slot
                    canvas.drawBitmap(itemSpriteSheet, sourceRect, destRect, null);
                }

                // Draw quantity text
                if (quantity > 1) {
                    canvas.drawText(String.valueOf(quantity), slotX + slotSize - 5, slotY + slotSize - 5, textPaint);
                }
            }
        }
    }

    // TODO: Add methods for handling clicks on slots (e.g., selecting items)
}

