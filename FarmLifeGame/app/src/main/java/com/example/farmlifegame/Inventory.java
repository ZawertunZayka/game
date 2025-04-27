package com.example.farmlifegame;

import android.util.Log;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the player's inventory, holding ItemStacks.
 * Implements Serializable to be saved in GameData.
 */
public class Inventory implements Serializable {
    private static final long serialVersionUID = 1L; // Needed for Serializable
    private static final String TAG = "Inventory";

    private int capacity; // Max number of unique item stacks
    private List<ItemStack> items;

    public Inventory(int capacity) {
        this.capacity = capacity;
        // Initialize with nulls or empty slots to represent fixed capacity
        this.items = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            this.items.add(null); // Use null to represent empty slots
        }
    }

    /**
     * Attempts to add a given quantity of an item to the inventory.
     * Tries to stack with existing items first, then uses empty slots.
     * @param item The Item to add.
     * @param quantity The quantity to add.
     * @return The quantity of the item that could NOT be added (0 if all added successfully).
     */
    public int addItem(Item item, int quantity) {
        if (item == null || quantity <= 0) {
            Log.w(TAG, "Attempted to add invalid item or quantity.");
            return quantity; // Cannot add null or zero/negative quantity
        }

        int remainingQuantity = quantity;

        // 1. Try to stack with existing stacks of the same item
        if (item.isStackable()) {
            for (int i = 0; i < capacity && remainingQuantity > 0; i++) {
                ItemStack stack = items.get(i);
                if (stack != null && stack.getItem().equals(item) && !stack.isFull()) {
                    int canAdd = stack.getItem().getMaxStackSize() - stack.getQuantity();
                    int amountToAdd = Math.min(remainingQuantity, canAdd);
                    stack.addQuantity(amountToAdd);
                    remainingQuantity -= amountToAdd;
                    Log.d(TAG, "Added " + amountToAdd + " to existing stack of " + item.getName() + ". Remaining: " + remainingQuantity);
                }
            }
        }

        // 2. If quantity still remains, try to add to empty slots
        if (remainingQuantity > 0) {
            for (int i = 0; i < capacity && remainingQuantity > 0; i++) {
                if (items.get(i) == null) { // Found an empty slot
                    int amountToAdd = Math.min(remainingQuantity, item.getMaxStackSize());
                    items.set(i, new ItemStack(item, amountToAdd));
                    remainingQuantity -= amountToAdd;
                    Log.d(TAG, "Added new stack of " + item.getName() + " with quantity " + amountToAdd + ". Remaining: " + remainingQuantity);
                    // If item is not stackable, we only add one stack of quantity 1
                    if (!item.isStackable()) {
                        if (remainingQuantity > 0) {
                             Log.w(TAG, "Tried to add multiple non-stackable items, only added one.");
                             // The loop will continue but won't find more empty slots for this specific call
                             // because remainingQuantity should be 0 after adding one non-stackable item.
                             // Let's ensure remainingQuantity reflects this for clarity.
                             // remainingQuantity = quantity - 1; // Or handle this logic outside the loop
                        }
                        break; // Only add one non-stackable item per call? Or per empty slot?
                               // Let's assume one per empty slot found.
                    }
                }
            }
        }

        if (remainingQuantity > 0) {
            Log.w(TAG, "Inventory full. Could not add " + remainingQuantity + " of " + item.getName());
        }

        return remainingQuantity; // Return the amount that couldn't be added
    }

    /**
     * Attempts to remove a given quantity of an item from the inventory.
     * Removes from stacks starting from the end.
     * @param item The Item to remove.
     * @param quantity The quantity to remove.
     * @return true if the specified quantity was successfully removed, false otherwise.
     */
    public boolean removeItem(Item item, int quantity) {
        if (item == null || quantity <= 0) {
            Log.w(TAG, "Attempted to remove invalid item or quantity.");
            return false;
        }

        int quantityToRemove = quantity;

        // Check if we have enough items first (optional but good practice)
        if (getItemCount(item) < quantityToRemove) {
            Log.w(TAG, "Not enough " + item.getName() + " to remove. Have: " + getItemCount(item) + ", Need: " + quantityToRemove);
            return false;
        }

        // Iterate backwards to easily remove empty stacks
        for (int i = capacity - 1; i >= 0 && quantityToRemove > 0; i--) {
            ItemStack stack = items.get(i);
            if (stack != null && stack.getItem().equals(item)) {
                int amountToRemoveFromStack = Math.min(quantityToRemove, stack.getQuantity());
                stack.addQuantity(-amountToRemoveFromStack); // addQuantity handles setting to 0
                quantityToRemove -= amountToRemoveFromStack;
                Log.d(TAG, "Removed " + amountToRemoveFromStack + " from stack of " + item.getName() + ". To remove: " + quantityToRemove);

                if (stack.getQuantity() <= 0) {
                    items.set(i, null); // Remove empty stack by setting slot to null
                    Log.d(TAG, "Removed empty stack of " + item.getName() + " from slot " + i);
                }
            }
        }

        // If quantityToRemove is > 0 here, it means we didn't have enough (should have been caught by initial check)
        if (quantityToRemove > 0) {
             Log.e(TAG, "Error in removeItem logic: Could not remove required quantity of " + item.getName() + ". Remaining to remove: " + quantityToRemove);
             // This case indicates a logic error if the initial check passed.
             return false; // Or handle partial removal if allowed?
        }

        return true; // Successfully removed the requested quantity
    }

    /**
     * Gets the total count of a specific item across all stacks.
     * @param item The item to count.
     * @return The total quantity of the item in the inventory.
     */
    public int getItemCount(Item item) {
        if (item == null) return 0;
        int count = 0;
        for (ItemStack stack : items) {
            if (stack != null && stack.getItem().equals(item)) {
                count += stack.getQuantity();
            }
        }
        return count;
    }

    /**
     * Checks if the inventory contains at least a certain quantity of an item.
     * @param item The item to check for.
     * @param quantity The minimum quantity required.
     * @return true if the inventory contains at least the specified quantity, false otherwise.
     */
    public boolean hasItem(Item item, int quantity) {
        return getItemCount(item) >= quantity;
    }

    /**
     * Returns the ItemStack at a specific slot index.
     * @param index The slot index.
     * @return The ItemStack at the index, or null if the slot is empty or index is invalid.
     */
    public ItemStack getItemStack(int index) {
        if (index >= 0 && index < capacity) {
            return items.get(index);
        }
        return null;
    }

    /**
     * Returns a direct list of ItemStacks. Be careful modifying this list directly.
     * Consider returning a copy if external modification is not desired.
     * Null entries represent empty slots.
     * @return The list of ItemStacks.
     */
    public List<ItemStack> getItems() {
        return items;
    }

    /**
     * Gets the total number of slots in the inventory.
     * @return The capacity.
     */
    public int getCapacity() {
        return capacity;
    }

    /**
     * Clears the inventory, setting all slots to null.
     */
    public void clear() {
        for (int i = 0; i < capacity; i++) {
            items.set(i, null);
        }
        Log.d(TAG, "Inventory cleared.");
    }

     /**
     * Sets the items directly. Used primarily for loading game state.
     * Ensures the loaded list matches the capacity.
     * @param loadedItems List of ItemStacks (can contain nulls).
     */
    public void setItems(List<ItemStack> loadedItems) {
        if (loadedItems != null && loadedItems.size() == capacity) {
            this.items = loadedItems;
        } else if (loadedItems != null) {
            Log.w(TAG, "Loaded item list size mismatch. Expected: " + capacity + ", Got: " + loadedItems.size() + ". Adjusting...");
            // Adjust: copy what fits, fill rest with null
            this.items = new ArrayList<>(capacity);
            for (int i = 0; i < capacity; i++) {
                if (i < loadedItems.size()) {
                    this.items.add(loadedItems.get(i));
                } else {
                    this.items.add(null);
                }
            }
        } else {
             Log.e(TAG, "Attempted to set null or invalid item list.");
             // Optionally clear the inventory or keep the existing one
             clear();
        }
    }
}

