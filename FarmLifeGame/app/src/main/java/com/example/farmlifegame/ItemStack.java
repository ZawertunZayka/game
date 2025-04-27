package com.example.farmlifegame;

<<<<<<< HEAD
import java.io.Serializable;

/**
 * Represents a stack of items in the inventory.
 * Ensure the contained Item class is also Serializable.
 */
public class ItemStack implements Serializable {
    private static final long serialVersionUID = 1L; // Needed for Serializable

=======
// Simple class to hold an item and its quantity
public class ItemStack {
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
    private Item item;
    private int quantity;

    public ItemStack(Item item, int quantity) {
<<<<<<< HEAD
        if (item == null) {
            throw new IllegalArgumentException("Item cannot be null");
        }
        this.item = item;
        // Ensure quantity doesn't exceed max stack size for stackable items
        this.quantity = Math.max(1, Math.min(quantity, item.getMaxStackSize()));
        // For non-stackable items, quantity should always be 1
        if (!item.isStackable()) {
            this.quantity = 1;
        }
=======
        this.item = item;
        this.quantity = quantity;
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
    }

    public Item getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

<<<<<<< HEAD
    /**
     * Sets the quantity of items in the stack.
     * Ensures the quantity is valid (>= 1) and respects the item's max stack size.
     * For non-stackable items, quantity is forced to 1.
     * @param quantity The new quantity.
     */
    public void setQuantity(int quantity) {
        if (item.isStackable()) {
            this.quantity = Math.max(1, Math.min(quantity, item.getMaxStackSize()));
        } else {
            this.quantity = 1; // Non-stackable items always have quantity 1
        }
    }

    /**
     * Adds a specified amount to the quantity.
     * Ensures the resulting quantity does not exceed the max stack size.
     * @param amount The amount to add (can be negative to remove).
     * @return The actual amount added (might be less than requested if max stack size is reached).
     */
    public int addQuantity(int amount) {
        if (!item.isStackable()) {
            return 0; // Cannot change quantity of non-stackable items
        }
        int newQuantity = this.quantity + amount;
        int maxStack = item.getMaxStackSize();

        if (newQuantity > maxStack) {
            int added = maxStack - this.quantity;
            this.quantity = maxStack;
            return added;
        } else if (newQuantity <= 0) {
            int removed = -this.quantity; // Amount actually removed
            this.quantity = 0; // Indicate stack should be removed
            return removed; // Return negative value indicating removal
        } else {
            this.quantity = newQuantity;
            return amount;
        }
    }

    /**
     * Checks if the stack can accommodate adding a certain amount.
     * @param amount The amount to check.
     * @return true if the amount can be added without exceeding max stack size, false otherwise.
     */
    public boolean canAddQuantity(int amount) {
        if (!item.isStackable()) {
            return false; // Cannot add to non-stackable items
        }
        return this.quantity + amount <= item.getMaxStackSize();
    }

    /**
     * Checks if the stack is full (quantity equals max stack size).
     * @return true if the stack is full, false otherwise.
     */
    public boolean isFull() {
        return item.isStackable() && this.quantity >= item.getMaxStackSize();
    }
=======
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void addQuantity(int amount) {
        this.quantity += amount;
    }

    // TODO: Add checks for max stack size if needed
>>>>>>> b4451981c2659383d1917b09f23e243d44d5887f
}

