package com.example.farmlifegame;

<<<<<<< HEAD
import java.io.Serializable;

// Basic enum for item types
enum ItemType {
    RESOURCE, // Ores, Fish, Crops
    TOOL,     // Axe, Pickaxe, Hoe, Fishing Rod, Watering Can
    SEED,     // Crop seeds
    CROP,     // Harvested crops
    FISH,     // Caught fish
    IMPROVEMENT // Backpack upgrade, etc.
}

public class Item implements Serializable {
    private static final long serialVersionUID = 1L; // Needed for Serializable

    private String name;
    private ItemType type;
    private int resourceId; // Drawable resource ID for the item's icon (transient? or save name/path)
    private String description;
    private boolean isStackable;
    private int maxStackSize;
    private int buyPrice;
    private int sellPrice;

    // Constructor for non-stackable items (like tools)
    public Item(String name, ItemType type, int resourceId, String description, int buyPrice, int sellPrice) {
        this(name, type, resourceId, description, false, 1, buyPrice, sellPrice);
    }

    // Constructor for stackable items
    public Item(String name, ItemType type, int resourceId, String description, int maxStackSize, int buyPrice, int sellPrice) {
        this(name, type, resourceId, description, true, maxStackSize, buyPrice, sellPrice);
    }

    // Private constructor
    private Item(String name, ItemType type, int resourceId, String description, boolean isStackable, int maxStackSize, int buyPrice, int sellPrice) {
        this.name = name;
        this.type = type;
        this.resourceId = resourceId; // Be careful with resource IDs during serialization/deserialization
        this.description = description;
        this.isStackable = isStackable;
        this.maxStackSize = isStackable ? maxStackSize : 1;
        this.buyPrice = buyPrice;
        this.sellPrice = sellPrice;
=======
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
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
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

<<<<<<< HEAD
    public boolean isStackable() {
        return isStackable;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    // Override equals and hashCode if items are stored in Sets or used as Map keys
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return name.equals(item.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
=======
    // TODO: Add equals() and hashCode() if items need to be compared or stored in sets/maps
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
}

