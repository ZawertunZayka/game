package com.example.farmlifegame;

import java.util.ArrayList;
import java.util.List;

public class Inventory {
    private int capacity; // Max number of unique item stacks
    private List<ItemStack> items;

    public Inventory(int capacity) {
        this.capacity = capacity;
        this.items = new ArrayList<>(capacity);
    }

    public boolean addItem(Item item, int quantity) {
        if (quantity <= 0) {
            return false;
        }

        // Check if item already exists and can be stacked
        for (ItemStack stack : items) {
            // TODO: Add check for stackable items and max stack size
            if (stack.getItem().getName().equals(item.getName())) { // Simple name check for now
                stack.addQuantity(quantity);
                return true;
            }
        }

        // If item not found or cannot stack more, add a new stack if space available
        if (items.size() < capacity) {
            items.add(new ItemStack(item, quantity));
            return true;
        } else {
            // Inventory full
            return false;
        }
    }

    public boolean removeItem(Item item, int quantity) {
        if (quantity <= 0) {
            return false;
        }

        for (int i = items.size() - 1; i >= 0; i--) {
            ItemStack stack = items.get(i);
            if (stack.getItem().getName().equals(item.getName())) { // Simple name check
                if (stack.getQuantity() > quantity) {
                    stack.addQuantity(-quantity);
                    return true;
                } else if (stack.getQuantity() == quantity) {
                    items.remove(i);
                    return true;
                } else {
                    // Not enough items in this stack, maybe check other stacks? (if splitting allowed)
                    // For now, assume we only remove from one stack fully or partially
                    return false; // Or handle splitting logic if needed
                }
            }
        }
        return false; // Item not found
    }

    public List<ItemStack> getItems() {
        return items;
    }

    public int getCapacity() {
        return capacity;
    }

    // TODO: Add methods to check quantity of an item, find specific items, etc.
}

