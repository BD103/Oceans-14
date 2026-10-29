package edu.miamioh.cse201.oceans14.engine;

import java.util.HashSet;
import java.util.Set;


public class GameState {

    private GameState() {}

    // TODO alter these to be in line with the game design.
    // Starting values for the game.
    private static final int STARTING_CHIPS = 500;
    private static final String STARTING_ROOM = "entrance_room";

    // Current game state
    private static int chipCount = STARTING_CHIPS;
    private static String currentRoomID = STARTING_ROOM;
    private static final Set<String> inventory = new HashSet<>();

    // All Methods Dealing with Chips ---------------------------------------------------

    /**
     * Get the current number of chips
     * @return and int representing the chip count
     */
    public static int getChipCount() { return chipCount; }

    /**
     * Add chips to the players chip count
     * @param amount int representing the amount of chips to add
     */
    public static void addChips(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Can't add negative chips");
        }
        chipCount += amount;
    }

    /**
     * Remove chips from the players count. Used if a player spends or loses chips
     * @param amount the amount of chips to lose
     * @return True if the chips were successfully removed, false if the player doesn't have enough chips to remove
     * @throws IllegalArgumentException if a negative number is entered.
     */
    public static boolean removeChips(int amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("Can't remove negative chips");
        } else if (amount > chipCount) {
            return false;
        } else {
            chipCount -= amount;
            return true;
        }

    }

    // All Methods Dealing with the Inventory -------------------------------------------

    /**
     * Checks to see if the player has a given item.
     * @param itemID the ID of the item to search
     * @return True if the player has the item, false if not.
     */
    public static boolean hasItem(String itemID) {
        return inventory.contains(itemID);
    }

    /**
     * Add an item to the players inventory.
     * @param itemID the ID of the item to add
     * @throws IllegalArgumentException if the item doesn't exist in the registry
     */
    public static void addItem(String itemID) {
        if (!GameWorld.itemExists(itemID)) {
            throw new IllegalArgumentException("Item: " + itemID + " does not exist in the registry");
        }
        inventory.add(itemID);
    }

    /**
     * remove an item from the players inventory
     * @param itemID the item to remove
     * @return true if the item was successfully removed, false if the item was not.
     * @throws IllegalArgumentException if the item doesn't exist in the registry
     */
    public static boolean removeItem(String itemID) {
        if (!GameWorld.itemExists(itemID)) {
            throw new IllegalArgumentException("Item: " + itemID + " does not exist in the registry");
        }
        return inventory.remove(itemID);
    }

    /**
     * Return the list of the players current items.
     * @return an unmodifiable copy of the inventory.
     */
    public static Set<String> getInventory() {
        return Set.copyOf(inventory);
    }

    // All Methods Dealing with the Current Room ----------------------------------------

    /**
     * returns the ID of the room the player is currently in
     * @return the ID of the room the player is currently in.
     */
    public static String getCurrentRoomID(){
        return currentRoomID;
    }

    /**
     * Changes the ID of the room the player is currently in
     * Intentionally package-private in scope. This is to prevent anything outside engine from
     * altering the current room. This method should only be called by GameEngine and no one else.
     * @param newRoomID the new room's ID
     */
    static void setCurrentRoomID(String newRoomID) {
        // TODO connect with a method to ensure the new room ID is valid. Should be implemented in GameWorld.
        currentRoomID = newRoomID;
    }

    /**
     * Resets the current game state, useful in the case of a game over.
     */
    public static void reset(){
        chipCount = STARTING_CHIPS;
        currentRoomID = STARTING_ROOM;
        inventory.clear();
    }




}
