package com.example.farmlifegame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.HashMap; // Import HashMap
import java.util.List;
import java.util.Map; // Import Map

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private GameThread gameThread;
    private SurfaceHolder surfaceHolder;
    private Tilemap tilemap;
    private Player player;
    private Joystick joystick;
    private List<ResourceNode> resourceNodes; // Add list for resource nodes
    private ItemRegistry itemRegistry; // To get item instances

    private int joystickPointerId = -1;
    private float playerSpeed = 4.0f;

    // Simple interaction button (example)
    private Rect actionButtonRect;
    private Paint actionButtonPaint;
    private boolean actionButtonPressed = false;

    public GameView(Context context) {
        super(context);
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        itemRegistry = new ItemRegistry(context); // Initialize item registry

        // Initialize Tilemap
        tilemap = new Tilemap(context);

        // Initialize Player
        player = new Player(context, 100, 100);

        // Initialize Joystick
        int joystickOuterRadius = 80;
        int joystickInnerRadius = 40;
        // Initial position, will be updated in surfaceChanged
        joystick = new Joystick(joystickOuterRadius + 60, 500, joystickOuterRadius, joystickInnerRadius);

        // Initialize Resource Nodes (example)
        resourceNodes = new ArrayList<>();
        resourceNodes.add(new ResourceNode(context, 200, 200, ResourceType.STONE));
        resourceNodes.add(new ResourceNode(context, 250, 150, ResourceType.IRON));
        resourceNodes.add(new ResourceNode(context, 300, 300, ResourceType.GOLD));

        // Initialize Action Button (example position bottom-right)
        actionButtonPaint = new Paint();
        actionButtonPaint.setColor(Color.RED);
        actionButtonPaint.setAlpha(128);
        // Position will be set in surfaceChanged
        actionButtonRect = new Rect(0, 0, 0, 0);

        gameThread = new GameThread(surfaceHolder, this);
        setFocusable(true);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (gameThread == null || !gameThread.isRunning()) {
             if (gameThread != null) {
                 gameThread.setRunning(false);
                 try { gameThread.join(); } catch (InterruptedException e) { e.printStackTrace(); }
             }
             gameThread = new GameThread(surfaceHolder, this); // Ensure thread is fresh
             gameThread.setRunning(true);
             gameThread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // Update joystick position
        joystick.setCenterPosition(joystick.outerCircleRadius + 60, height - joystick.outerCircleRadius - 60);
        // Update action button position
        int buttonSize = 150;
        actionButtonRect.set(width - buttonSize - 60, height - buttonSize - 60, width - 60, height - 60);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        if (gameThread != null) {
            gameThread.setRunning(false);
            while (retry) {
                try {
                    gameThread.join();
                    retry = false;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        gameThread = null;
    }

    public void update() {
        joystick.update();

        if (player != null) {
            double moveX = joystick.getActuatorX() * playerSpeed;
            double moveY = joystick.getActuatorY() * playerSpeed;

            if (Math.abs(moveX) > 0 || Math.abs(moveY) > 0) { // Only change direction if moving
                 if (Math.abs(moveX) > Math.abs(moveY)) {
                     if (moveX > 0) player.setDirection(3); // Right
                     else player.setDirection(2); // Left
                 } else {
                     if (moveY > 0) player.setDirection(0); // Down
                     else player.setDirection(1); // Up
                 }
            }

            // TODO: Add collision detection
            player.setPosition((int)(player.getX() + moveX), (int)(player.getY() + moveY));
            player.update();
        }

        // Handle interaction button press
        if (actionButtonPressed) {
            handleInteraction();
            actionButtonPressed = false; // Reset after handling
        }

        // TODO: Update resource nodes (e.g., respawn timers)
    }

    private void handleInteraction() {
        if (player == null) return;

        // Simple interaction: check for nearby resource nodes
        Rect playerInteractionBounds = new Rect(player.getX(), player.getY(), player.getX() + player.width, player.getY() + player.height);
        // Extend bounds slightly based on direction for interaction range
        int interactionRange = 10;
        switch (player.direction) {
            case 0: playerInteractionBounds.bottom += interactionRange; break; // Down
            case 1: playerInteractionBounds.top -= interactionRange; break; // Up
            case 2: playerInteractionBounds.left -= interactionRange; break; // Left
            case 3: playerInteractionBounds.right += interactionRange; break; // Right
        }


        for (ResourceNode node : resourceNodes) {
            if (!node.isDepleted() && Rect.intersects(playerInteractionBounds, node.getBounds())) {
                node.hit();
                System.out.println("Hit node: " + node.getType()); // Debug log
                if (node.isDepleted()) {
                    System.out.println("Node depleted: " + node.getType()); // Debug log
                    // Add item to inventory
                    Item droppedItem = itemRegistry.getItemForResource(node.getType());
                    if (droppedItem != null) {
                        boolean added = player.getInventory().addItem(droppedItem, 1);
                        System.out.println("Item added to inventory: " + droppedItem.getName() + " Success: " + added); // Debug log
                    }
                }
                break; // Interact with only one node per button press
            }
        }
        // TODO: Add interaction logic for other things (NPCs, buildings, farming plots)
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (canvas != null) {
            // Draw Tilemap
            if (tilemap != null) {
                tilemap.draw(canvas);
            }

            // Draw Resource Nodes
            for (ResourceNode node : resourceNodes) {
                node.draw(canvas);
            }

            // Draw Player
            if (player != null) {
                player.draw(canvas);
            }

            // Draw Joystick
            joystick.draw(canvas);

            // Draw Action Button
            canvas.drawRect(actionButtonRect, actionButtonPaint);

            // TODO: Draw UI (inventory display, health, etc.)
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (joystick.isPressed(x, y)) {
                    if (joystickPointerId == -1) {
                        joystickPointerId = pointerId;
                        joystick.setIsPressed(true);
                        joystick.setActuator(x, y);
                    }
                } else if (actionButtonRect.contains((int)x, (int)y)) {
                    // Action button pressed
                    actionButtonPressed = true;
                } else {
                    // Handle other touch events
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < event.getPointerCount(); i++) {
                    int currentPointerId = event.getPointerId(i);
                    if (currentPointerId == joystickPointerId) {
                        float currentX = event.getX(i);
                        float currentY = event.getY(i);
                        joystick.setActuator(currentX, currentY);
                        break;
                    }
                }
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                if (pointerId == joystickPointerId) {
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                    joystickPointerId = -1;
                }
                // Reset action button state if needed (handled in update)
                return true;

            case MotionEvent.ACTION_CANCEL:
                 joystick.setIsPressed(false);
                 joystick.resetActuator();
                 joystickPointerId = -1;
                 actionButtonPressed = false;
                 return true;
        }
        return super.onTouchEvent(event);
    }

    public void pause() {
        if (gameThread != null && gameThread.isRunning()) {
             gameThread.setRunning(false);
             boolean retry = true;
             while(retry) {
                 try {
                     gameThread.join();
                     retry = false;
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }
             }
        }
    }

    public void resume() {
        if (gameThread == null || !gameThread.isRunning()) {
            if (surfaceHolder.getSurface().isValid()) {
                 gameThread = new GameThread(surfaceHolder, this); // Recreate if needed
                 gameThread.setRunning(true);
                 gameThread.start();
            }
        }
    }

    // Need a way to get Item instances based on ResourceType
    private static class ItemRegistry {
        private Map<ResourceType, Item> resourceItems = new HashMap<>();
        // Add maps for other item types if needed

        public ItemRegistry(Context context) {
            // Define items corresponding to resources
            // Use placeholder resource IDs for icons for now
            resourceItems.put(ResourceType.STONE, new Item("Stone", ItemType.RESOURCE, R.drawable.spritesheet_icons, "A piece of rock."));
            resourceItems.put(ResourceType.COAL, new Item("Coal", ItemType.RESOURCE, R.drawable.spritesheet_icons, "A lump of coal.")); // Need Coal icon
            resourceItems.put(ResourceType.IRON, new Item("Iron Ore", ItemType.RESOURCE, R.drawable.spritesheet_icons, "Raw iron ore."));
            resourceItems.put(ResourceType.GOLD, new Item("Gold Ore", ItemType.RESOURCE, R.drawable.spritesheet_icons, "Raw gold ore."));
            resourceItems.put(ResourceType.EMERALD, new Item("Emerald", ItemType.RESOURCE, R.drawable.spritesheet_icons, "A green gemstone."));
            resourceItems.put(ResourceType.DIAMOND, new Item("Diamond", ItemType.RESOURCE, R.drawable.spritesheet_icons, "A hard, clear gemstone."));
            // TODO: Assign correct icon rects or individual drawables later
        }

        public Item getItemForResource(ResourceType type) {
            return resourceItems.get(type);
        }
    }
}

