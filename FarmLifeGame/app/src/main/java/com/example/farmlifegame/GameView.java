package com.example.farmlifegame;

import android.content.Context;
<<<<<<< HEAD
import android.content.Intent;
=======
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
<<<<<<< HEAD
import android.util.Log; // Import Log for debugging
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import androidx.activity.result.ActivityResultLauncher;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = "GameView"; // Tag for logging
    private static final String DEFAULT_SAVE_SLOT = "slot1"; // Example save slot

=======
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.util.ArrayList;
import java.util.HashMap; // Import HashMap
import java.util.List;
import java.util.Map; // Import Map

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
    private GameThread gameThread;
    private SurfaceHolder surfaceHolder;
    private Tilemap tilemap;
    private Player player;
    private Joystick joystick;
<<<<<<< HEAD
    private List<ResourceNode> resourceNodes; // List for resource nodes
    private List<FarmPlot> farmPlots; // List for farm plots
    private ItemRegistry itemRegistry; // To get item instances
    private CropRegistry cropRegistry; // To get crop instances
    private SaveGameManager saveGameManager; // Add SaveGameManager
    private Context context; // Store context
    private ActivityResultLauncher<Intent> shopLauncher; // Launcher for ShopActivity

    private int joystickPointerId = -1;
    private float playerSpeed = 2.0f; // Adjusted speed

    // Simple interaction button
=======
    private List<ResourceNode> resourceNodes; // Add list for resource nodes
    private ItemRegistry itemRegistry; // To get item instances

    private int joystickPointerId = -1;
    private float playerSpeed = 4.0f;

    // Simple interaction button (example)
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
    private Rect actionButtonRect;
    private Paint actionButtonPaint;
    private boolean actionButtonPressed = false;

