package com.example.farmlifegame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Activity for the Shop interface.
 * Allows buying and selling items.
 */
public class ShopActivity extends AppCompatActivity {

    private static final String TAG = "ShopActivity";
    public static final String EXTRA_PLAYER_DATA = "com.example.farmlifegame.PLAYER_DATA";
    public static final String EXTRA_SHOP_ITEMS = "com.example.farmlifegame.SHOP_ITEMS"; // Optional: Pass specific items

    private RecyclerView buyRecyclerView;
    private RecyclerView sellRecyclerView;
    private TextView playerCurrencyTextView;
    private Button closeButton;

    private PlayerData playerData; // Use a simplified PlayerData for transfer
    private ShopItemAdapter buyAdapter;
    private ShopItemAdapter sellAdapter;
    private GameView.ItemRegistry itemRegistry; // Need access to item definitions

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop); // Need to create this layout file

        Log.d(TAG, "onCreate: Initializing ShopActivity.");

        // Get player data from Intent
        if (getIntent().hasExtra(EXTRA_PLAYER_DATA)) {
            playerData = (PlayerData) getIntent().getSerializableExtra(EXTRA_PLAYER_DATA);
        } else {
            Log.e(TAG, "Player data not found in Intent. Cannot open shop.");
            Toast.makeText(this, "Error: Player data missing.", Toast.LENGTH_SHORT).show();
            finish(); // Close activity if data is missing
            return;
        }

        itemRegistry = new GameView.ItemRegistry(this); // Initialize registry to get item details

        buyRecyclerView = findViewById(R.id.buyRecyclerView);
        sellRecyclerView = findViewById(R.id.sellRecyclerView);
        playerCurrencyTextView = findViewById(R.id.playerCurrencyTextView);
        closeButton = findViewById(R.id.closeShopButton);

        playerCurrencyTextView.setText("Currency: " + playerData.getPlayerCurrency());

        setupRecyclerViews();
        populateAdapters();

        closeButton.setOnClickListener(v -> finish());

        Log.d(TAG, "ShopActivity initialized successfully.");
    }

    private void setupRecyclerViews() {
        buyRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        sellRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        buyAdapter = new ShopItemAdapter(this, true); // true for buying
        sellAdapter = new ShopItemAdapter(this, false); // false for selling

        buyRecyclerView.setAdapter(buyAdapter);
        sellRecyclerView.setAdapter(sellAdapter);
    }

    private void populateAdapters() {
        // --- Populate Buy List ---
        // Get items available for purchase (e.g., seeds, basic tools)
        List<ItemStack> buyableItems = new ArrayList<>();
        // Example: Add seeds and basic tools if they have a buy price > 0
        Item turnipSeeds = itemRegistry.getItemByName("Turnip Seeds");
        Item basicPickaxe = itemRegistry.getItemByName("Basic Pickaxe");
        Item basicHoe = itemRegistry.getItemByName("Basic Hoe");
        Item wateringCan = itemRegistry.getItemByName("Watering Can");
        Item fishingRod = itemRegistry.getItemByName("Fishing Rod");

        if (turnipSeeds != null && turnipSeeds.getBuyPrice() > 0) buyableItems.add(new ItemStack(turnipSeeds, 99)); // Infinite stock for now
        if (basicPickaxe != null && basicPickaxe.getBuyPrice() > 0) buyableItems.add(new ItemStack(basicPickaxe, 1));
        if (basicHoe != null && basicHoe.getBuyPrice() > 0) buyableItems.add(new ItemStack(basicHoe, 1));
        if (wateringCan != null && wateringCan.getBuyPrice() > 0) buyableItems.add(new ItemStack(wateringCan, 1));
        if (fishingRod != null && fishingRod.getBuyPrice() > 0) buyableItems.add(new ItemStack(fishingRod, 1));
        // Add more buyable items...

        buyAdapter.setItems(buyableItems);
        Log.d(TAG, "Populated buy adapter with " + buyableItems.size() + " items.");

        // --- Populate Sell List ---
        // Get items from player inventory that are sellable (sell price > 0)
        if (playerData.getPlayerInventoryItems() != null) {
            List<ItemStack> sellableItems = playerData.getPlayerInventoryItems().stream()
                    .filter(stack -> stack != null && stack.getItem() != null && stack.getItem().getSellPrice() > 0)
                    .collect(Collectors.toList());
            sellAdapter.setItems(sellableItems);
            Log.d(TAG, "Populated sell adapter with " + sellableItems.size() + " items from player inventory.");
        } else {
            Log.w(TAG, "Player inventory data is null.");
            sellAdapter.setItems(new ArrayList<>());
        }
    }

    private void handleBuy(ItemStack itemStack) {
        if (itemStack == null || itemStack.getItem() == null) return;
        Item itemToBuy = itemStack.getItem();
        int cost = itemToBuy.getBuyPrice();

        Log.d(TAG, "Attempting to buy: " + itemToBuy.getName() + " for " + cost);

        if (playerData.getPlayerCurrency() >= cost) {
            // Simulate adding item to inventory (actual update happens on return)
            boolean added = InventoryHelper.canAddItem(playerData.getPlayerInventoryItems(), itemToBuy, 1);

            if (added) {
                playerData.setPlayerCurrency(playerData.getPlayerCurrency() - cost);
                InventoryHelper.addItem(playerData.getPlayerInventoryItems(), itemToBuy, 1);

                playerCurrencyTextView.setText("Currency: " + playerData.getPlayerCurrency());
                // Refresh sell adapter as inventory changed
                refreshSellAdapter();
                Toast.makeText(this, "Bought " + itemToBuy.getName(), Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Buy successful. New currency: " + playerData.getPlayerCurrency());
            } else {
                Toast.makeText(this, "Inventory full!", Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Buy failed: Inventory full.");
            }
        } else {
            Toast.makeText(this, "Not enough currency!", Toast.LENGTH_SHORT).show();
            Log.w(TAG, "Buy failed: Not enough currency.");
        }
    }

    private void handleSell(ItemStack itemStack) {
        if (itemStack == null || itemStack.getItem() == null) return;
        Item itemToSell = itemStack.getItem();
        int value = itemToSell.getSellPrice();

        Log.d(TAG, "Attempting to sell: " + itemToSell.getName() + " for " + value);

        // Simulate removing item from inventory
        boolean removed = InventoryHelper.removeItem(playerData.getPlayerInventoryItems(), itemToSell, 1);

        if (removed) {
            playerData.setPlayerCurrency(playerData.getPlayerCurrency() + value);
            playerCurrencyTextView.setText("Currency: " + playerData.getPlayerCurrency());
            // Refresh sell adapter as inventory changed
            refreshSellAdapter();
            Toast.makeText(this, "Sold " + itemToSell.getName(), Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Sell successful. New currency: " + playerData.getPlayerCurrency());
        } else {
            // This shouldn't happen if the item came from the sell list
            Toast.makeText(this, "Error selling item!", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "Sell failed: Item not found in player data inventory?");
        }
    }

    private void refreshSellAdapter() {
        if (playerData.getPlayerInventoryItems() != null) {
            List<ItemStack> sellableItems = playerData.getPlayerInventoryItems().stream()
                    .filter(stack -> stack != null && stack.getItem() != null && stack.getItem().getSellPrice() > 0)
                    .collect(Collectors.toList());
            sellAdapter.setItems(sellableItems);
        } else {
            sellAdapter.setItems(new ArrayList<>());
        }
    }

    @Override
    public void finish() {
        // Return updated player data
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_PLAYER_DATA, playerData);
        setResult(Activity.RESULT_OK, resultIntent);
        Log.d(TAG, "Finishing ShopActivity, returning updated player data.");
        super.finish();
    }

    // --- Adapter Class ---
    private class ShopItemAdapter extends RecyclerView.Adapter<ShopItemAdapter.ViewHolder> {

        private List<ItemStack> items;
        private Context context;
        private boolean isBuyMode;

        public ShopItemAdapter(Context context, boolean isBuyMode) {
            this.context = context;
            this.items = new ArrayList<>();
            this.isBuyMode = isBuyMode;
        }

        public void setItems(List<ItemStack> items) {
            this.items = items != null ? new ArrayList<>(items) : new ArrayList<>();
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.shop_item_row, parent, false); // Need this layout
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ItemStack currentStack = items.get(position);
            if (currentStack == null || currentStack.getItem() == null) {
                // Handle null item case if necessary
                holder.itemName.setText("Error");
                holder.itemPrice.setText("-");
                holder.itemIcon.setImageResource(R.drawable.ic_launcher_background); // Placeholder
                holder.actionButton.setVisibility(View.GONE);
                return;
            }
            Item currentItem = currentStack.getItem();

            holder.itemName.setText(currentItem.getName());
            // TODO: Set actual item icon based on currentItem.getResourceId() or similar
            // holder.itemIcon.setImageResource(currentItem.getResourceId());
            holder.itemIcon.setImageResource(R.drawable.spritesheet_icons); // Placeholder icon

            if (isBuyMode) {
                holder.itemPrice.setText("Buy: " + currentItem.getBuyPrice());
                holder.actionButton.setText("Buy");
                holder.actionButton.setEnabled(currentItem.getBuyPrice() > 0);
                holder.actionButton.setOnClickListener(v -> handleBuy(currentStack));
            } else {
                holder.itemPrice.setText("Sell: " + currentItem.getSellPrice() + " (Qty: " + currentStack.getQuantity() + ")");
                holder.actionButton.setText("Sell");
                holder.actionButton.setEnabled(currentItem.getSellPrice() > 0 && currentStack.getQuantity() > 0);
                holder.actionButton.setOnClickListener(v -> handleSell(currentStack));
            }
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView itemIcon;
            TextView itemName;
            TextView itemPrice;
            Button actionButton;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                itemIcon = itemView.findViewById(R.id.itemIcon);
                itemName = itemView.findViewById(R.id.itemName);
                itemPrice = itemView.findViewById(R.id.itemPrice);
                actionButton = itemView.findViewById(R.id.actionButton);
            }
        }
    }

    // --- Helper Class for simplified inventory manipulation ---
    // This avoids needing the full Inventory class logic here
    private static class InventoryHelper {
        public static boolean canAddItem(List<ItemStack> items, Item item, int quantity) {
            if (items == null || item == null || quantity <= 0) return false;
            // Check existing stacks
            for (ItemStack stack : items) {
                if (stack != null && stack.getItem() != null && stack.getItem().getName().equals(item.getName())) {
                    if (stack.getQuantity() + quantity <= item.getMaxStackSize()) {
                        return true;
                    }
                }
            }
            // Check empty slots
            for (ItemStack stack : items) {
                if (stack == null || stack.getItem() == null) {
                    return true;
                }
            }
            // Check if list size is less than capacity (assuming fixed capacity)
            // For simplicity, let's assume we can always add if there's logical space
            return items.size() < 20; // Assuming capacity 20 for PlayerData
        }

        public static void addItem(List<ItemStack> items, Item item, int quantity) {
            if (items == null || item == null || quantity <= 0) return;
            // Add to existing stack
            for (ItemStack stack : items) {
                if (stack != null && stack.getItem() != null && stack.getItem().getName().equals(item.getName())) {
                    int canAdd = item.getMaxStackSize() - stack.getQuantity();
                    if (canAdd >= quantity) {
                        stack.setQuantity(stack.getQuantity() + quantity);
                        return;
                    }
                }
            }
            // Add to new stack
            for (int i = 0; i < items.size(); i++) {
                if (items.get(i) == null || items.get(i).getItem() == null) {
                    items.set(i, new ItemStack(item, quantity));
                    return;
                }
            }
            // Add to end if capacity allows (simplified)
            if (items.size() < 20) {
                 items.add(new ItemStack(item, quantity));
            }
        }

        public static boolean removeItem(List<ItemStack> items, Item item, int quantity) {
            if (items == null || item == null || quantity <= 0) return false;
            for (int i = 0; i < items.size(); i++) {
                ItemStack stack = items.get(i);
                if (stack != null && stack.getItem() != null && stack.getItem().getName().equals(item.getName())) {
                    if (stack.getQuantity() >= quantity) {
                        stack.setQuantity(stack.getQuantity() - quantity);
                        if (stack.getQuantity() == 0) {
                            items.set(i, null); // Remove stack if empty
                        }
                        return true;
                    }
                }
            }
            return false;
        }
    }

    // --- Simplified PlayerData for Intent Transfer ---
    // Needs to match the structure used in GameData/Player for relevant fields
    public static class PlayerData implements Serializable {
        private static final long serialVersionUID = 1L;
        private int playerCurrency;
        private List<ItemStack> playerInventoryItems; // Must be Serializable

        // Constructor, Getters, Setters
        public PlayerData(int currency, List<ItemStack> inventory) {
            this.playerCurrency = currency;
            // Ensure the list passed is modifiable and serializable
            this.playerInventoryItems = (inventory != null) ? new ArrayList<>(inventory) : new ArrayList<>();
        }

        public int getPlayerCurrency() { return playerCurrency; }
        public void setPlayerCurrency(int playerCurrency) { this.playerCurrency = playerCurrency; }
        public List<ItemStack> getPlayerInventoryItems() { return playerInventoryItems; }
        public void setPlayerInventoryItems(List<ItemStack> playerInventoryItems) { this.playerInventoryItems = playerInventoryItems; }
    }
}

