package com.example.farmlifegame;

// Basic enum for item types
enum ItemType {
    RESOURCE, // Ores, Fish, Crops
    TOOL,     // Axe, Pickaxe, Hoe, Fishing Rod
    SEED,     // Crop seeds
    IMPROVEMENT // Backpack upgrade, etc.
}

public class Item {
    private String name;
    private ItemType type;
    private int resourceId; // Drawable resource ID for the item's icon
    private String description;
    // Add other relevant properties like stack size, value, tool durability, etc.

    public Item(String name, ItemType type, int resourceId, String description) {
        this.name = name;
        this.type = type;
        this.resourceId = resourceId;
        this.description = description;
    }

    // Getters
    public String getName() {
        return name;
    }

    public ItemType getType() {
        return type;
    }

    public int getResourceId() {
        return resourceId;
    }

    public String getDescription() {
        return description;
    }

    // TODO: Add equals() and hashCode() if items need to be compared or stored in sets/maps
}