<<<<<<< HEAD
    // Game State
    private enum GameState { LOADING, RUNNING, PAUSED, INVENTORY, SHOP_TRANSITION, FISHING_MINIGAME /* Add other states */ }
    private GameState currentState = GameState.LOADING;

    // Fishing Minigame variables
    private FishingMinigame fishingMinigame;
    private long fishingStartTime = 0;
    private static final long FISHING_CAST_TIME = 1500; // Time to wait before bite
    private static final long FISHING_BITE_WINDOW = 1000; // Time window to react to bite
    private boolean fishingWaitingForBite = false;
    private boolean fishingBiteOccurred = false;
    private Random random = new Random();

    // Shop interaction placeholder
    private Rect shopTriggerArea; // Example: A rectangle representing the shop entrance

    public GameView(Context context) {
        super(context);
        this.context = context; // Store context
=======
    public GameView(Context context) {
        super(context);
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        itemRegistry = new ItemRegistry(context); // Initialize item registry
<<<<<<< HEAD
        cropRegistry = new CropRegistry(context, itemRegistry); // Initialize crop registry
        saveGameManager = new SaveGameManager(context); // Initialize SaveGameManager

        // Initialization moved to newGame() / loadGame()

        gameThread = new GameThread(surfaceHolder, this);
        setFocusable(true);
    }

    // Method for GameActivity to set the launcher
    public void setShopLauncher(ActivityResultLauncher<Intent> launcher) {
        this.shopLauncher = launcher;
    }

    // Called from GameActivity when starting a new game
    public void newGame() {
        Log.d(TAG, "Initializing new game...");
=======

>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
        // Initialize Tilemap
        tilemap = new Tilemap(context);

        // Initialize Player
        player = new Player(context, 100, 100);
<<<<<<< HEAD
        Item pickaxe = itemRegistry.getItemByName("Basic Pickaxe");
        Item hoe = itemRegistry.getItemByName("Basic Hoe");
        Item can = itemRegistry.getItemByName("Watering Can");
        Item rod = itemRegistry.getItemByName("Fishing Rod");
        Item seeds = itemRegistry.getItemByName("Turnip Seeds");
        if (pickaxe != null) player.getInventory().addItem(pickaxe, 1);
        if (hoe != null) player.getInventory().addItem(hoe, 1);
        if (can != null) player.getInventory().addItem(can, 1);
        if (rod != null) player.getInventory().addItem(rod, 1);
        if (seeds != null) player.getInventory().addItem(seeds, 5); // Give some starting seeds
        player.setEquippedTool(player.getInventory().getItemStack(0)); // Equip pickaxe initially
        player.setCurrency(500); // Starting currency
=======
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f

        // Initialize Joystick
        int joystickOuterRadius = 80;
        int joystickInnerRadius = 40;
<<<<<<< HEAD
=======
        // Initial position, will be updated in surfaceChanged
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
        joystick = new Joystick(joystickOuterRadius + 60, 500, joystickOuterRadius, joystickInnerRadius);

        // Initialize Resource Nodes (example)
        resourceNodes = new ArrayList<>();
        resourceNodes.add(new ResourceNode(context, 200, 200, ResourceType.STONE));
        resourceNodes.add(new ResourceNode(context, 250, 150, ResourceType.IRON));
        resourceNodes.add(new ResourceNode(context, 300, 300, ResourceType.GOLD));

<<<<<<< HEAD
        // Initialize Farm Plots (example area)
        farmPlots = new ArrayList<>();
        int farmStartX = 5; // Tile X
        int farmStartY = 5; // Tile Y
        int farmWidth = 3;
        int farmHeight = 3;
        for (int y = farmStartY; y < farmStartY + farmHeight; y++) {
            for (int x = farmStartX; x < farmStartX + farmWidth; x++) {
                int baseTile = tilemap.getTileType(x, y);
                if (baseTile == 0 || baseTile == 1) { // Assuming 0=grass, 1=dirt
                    farmPlots.add(new FarmPlot(context, tilemap, x, y));
                }
            }
        }

        // Initialize Action Button
        actionButtonPaint = new Paint();
        actionButtonPaint.setColor(Color.DKGRAY);
        actionButtonPaint.setAlpha(180);
        actionButtonRect = new Rect(0, 0, 0, 0);

        // Initialize Shop Trigger Area (Example: top-right corner tile)
        int shopTileX = tilemap.getMapWidthTiles() - 2;
        int shopTileY = 1;
        int tileSize = tilemap.getTileSize();
        shopTriggerArea = new Rect(shopTileX * tileSize, shopTileY * tileSize,
                                 (shopTileX + 1) * tileSize, (shopTileY + 1) * tileSize);
        // TODO: Maybe draw something here to indicate the shop

        currentState = GameState.RUNNING;
        Log.d(TAG, "New game initialized.");
    }

    // Called from GameActivity when loading a game
    public void loadGame() {
        Log.d(TAG, "Attempting to load game from slot: " + DEFAULT_SAVE_SLOT);
        GameData loadedData = saveGameManager.loadGame(DEFAULT_SAVE_SLOT);

        if (loadedData != null) {
            Log.i(TAG, "Game data loaded successfully. Applying state...");
            // Apply loaded data
            tilemap = new Tilemap(context);
            if (loadedData.getTilemapData() != null) {
                tilemap.setMapData(loadedData.getTilemapData());
            }

            player = new Player(context, loadedData.getPlayerX(), loadedData.getPlayerY());
            player.setDirection(loadedData.getPlayerDirection());
            player.setCurrency(loadedData.getPlayerCurrency());
            player.setExperience(loadedData.getPlayerExperience());
            player.setLevel(loadedData.getPlayerLevel());
            if (loadedData.getPlayerInventoryItems() != null) {
                Inventory loadedInventory = new Inventory(player.getInventory().getCapacity());
                List<ItemStack> reconstructedItems = new ArrayList<>(loadedInventory.getCapacity());
                for (int i = 0; i < loadedInventory.getCapacity(); i++) {
                    ItemStack savedStack = (i < loadedData.getPlayerInventoryItems().size()) ? loadedData.getPlayerInventoryItems().get(i) : null;
                    if (savedStack != null && savedStack.getItem() != null) {
                        Item item = itemRegistry.getItemByName(savedStack.getItem().getName());
                        if (item != null) {
                            ItemStack stack = new ItemStack(item, savedStack.getQuantity());
                            // TODO: Restore durability if it's a tool
                            reconstructedItems.add(stack);
                        } else {
                            Log.w(TAG, "Could not find item in registry: " + savedStack.getItem().getName());
                            reconstructedItems.add(null);
                        }
                    } else {
                        reconstructedItems.add(null);
                    }
                }
                loadedInventory.setItems(reconstructedItems);
                player.setInventory(loadedInventory);
                // TODO: Restore equipped tool based on saved data (e.g., index)
            } else {
                 player.getInventory().clear();
            }
            player.loadSprites(context); // Reload transient sprites

            int joystickOuterRadius = 80;
            int joystickInnerRadius = 40;
            joystick = new Joystick(joystickOuterRadius + 60, 500, joystickOuterRadius, joystickInnerRadius);

            // Initialize Resource Nodes and apply saved state
            resourceNodes = new ArrayList<>();
            // TODO: Determine node locations dynamically or from map data
            resourceNodes.add(new ResourceNode(context, 200, 200, ResourceType.STONE));
            resourceNodes.add(new ResourceNode(context, 250, 150, ResourceType.IRON));
            resourceNodes.add(new ResourceNode(context, 300, 300, ResourceType.GOLD));
            if (loadedData.getResourceNodeStates() != null) {
                Map<String, ResourceNode.ResourceNodeState> stateMap = new HashMap<>();
                for (ResourceNode.ResourceNodeState state : loadedData.getResourceNodeStates()) {
                    stateMap.put(state.x + "-" + state.y + "-" + state.type, state);
                }
                for (ResourceNode node : resourceNodes) {
                    ResourceNode.ResourceNodeState state = stateMap.get(node.getX() + "-" + node.getY() + "-" + node.getType());
                    if (state != null) {
                        node.applyState(state);
                        node.loadSprite(context);
                    }
                }
            }

            // Initialize Farm Plots and apply saved state
            farmPlots = new ArrayList<>();
            // TODO: Determine plot locations dynamically or from map data
            int farmStartX = 5, farmStartY = 5, farmWidth = 3, farmHeight = 3;
            for (int y = farmStartY; y < farmStartY + farmHeight; y++) {
                for (int x = farmStartX; x < farmStartX + farmWidth; x++) {
                    int baseTile = tilemap.getTileType(x, y);
                    if (baseTile == 0 || baseTile == 1) {
                        farmPlots.add(new FarmPlot(context, tilemap, x, y));
                    }
                }
            }
            if (loadedData.getFarmPlotStates() != null) {
                 Map<String, FarmPlot.FarmPlotState> plotStateMap = new HashMap<>();
                 for(FarmPlot.FarmPlotState state : loadedData.getFarmPlotStates()) {
                     plotStateMap.put(state.tileX + "-" + state.tileY, state);
                 }
                 for (FarmPlot plot : farmPlots) {
                     FarmPlot.FarmPlotState state = plotStateMap.get(plot.getTileX() + "-" + plot.getTileY());
                     if (state != null) {
                         plot.applySaveState(state, cropRegistry.getAllCrops(), context);
                         plot.loadSprites(context); // Load sprites after applying state
                     }
                 }
            }

            actionButtonPaint = new Paint();
            actionButtonPaint.setColor(Color.DKGRAY);
            actionButtonPaint.setAlpha(180);
            actionButtonRect = new Rect(0, 0, 0, 0);

            // Initialize Shop Trigger Area (same as new game for now)
            int shopTileX = tilemap.getMapWidthTiles() - 2;
            int shopTileY = 1;
            int tileSize = tilemap.getTileSize();
            shopTriggerArea = new Rect(shopTileX * tileSize, shopTileY * tileSize,
                                     (shopTileX + 1) * tileSize, (shopTileY + 1) * tileSize);

            currentState = GameState.RUNNING;
            Log.i(TAG, "Game state applied from loaded data.");
        } else {
            Log.w(TAG, "Failed to load game data or no save file found. Starting new game instead.");
            newGame(); // Fallback to new game if load fails
        }
    }

    // Method to gather current game state into a GameData object
    private GameData gatherGameData() {
        if (player == null || tilemap == null || resourceNodes == null || farmPlots == null) {
            Log.e(TAG, "Cannot gather game data, core components missing.");
            return null;
        }
        GameData data = new GameData();

        // Player Data
        data.setPlayerX(player.getX());
        data.setPlayerY(player.getY());
        data.setPlayerDirection(player.getDirection());
        data.setPlayerCurrency(player.getCurrency());
        data.setPlayerExperience(player.getExperience());
        data.setPlayerLevel(player.getLevel());
        // TODO: Need to save ItemStack state properly (e.g., tool durability)
        data.setPlayerInventoryItems(player.getInventory().getItems());
        // TODO: Save equipped tool index

        // World Data
        data.setTilemapData(tilemap.getMapData());
        List<ResourceNode.ResourceNodeState> nodeStates = resourceNodes.stream()
                                                                    .map(ResourceNode::getState)
                                                                    .collect(Collectors.toList());
        data.setResourceNodeStates(nodeStates);
        List<FarmPlot.FarmPlotState> plotStates = farmPlots.stream()
                                                            .map(FarmPlot::getSaveState)
                                                            .collect(Collectors.toList());
        data.setFarmPlotStates(plotStates);
        // TODO: Save fishing state

        Log.d(TAG, "Game data gathered.");
        return data;
    }

    // Method to trigger saving the game
    public void saveGame() {
        // Only save if the game is in a state where saving makes sense
        if (currentState == GameState.RUNNING || currentState == GameState.PAUSED) {
            Log.d(TAG, "Attempting to save game to slot: " + DEFAULT_SAVE_SLOT);
            GameData dataToSave = gatherGameData();
            if (dataToSave != null) {
                boolean success = saveGameManager.saveGame(dataToSave, DEFAULT_SAVE_SLOT);
                if (success) {
                    Log.i(TAG, "Game saved successfully.");
                } else {
                    Log.e(TAG, "Failed to save game.");
                }
            } else {
                Log.e(TAG, "Could not gather data to save.");
            }
        } else {
            Log.w(TAG, "Skipping save because current state is: " + currentState);
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "Surface created.");
        if (gameThread == null || !gameThread.isRunning()) {
             if (gameThread != null) {
                 gameThread.setRunning(false);
                 try { gameThread.join(); } catch (InterruptedException e) { Log.e(TAG, "Error joining thread", e); }
             }
             gameThread = new GameThread(surfaceHolder, this);
             // Start thread only if not loading initially
             if (currentState != GameState.LOADING) {
                 gameThread.setRunning(true);
                 gameThread.start();
             }
=======
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
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
<<<<<<< HEAD
        Log.d(TAG, "Surface changed: w=" + width + ", h=" + height);
        if (joystick != null) {
            joystick.setCenterPosition(joystick.outerCircleRadius + 60, height - joystick.outerCircleRadius - 60);
        }
        if (actionButtonRect != null) {
            int buttonSize = 150;
            actionButtonRect.set(width - buttonSize - 60, height - buttonSize - 60, width - 60, height - 60);
        }
        if (fishingMinigame != null) {
            fishingMinigame.setScreenSize(width, height);
        }
=======
        // Update joystick position
        joystick.setCenterPosition(joystick.outerCircleRadius + 60, height - joystick.outerCircleRadius - 60);
        // Update action button position
        int buttonSize = 150;
        actionButtonRect.set(width - buttonSize - 60, height - buttonSize - 60, width - 60, height - 60);
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
<<<<<<< HEAD
        Log.d(TAG, "Surface destroyed.");
        pause();
=======
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
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
        gameThread = null;
    }

    public void update() {
<<<<<<< HEAD
        long currentTime = System.currentTimeMillis();

        switch (currentState) {
            case RUNNING:
                updateRunningState();
                break;
            case FISHING_MINIGAME:
                updateFishingMinigameState(currentTime);
                break;
            // Handle other states
            case PAUSED:
            case LOADING:
            case INVENTORY:
            case SHOP_TRANSITION: // No updates while transitioning
                // No updates needed or handled elsewhere
                break;
        }
    }

    private void updateRunningState() {
        if (joystick != null) {
            joystick.update();
        }

        boolean playerIsMoving = false;
        if (player != null && joystick != null) {
            double moveX = joystick.getActuatorX() * playerSpeed;
            double moveY = joystick.getActuatorY() * playerSpeed;

            if (Math.abs(moveX) > 0.1 || Math.abs(moveY) > 0.1) {
                playerIsMoving = true;
=======
        joystick.update();

        if (player != null) {
            double moveX = joystick.getActuatorX() * playerSpeed;
            double moveY = joystick.getActuatorY() * playerSpeed;

            if (Math.abs(moveX) > 0 || Math.abs(moveY) > 0) { // Only change direction if moving
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
                 if (Math.abs(moveX) > Math.abs(moveY)) {
                     if (moveX > 0) player.setDirection(3); // Right
                     else player.setDirection(2); // Left
                 } else {
                     if (moveY > 0) player.setDirection(0); // Down
                     else player.setDirection(1); // Up
                 }
<<<<<<< HEAD

                int currentX = player.getX();
                int currentY = player.getY();
                int nextX = (int)(currentX + moveX);
                int nextY = (int)(currentY + moveY);

                if (!isCollision(nextX, currentY, player.getWidth(), player.getHeight())) {
                    player.setPosition(nextX, currentY);
                }
                currentX = player.getX();
                if (!isCollision(currentX, nextY, player.getWidth(), player.getHeight())) {
                    player.setPosition(currentX, nextY);
                }
            }
        }

        if (player != null) {
            player.setMoving(playerIsMoving);
            player.update();
        }

        // Update resource nodes (for respawning)
        if (resourceNodes != null) {
            for (ResourceNode node : resourceNodes) {
                node.update();
            }
        }

        // Update farm plots (for growth)
        if (farmPlots != null) {
            for (FarmPlot plot : farmPlots) {
                plot.update();
            }
        }

        if (actionButtonPressed) {
            handleInteraction();
            actionButtonPressed = false;
        }

        // TODO: Update time manager
    }

    private void updateFishingMinigameState(long currentTime) {
        if (fishingMinigame != null) {
            fishingMinigame.update();
            if (fishingMinigame.isFinished()) {
                Item caughtFish = fishingMinigame.getResult();
                if (caughtFish != null) {
                    Log.i(TAG, "Caught a " + caughtFish.getName() + "!");
                    int notAdded = player.getInventory().addItem(caughtFish, 1);
                    if (notAdded > 0) {
                        Log.w(TAG, "Inventory full, could not add fish.");
                    }
                    player.addExperience(20); // XP for fishing
                } else {
                    Log.i(TAG, "The fish got away...");
                }
                // Exit minigame
                fishingMinigame = null;
                currentState = GameState.RUNNING;
            }
        } else {
            // Simple bite mechanic (placeholder for actual minigame)
            if (fishingWaitingForBite) {
                if (currentTime - fishingStartTime > FISHING_CAST_TIME) {
                    // Bite occurs randomly within the cast time
                    if (!fishingBiteOccurred && random.nextInt(100) < 5) { // 5% chance per update tick after cast time?
                        fishingBiteOccurred = true;
                        fishingStartTime = currentTime; // Reset timer for reaction window
                        Log.d(TAG, "!!! BITE !!!");
                        // TODO: Add visual/audio cue
                    }
                }
                if (fishingBiteOccurred && currentTime - fishingStartTime > FISHING_BITE_WINDOW) {
                    // Missed the bite
                    Log.d(TAG, "Missed the bite.");
                    fishingWaitingForBite = false;
                    fishingBiteOccurred = false;
                    currentState = GameState.RUNNING;
                }
            }
        }
    }

    private boolean isCollision(int x, int y, int width, int height) {
        if (tilemap == null) return false;
        int tileSize = tilemap.getTileSize();
        if (tileSize <= 0) return false;

        int collisionBoxInsetX = 2;
        int collisionBoxInsetY = 4;
        int boxLeft = x + collisionBoxInsetX;
        int boxRight = x + width - collisionBoxInsetX;
        int boxTop = y + collisionBoxInsetY;
        int boxBottom = y + height - collisionBoxInsetY / 2;

        int topLeftTileX = boxLeft / tileSize;
        int topLeftTileY = boxTop / tileSize;
        int topRightTileX = boxRight / tileSize;
        int topRightTileY = boxTop / tileSize;
        int bottomLeftTileX = boxLeft / tileSize;
        int bottomLeftTileY = boxBottom / tileSize;
        int bottomRightTileX = boxRight / tileSize;
        int bottomRightTileY = boxBottom / tileSize;

        if (tilemap.isSolid(topLeftTileX, topLeftTileY) ||
            tilemap.isSolid(topRightTileX, topRightTileY) ||
            tilemap.isSolid(bottomLeftTileX, bottomLeftTileY) ||
            tilemap.isSolid(bottomRightTileX, bottomRightTileY)) {
            return true;
        }

        // Check collision with shop trigger area (treat as solid for now)
        // Rect playerRect = new Rect(x, y, x + width, y + height);
        // if (shopTriggerArea != null && Rect.intersects(playerRect, shopTriggerArea)) {
        //     return true; // Prevent walking onto the shop trigger tile?
        // }

        // TODO: Add collision checks with other objects (ResourceNodes, NPCs, FarmPlots?)

        return false;
    }

    private void handleInteraction() {
        if (player == null || tilemap == null) return;

        Rect playerInteractionBounds = player.getInteractionBounds();
        int targetTileX = playerInteractionBounds.centerX() / tilemap.getTileSize();
        int targetTileY = playerInteractionBounds.centerY() / tilemap.getTileSize();
        Log.d(TAG, "Interaction triggered. Bounds: " + playerInteractionBounds.toShortString() + " Target Tile: (" + targetTileX + "," + targetTileY + ")");

        ItemStack equippedStack = player.getEquippedTool();
        Item equippedItem = (equippedStack != null) ? equippedStack.getItem() : null;
        Tool equippedTool = (equippedItem instanceof Tool) ? (Tool) equippedItem : null;

        // 0. Check Shop Trigger
        if (shopTriggerArea != null && Rect.intersects(playerInteractionBounds, shopTriggerArea)) {
            Log.d(TAG, "Player interacting with shop trigger area.");
            launchShop();
            return; // Prioritize shop interaction
        }

        // 1. Check if facing water with Fishing Rod
        int facingTileX = targetTileX;
        int facingTileY = targetTileY;
        // Adjust target tile based on player direction (crude)
        switch (player.getDirection()) {
            case 0: facingTileY++; break; // Down
            case 1: facingTileY--; break; // Up
            case 2: facingTileX--; break; // Left
            case 3: facingTileX++; break; // Right
        }
        int facingTileType = tilemap.getTileType(facingTileX, facingTileY);
        if (equippedTool != null && equippedTool.getName().contains("Fishing Rod") && facingTileType == 2 /* Water */) {
            Log.d(TAG, "Attempting to fish in water tile (" + facingTileX + "," + facingTileY + ")");
            startFishing();
            return; // Start fishing, don't check other interactions
        }

        // 2. Check Resource Nodes
        if (resourceNodes != null) {
            for (ResourceNode node : resourceNodes) {
                if (!node.isDepleted() && Rect.intersects(playerInteractionBounds, node.getBounds())) {
                    if (equippedTool != null /* && tool matches node type */) {
                        // TODO: Check specific tool type (Pickaxe, Axe etc.) against node type
                        int damage = (int) (1 * equippedTool.getEfficiency());
                        node.hit(damage);
                        equippedTool.use();
                        if (equippedTool.isBroken()) {
                            Log.i(TAG, equippedTool.getName() + " broke!");
                            player.setEquippedTool(null);
                        }

                        Log.d(TAG, "Hit node: " + node.getType() + " Health: " + node.getCurrentHealth());
                        if (node.isDepleted()) {
                            Log.d(TAG, "Node depleted: " + node.getType());
                            Item droppedItem = itemRegistry.getItemForResource(node.getType());
                            if (droppedItem != null) {
                                int notAdded = player.getInventory().addItem(droppedItem, 1);
                                if (notAdded == 0) {
                                    Log.d(TAG, "Item added to inventory: " + droppedItem.getName());
                                    player.addExperience(10); // Grant XP
                                } else {
                                    Log.w(TAG, "Inventory full, could not add " + droppedItem.getName());
                                }
                            }
                        }
                        return; // Interact with only one thing
                    } else {
                        Log.d(TAG, "Cannot interact with node " + node.getType() + ", appropriate tool not equipped.");
                        return;
                    }
                }
            }
        }

        // 3. Check Farm Plots
        if (farmPlots != null) {
            FarmPlot targetPlot = null;
            for (FarmPlot plot : farmPlots) {
                if (plot.getTileX() == targetTileX && plot.getTileY() == targetTileY) {
                    targetPlot = plot;
                    break;
                }
            }

            if (targetPlot != null) {
                Log.d(TAG, "Interacting with FarmPlot at (" + targetTileX + "," + targetTileY + "). State: " + targetPlot.getState());
                // Tilling with Hoe
                if (equippedTool != null && equippedTool.getName().contains("Hoe") && targetPlot.getState() == FarmPlot.PlotState.UNTILLED) {
                    if (targetPlot.till()) {
                        equippedTool.use();
                        if (equippedTool.isBroken()) player.setEquippedTool(null);
                        player.addExperience(2); // XP for tilling
                    }
                    return;
                }
                // Planting with Seeds
                if (equippedItem != null && equippedItem.getType() == ItemType.SEED && targetPlot.getState() == FarmPlot.PlotState.TILLED) {
                    Crop cropToPlant = cropRegistry.getCropForSeed(equippedItem);
                    if (cropToPlant != null) {
                        if (targetPlot.plant(cropToPlant, context)) {
                            player.getInventory().removeItem(equippedItem, 1); // Consume seed
                            player.addExperience(5); // XP for planting
                        }
                    }
                    return;
                }
                // Watering with Watering Can
                if (equippedTool != null && equippedTool.getName().contains("Watering Can") && (targetPlot.getState() == FarmPlot.PlotState.TILLED || targetPlot.getState() == FarmPlot.PlotState.PLANTED)) {
                     if (targetPlot.water()) {
                         // equippedTool.use();
                     }
                     return;
                }
                // Harvesting
                if (targetPlot.getState() == FarmPlot.PlotState.PLANTED) {
                    Item harvestedItem = targetPlot.harvest();
                    if (harvestedItem != null) {
                        int notAdded = player.getInventory().addItem(harvestedItem, 1);
                        if (notAdded == 0) {
                            Log.d(TAG, "Harvested item added to inventory: " + harvestedItem.getName());
                            player.addExperience(15); // XP for harvesting
                        } else {
                            Log.w(TAG, "Inventory full, could not add harvested item: " + harvestedItem.getName());
                        }
                    }
                    return;
                }
            }
        }

        // 4. TODO: Check NPCs

        Log.d(TAG, "No specific interaction target found in range.");
    }

    // Method to launch the ShopActivity
    private void launchShop() {
        if (shopLauncher == null) {
            Log.e(TAG, "ShopLauncher is not set. Cannot open shop.");
            return;
        }
        if (player == null) {
            Log.e(TAG, "Player object is null. Cannot open shop.");
            return;
        }

        Log.i(TAG, "Launching ShopActivity...");
        currentState = GameState.SHOP_TRANSITION; // Pause updates while shop is open

        Intent intent = new Intent(context, ShopActivity.class);
        // Create PlayerData to pass (simplified version)
        ShopActivity.PlayerData currentPlayerData = new ShopActivity.PlayerData(
                player.getCurrency(),
                player.getInventory().getItems() // Pass a copy or ensure ItemStack is Serializable
        );
        intent.putExtra(ShopActivity.EXTRA_PLAYER_DATA, currentPlayerData);
        shopLauncher.launch(intent);
    }

    // Method called by GameActivity when ShopActivity returns a result
    public void applyShopResult(ShopActivity.PlayerData updatedPlayerData) {
        Log.d(TAG, "Applying shop result.");
        if (player != null && updatedPlayerData != null) {
            player.setCurrency(updatedPlayerData.getPlayerCurrency());
            // Need to carefully update inventory based on the returned list
            // This assumes the returned list IS the new inventory state
            player.getInventory().setItems(updatedPlayerData.getPlayerInventoryItems());
            Log.i(TAG, "Player currency and inventory updated from shop result.");
        } else {
            Log.e(TAG, "Cannot apply shop result: Player or updatedPlayerData is null.");
        }
        // Resume game running state
        currentState = GameState.RUNNING;
    }

    private void startFishing() {
        // Simple bite mechanic for now
        currentState = GameState.FISHING_MINIGAME;
        fishingStartTime = System.currentTimeMillis();
        fishingWaitingForBite = true;
        fishingBiteOccurred = false;
        Log.i(TAG, "Cast line... waiting for a bite.");

        // TODO: Replace with actual minigame initialization
        // fishingMinigame = new FishingMinigame(context, itemRegistry.getRandomFish());
        // fishingMinigame.setScreenSize(getWidth(), getHeight());
    }

    private void handleFishingAction() {
        if (currentState == GameState.FISHING_MINIGAME) {
            if (fishingMinigame != null) {
                fishingMinigame.handleTap();
            } else {
                // Simple bite mechanic reaction
                if (fishingWaitingForBite && fishingBiteOccurred) {
                    Log.i(TAG, "Hooked!");
                    // Success! Catch a fish
                    Item caughtFish = itemRegistry.getRandomFish();
                    if (caughtFish != null) {
                        Log.i(TAG, "Caught a " + caughtFish.getName() + "!");
                        int notAdded = player.getInventory().addItem(caughtFish, 1);
                        if (notAdded > 0) {
                            Log.w(TAG, "Inventory full, could not add fish.");
                        }
                        player.addExperience(20); // XP for fishing
                    } else {
                        Log.w(TAG, "No fish available to catch?");
                    }
                    fishingWaitingForBite = false;
                    fishingBiteOccurred = false;
                    currentState = GameState.RUNNING;
                } else if (fishingWaitingForBite) {
                    // Pulled back too early
                    Log.d(TAG, "Pulled back too soon.");
                    fishingWaitingForBite = false;
                    fishingBiteOccurred = false;
                    currentState = GameState.RUNNING;
                }
            }
        }
=======
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
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
<<<<<<< HEAD
        if (canvas == null) return;

        canvas.drawColor(Color.rgb(135, 206, 235)); // Light Sky Blue background

        switch (currentState) {
            case LOADING:
                drawLoadingScreen(canvas);
                break;
            case RUNNING:
            case PAUSED:
            case SHOP_TRANSITION: // Draw world while transitioning
                drawWorldScreen(canvas);
                if (currentState == GameState.PAUSED) drawPauseOverlay(canvas);
                break;
            case FISHING_MINIGAME:
                drawWorldScreen(canvas); // Draw world behind minigame
                drawFishingMinigameScreen(canvas);
                break;
            // Draw other states (Inventory)
        }
    }

    private void drawLoadingScreen(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(50);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("Loading...", canvas.getWidth() / 2f, canvas.getHeight() / 2f, paint);
    }

    private void drawWorldScreen(Canvas canvas) {
        int cameraX = 0; // Placeholder
        int cameraY = 0; // Placeholder
        canvas.save();
        canvas.translate(-cameraX, -cameraY);

        if (tilemap != null) {
            tilemap.draw(canvas);
        }
        if (farmPlots != null) {
            for (FarmPlot plot : farmPlots) {
                plot.draw(canvas);
            }
        }
        if (resourceNodes != null) {
            for (ResourceNode node : resourceNodes) {
                node.draw(canvas);
            }
        }
        // Draw shop trigger indicator (optional)
        if (shopTriggerArea != null) {
            Paint shopPaint = new Paint();
            shopPaint.setColor(Color.argb(100, 255, 255, 0)); // Semi-transparent yellow
            canvas.drawRect(shopTriggerArea, shopPaint);
        }
        if (player != null) {
            player.draw(canvas);
        }
        // TODO: Draw other entities

        canvas.restore();

        // --- Draw UI Elements ---
        if (joystick != null) {
            joystick.draw(canvas);
        }
        if (actionButtonRect != null && actionButtonPaint != null) {
            canvas.drawRect(actionButtonRect, actionButtonPaint);
            Paint textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(40);
            textPaint.setTextAlign(Paint.Align.CENTER);
            canvas.drawText("ACT", actionButtonRect.centerX(), actionButtonRect.centerY() + 15, textPaint);
        }
        // Draw Currency UI
        if (player != null) {
            Paint currencyPaint = new Paint();
            currencyPaint.setColor(Color.YELLOW);
            currencyPaint.setTextSize(40);
            currencyPaint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText("$: " + player.getCurrency(), 20, 50, currencyPaint);
        }
        // TODO: Draw Inventory Icon/Button, Clock, XP Bar
    }

    private void drawPauseOverlay(Canvas canvas) {
        Paint pausePaint = new Paint();
        pausePaint.setColor(Color.argb(150, 0, 0, 0));
        canvas.drawRect(0, 0, canvas.getWidth(), canvas.getHeight(), pausePaint);
        pausePaint.setColor(Color.WHITE);
        pausePaint.setTextSize(80);
        pausePaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("PAUSED", canvas.getWidth() / 2f, canvas.getHeight() / 2f, pausePaint);
    }

    private void drawFishingMinigameScreen(Canvas canvas) {
        if (fishingMinigame != null) {
            fishingMinigame.draw(canvas);
        } else {
            // Simple bite mechanic UI
            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(60);
            paint.setTextAlign(Paint.Align.CENTER);
            float centerX = canvas.getWidth() / 2f;
            float centerY = canvas.getHeight() / 3f;

            if (fishingWaitingForBite) {
                if (fishingBiteOccurred) {
                    paint.setColor(Color.RED);
                    canvas.drawText("!!! BITE !!!", centerX, centerY, paint);
                } else {
                    canvas.drawText("Fishing...", centerX, centerY, paint);
                }
            }
=======
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
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        int pointerIndex = event.getActionIndex();
        int pointerId = event.getPointerId(pointerIndex);
        float x = event.getX(pointerIndex);
        float y = event.getY(pointerIndex);

<<<<<<< HEAD
        switch (currentState) {
            case RUNNING:
                return handleRunningTouchEvent(event, action, pointerId, x, y);
            case PAUSED:
                if (action == MotionEvent.ACTION_DOWN) {
                    resume();
                    return true;
                }
                break;
            case FISHING_MINIGAME:
                return handleFishingTouchEvent(event, action, pointerId, x, y);
            case SHOP_TRANSITION: // Ignore touch while transitioning
                return true;
            // Handle other states
        }
        return true;
    }

    private boolean handleRunningTouchEvent(MotionEvent event, int action, int pointerId, float x, float y) {
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (joystick != null && joystick.isPressed(x, y)) {
=======
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                if (joystick.isPressed(x, y)) {
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
                    if (joystickPointerId == -1) {
                        joystickPointerId = pointerId;
                        joystick.setIsPressed(true);
                        joystick.setActuator(x, y);
                    }
<<<<<<< HEAD
                } else if (actionButtonRect != null && actionButtonRect.contains((int)x, (int)y)) {
                    actionButtonPressed = true;
                } else {
                    // Handle other UI touches
=======
                } else if (actionButtonRect.contains((int)x, (int)y)) {
                    // Action button pressed
                    actionButtonPressed = true;
                } else {
                    // Handle other touch events
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
                }
                return true;

            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < event.getPointerCount(); i++) {
                    int currentPointerId = event.getPointerId(i);
<<<<<<< HEAD
                    if (currentPointerId == joystickPointerId && joystick != null) {
=======
                    if (currentPointerId == joystickPointerId) {
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
                        float currentX = event.getX(i);
                        float currentY = event.getY(i);
                        joystick.setActuator(currentX, currentY);
                        break;
                    }
                }
                return true;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
<<<<<<< HEAD
                if (pointerId == joystickPointerId && joystick != null) {
=======
                if (pointerId == joystickPointerId) {
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
                    joystick.setIsPressed(false);
                    joystick.resetActuator();
                    joystickPointerId = -1;
                }
<<<<<<< HEAD
                return true;

            case MotionEvent.ACTION_CANCEL:
                 if (joystick != null) {
                     joystick.setIsPressed(false);
                     joystick.resetActuator();
                 }
=======
                // Reset action button state if needed (handled in update)
                return true;

            case MotionEvent.ACTION_CANCEL:
                 joystick.setIsPressed(false);
                 joystick.resetActuator();
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
                 joystickPointerId = -1;
                 actionButtonPressed = false;
                 return true;
        }
<<<<<<< HEAD
        return false;
    }

    private boolean handleFishingTouchEvent(MotionEvent event, int action, int pointerId, float x, float y) {
         if (action == MotionEvent.ACTION_DOWN) {
             handleFishingAction(); // Trigger reel-in on tap
             return true;
         }
         return false;
    }

    public void pause() {
        Log.d(TAG, "Pausing game.");
        // Save game before pausing
        saveGame();
        // Set paused state
        currentState = GameState.PAUSED;
        // Stop the game thread
=======
        return super.onTouchEvent(event);
    }

    public void pause() {
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
        if (gameThread != null && gameThread.isRunning()) {
             gameThread.setRunning(false);
             boolean retry = true;
             while(retry) {
                 try {
                     gameThread.join();
                     retry = false;
                 } catch (InterruptedException e) {
<<<<<<< HEAD
                     Log.e(TAG, "Error joining thread on pause", e);
=======
                     e.printStackTrace();
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
                 }
             }
        }
    }

    public void resume() {
<<<<<<< HEAD
        Log.d(TAG, "Resuming game.");
        // Only resume if paused
        if (currentState == GameState.PAUSED) {
            currentState = GameState.RUNNING; // Set back to running
            if (gameThread == null || !gameThread.isRunning()) {
                if (surfaceHolder.getSurface().isValid()) {
                     // Reload transient resources
                     if (player != null) player.loadSprites(context);
                     if (resourceNodes != null) {
                         for(ResourceNode node : resourceNodes) node.loadSprite(context);
                     }
                     if (farmPlots != null) {
                         for(FarmPlot plot : farmPlots) plot.loadSprites(context);
                     }
                     // TODO: Reload other transient resources

                     gameThread = new GameThread(surfaceHolder, this);
                     gameThread.setRunning(true);
                     gameThread.start();
                }
=======
        if (gameThread == null || !gameThread.isRunning()) {
            if (surfaceHolder.getSurface().isValid()) {
                 gameThread = new GameThread(surfaceHolder, this); // Recreate if needed
                 gameThread.setRunning(true);
                 gameThread.start();
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
            }
        }
    }

<<<<<<< HEAD
    // --- Item Registry Inner Class ---
    private static class ItemRegistry {
        private Map<ResourceType, Item> resourceItems = new HashMap<>();
        private Map<String, Item> allItemsByName = new HashMap<>();
        private List<Item> fishList = new ArrayList<>();
        private Random random = new Random();

        public ItemRegistry(Context context) {
            int placeholderIcon = R.drawable.spritesheet_icons;
            int cropIconSheet = R.drawable.spritesheet_crops;
            int fishIconSheet = R.drawable.spritesheet_fish; // Assuming fish spritesheet

            // Resources
            addItem(new Item("Stone", ItemType.RESOURCE, placeholderIcon, "A piece of rock.", 20, 0, 2));
            addItem(new Item("Coal", ItemType.RESOURCE, placeholderIcon, "A lump of coal.", 20, 0, 10));
            addItem(new Item("Iron Ore", ItemType.RESOURCE, placeholderIcon, "Raw iron ore.", 20, 0, 20));
            addItem(new Item("Gold Ore", ItemType.RESOURCE, placeholderIcon, "Raw gold ore.", 20, 0, 100));
            addItem(new Item("Emerald", ItemType.RESOURCE, placeholderIcon, "A green gemstone.", 20, 0, 250));
            addItem(new Item("Diamond", ItemType.RESOURCE, placeholderIcon, "A hard, clear gemstone.", 20, 0, 500));

            // Tools
            addItem(new Tool("Basic Pickaxe", placeholderIcon, "A simple pickaxe.", 100, 1, 1.0f, 50, 10));
            addItem(new Tool("Basic Hoe", placeholderIcon, "Tills soil.", 100, 1, 1.0f, 50, 10));
            addItem(new Tool("Basic Axe", placeholderIcon, "Chops wood.", 100, 1, 1.0f, 50, 10));
            addItem(new Tool("Watering Can", placeholderIcon, "Waters crops.", 100, 1, 1.0f, 75, 15));
            addItem(new Tool("Fishing Rod", placeholderIcon, "Catches fish.", 100, 1, 1.0f, 100, 20));

            // Seeds
            addItem(new Item("Turnip Seeds", ItemType.SEED, cropIconSheet, "Plant in spring.", 99, 20, 5));

            // Crops (Harvested Item)
            addItem(new Item("Turnip", ItemType.CROP, cropIconSheet, "A root vegetable.", 10, 0, 60));

            // Fish
            addFish(new Item("Carp", ItemType.FISH, fishIconSheet, "A common river fish.", 10, 0, 30));
            addFish(new Item("Sardine", ItemType.FISH, fishIconSheet, "A small, oily fish.", 10, 0, 40));
            addFish(new Item("Tuna", ItemType.FISH, fishIconSheet, "A large saltwater fish.", 5, 0, 120));
            addFish(new Item("Old Boot", ItemType.JUNK, placeholderIcon, "Someone lost this.", 1, 0, 1)); // Junk item

            // Map resource types to items (for drops)
            resourceItems.put(ResourceType.STONE, getItemByName("Stone"));
            resourceItems.put(ResourceType.COAL, getItemByName("Coal"));
            resourceItems.put(ResourceType.IRON, getItemByName("Iron Ore"));
            resourceItems.put(ResourceType.GOLD, getItemByName("Gold Ore"));
            resourceItems.put(ResourceType.EMERALD, getItemByName("Emerald"));
            resourceItems.put(ResourceType.DIAMOND, getItemByName("Diamond"));
        }

        private void addItem(Item item) {
            if (item != null) {
                allItemsByName.put(item.getName(), item);
            }
        }

        private void addFish(Item fish) {
            if (fish != null) {
                addItem(fish);
                if (fish.getType() == ItemType.FISH || fish.getType() == ItemType.JUNK) {
                    fishList.add(fish);
                }
            }
=======
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
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
        }

        public Item getItemForResource(ResourceType type) {
            return resourceItems.get(type);
        }
<<<<<<< HEAD

        public Item getItemByName(String name) {
            return allItemsByName.get(name);
        }

        // Get a random fish (or junk) from the list
        public Item getRandomFish() {
            if (fishList.isEmpty()) {
                return null;
            }
            // Basic random selection, could be weighted later
            return fishList.get(random.nextInt(fishList.size()));
        }
    }

    // --- Crop Registry Inner Class ---
    private static class CropRegistry {
        private Map<String, Crop> cropsByName = new HashMap<>();
        private Map<Item, Crop> cropsBySeed = new HashMap<>();

        public CropRegistry(Context context, ItemRegistry itemRegistry) {
            // Define crops
            Item turnipSeed = itemRegistry.getItemByName("Turnip Seeds");
            Item turnip = itemRegistry.getItemByName("Turnip");
            if (turnipSeed != null && turnip != null) {
                long dayMillis = 20 * 1000L; // Example: 20 seconds = 1 day
                int[] turnipGrowthTimes = {0, (int)(1 * dayMillis), (int)(2 * dayMillis), (int)(4 * dayMillis)};
                int turnipSpriteRow = 0;
                addCrop(new Crop("Turnip", turnipSeed, turnip, turnipGrowthTimes, turnipSpriteRow));
            }
        }

        private void addCrop(Crop crop) {
            if (crop != null) {
                cropsByName.put(crop.getName(), crop);
                if (crop.getSeedItem() != null) {
                    cropsBySeed.put(crop.getSeedItem(), crop);
                }
            }
        }

        public Crop getCropByName(String name) {
            return cropsByName.get(name);
        }

        public Crop getCropForSeed(Item seedItem) {
            return cropsBySeed.get(seedItem);
        }

        public Map<String, Crop> getAllCrops() {
            return cropsByName;
        }
    }

    // --- Tool Class ---
    public static class Tool extends Item {
        private static final long serialVersionUID = 1L;
        private int maxDurability;
        private int currentDurability;
        private int level;
        private float efficiency;

        public Tool(String name, int resourceId, String description, int durability, int level, float efficiency, int buyPrice, int sellPrice) {
            super(name, ItemType.TOOL, resourceId, description, buyPrice, sellPrice);
            this.maxDurability = durability;
            this.currentDurability = durability;
            this.level = level;
            this.efficiency = efficiency;
        }

        public int getMaxDurability() { return maxDurability; }
        public int getCurrentDurability() { return currentDurability; }
        public int getLevel() { return level; }
        public float getEfficiency() { return efficiency; }

        public void use() {
            if (currentDurability > 0) {
                currentDurability--;
            }
        }

        public boolean isBroken() {
            return currentDurability <= 0;
        }

        public void setCurrentDurability(int durability) {
            this.currentDurability = Math.max(0, Math.min(durability, maxDurability));
        }
    }

    // --- Fishing Minigame Placeholder Class ---
    private static class FishingMinigame {
        private Context context;
        private Item targetFish;
        private boolean finished = false;
        private Item result = null;
        private int screenWidth, screenHeight;
        private Paint textPaint;

        public FishingMinigame(Context context, Item targetFish) {
            this.context = context;
            this.targetFish = targetFish;
            this.textPaint = new Paint();
            textPaint.setColor(Color.WHITE);
            textPaint.setTextSize(60);
            textPaint.setTextAlign(Paint.Align.CENTER);
        }

        public void setScreenSize(int width, int height) {
            this.screenWidth = width;
            this.screenHeight = height;
        }

        public void update() { }

        public void draw(Canvas canvas) {
            canvas.drawColor(Color.argb(180, 0, 0, 100)); // Dark blue overlay
            canvas.drawText("Fishing Minigame!", screenWidth / 2f, screenHeight / 3f, textPaint);
            canvas.drawText("Tap to Reel!", screenWidth / 2f, screenHeight / 2f, textPaint);
        }

        public void handleTap() {
            finish(true);
        }

        private void finish(boolean success) {
            this.finished = true;
            this.result = success ? targetFish : null;
        }

        public boolean isFinished() {
            return finished;
        }

        public Item getResult() {
            return result;
        }
=======
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
    }
}

