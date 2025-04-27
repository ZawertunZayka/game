package com.example.farmlifegame;

import java.io.Serializable;
import java.util.List;
// Add other necessary imports for game state data structures

/**
 * A Plain Old Java Object (POJO) class to hold all the game state data
 * that needs to be saved and loaded. Make sure all contained objects
 * are also Serializable or handle their serialization manually.
 */
public class GameData implements Serializable {

    private static final long serialVersionUID = 1L; // For Serializable versioning

    // Player Data
    private int playerX;
    private int playerY;
    private int playerDirection;
    private List<ItemStack> playerInventoryItems; // Assuming ItemStack and Item are Serializable
    private int playerCurrency;
    private int playerExperience; // Example: single XP value
    private int playerLevel;
    // Add equipped tool, health, etc.

    // World Data
    private int[][] tilemapData; // The state of the tilemap (e.g., tilled plots)
    private List<ResourceNodeData> resourceNodeStates; // Need a serializable ResourceNodeData class
    private List<FarmPlotData> farmPlotStates; // Need a serializable FarmPlotData class
    // Add data for time, season, weather etc.

    // --- Getters and Setters ---

    public int getPlayerX() {
        return playerX;
    }

    public void setPlayerX(int playerX) {
        this.playerX = playerX;
    }

    public int getPlayerY() {
        return playerY;
    }

    public void setPlayerY(int playerY) {
        this.playerY = playerY;
    }

    public int getPlayerDirection() {
        return playerDirection;
    }

    public void setPlayerDirection(int playerDirection) {
        this.playerDirection = playerDirection;
    }

    public List<ItemStack> getPlayerInventoryItems() {
        return playerInventoryItems;
    }

    public void setPlayerInventoryItems(List<ItemStack> playerInventoryItems) {
        this.playerInventoryItems = playerInventoryItems;
    }

    public int getPlayerCurrency() {
        return playerCurrency;
    }

    public void setPlayerCurrency(int playerCurrency) {
        this.playerCurrency = playerCurrency;
    }

    public int getPlayerExperience() {
        return playerExperience;
    }

    public void setPlayerExperience(int playerExperience) {
        this.playerExperience = playerExperience;
    }

    public int getPlayerLevel() {
        return playerLevel;
    }

    public void setPlayerLevel(int playerLevel) {
        this.playerLevel = playerLevel;
    }

    public int[][] getTilemapData() {
        return tilemapData;
    }

    public void setTilemapData(int[][] tilemapData) {
        this.tilemapData = tilemapData;
    }

    public List<ResourceNodeData> getResourceNodeStates() {
        return resourceNodeStates;
    }

    public void setResourceNodeStates(List<ResourceNodeData> resourceNodeStates) {
        this.resourceNodeStates = resourceNodeStates;
    }

    public List<FarmPlotData> getFarmPlotStates() {
        return farmPlotStates;
    }

    public void setFarmPlotStates(List<FarmPlotData> farmPlotStates) {
        this.farmPlotStates = farmPlotStates;
    }

    // --- Helper classes for complex states (if needed) ---

    // Example: Serializable data for ResourceNode
    public static class ResourceNodeData implements Serializable {
        private static final long serialVersionUID = 1L;
        int x, y;
        ResourceType type; // Assuming ResourceType is Serializable (enum is fine)
        int health;
        boolean depleted;
        // Add constructor, getters, setters
    }

    // Example: Serializable data for FarmPlot
    public static class FarmPlotData implements Serializable {
        private static final long serialVersionUID = 1L;
        int x, y;
        // Add state, crop type, growth progress, watered status etc.
        // Add constructor, getters, setters
    }

    // Ensure ItemStack and Item are also Serializable
    // If using Bitmaps directly, they are not Serializable - need to save paths or reconstruct
}

