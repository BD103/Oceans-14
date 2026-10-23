package edu.miamioh.cse201.oceans14.engine;

import edu.miamioh.cse201.oceans14.rooms.Room;
import edu.miamioh.cse201.oceans14.rooms.EntranceRoom;
import edu.miamioh.cse201.oceans14.actors.Actor;
import edu.miamioh.cse201.oceans14.items.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * Central Registry of all that exists within the world
 * This class handles tracking all items, rooms, and actors.
 * It also provides validation methods such as seeing if a given
 * object exists.
 */
public class GameWorld {
    private static final Map<String, Room> rooms = new HashMap<>();
    private static final Map<String, Actor> actors = new HashMap<>();
    private static final Map<String, Item> items = new HashMap<>();

    private GameWorld() {}

    /*
     * Initializes the registry when GameWorld is first used.
     */
    static {
        registerRooms();
        registerActors();
        registerItems();
    }

    // --------------------------------------------------------------------------------------------
    // REGISTER ALL ROOMS, ACTORS, and ITEMS IN THIS AREA
    // --------------------------------------------------------------------------------------------

    /**
     * Area where all rooms are initialized and added to the rooms map
     */
    private static void registerRooms() {
        registerRoom(new EntranceRoom());
    }

    /**
     * Area where all actors are initialized and added to the actors map
     */
    private static void registerActors() {

    }

    /**
     * Area where all items are initialized and added to the items map.
     */
    private static void registerItems() {
        registerItem(new Item(
                "screwdriver",
                "Screwdriver",
                "SCREWDRIVER DESCRIPTIVE TEXT HERE"));
    }

    // Registration Methods

    /**
     * Register a new room
     * @param room the room to add to the registry
     * @throws IllegalArgumentException if the given room ID already exists
     */
    private static void registerRoom(Room room) {
        if (!rooms.containsKey(room.getID())) {
            rooms.put(room.getID(), room);
        } else {
            throw new IllegalArgumentException("Duplicate Room ID: " + room.getID());
        }
    }

    /**
     * Register a new actor
     * @param actor the actor to add to the registry
     * @throws IllegalArgumentException if the given actor ID already exists
     */
    private static void registerActor(Actor actor) {
        if (!actors.containsKey(actor.getID())) {
            actors.put(actor.getID(), actor);
        } else {
            throw new IllegalArgumentException("Duplicate Actor ID: " + actor.getID());
        }
    }

    /**
     * Register a new item
     * @param item the item to add to the registry
     * @throws IllegalArgumentException if the given item ID already exists
     */
    private static void registerItem(Item item) {
        if (!items.containsKey(item.id())) {
            items.put(item.id(), item);
        } else {
            throw new IllegalArgumentException("Duplicate Item ID: " + item.id());
        }
    }

    // Public Facing Getter Methods

    /**
     * Get the room object associated with a specific ID
     * @param roomID the room ID to find the room of
     * @return Room object of the given room
     * @throws IllegalArgumentException if the room ID does not exist
     */
    public static Room getRoom(String roomID) {
        if (roomExists(roomID)) {
            return rooms.get(roomID);
        } else {
            throw new IllegalArgumentException("No such room exists with ID: " + roomID);
        }

    }

    /**
     * Get the actor object associated with a specific ID
     * @param actorID the actor ID to find the actor of
     * @return Actor object of the given actor
     * @throws IllegalArgumentException if the actor ID does not exist
     */
    public static Actor getActor(String actorID) {
        if (actorExists(actorID)) {
            return actors.get(actorID);
        } else {
            throw new IllegalArgumentException("No such actor exists with ID: " + actorID);
        }
    }

    /**
     * Get the item object associated with a specific ID
     * @param itemID the item ID to find the item of
     * @return Item record of the given item
     * @throws IllegalArgumentException if the item ID does not exist
     */
    public static Item getItem(String itemID) {
        if (itemExists(itemID)) {
            return items.get(itemID);
        } else {
            throw new IllegalArgumentException("No such item exists with ID: " + itemID);
        }
    }

    /**
     * Checks to see if a given room Exists
     * @param roomID the ID of the room to check
     * @return true if it exists, false otherwise
     */
    public static boolean roomExists(String roomID) {
        return rooms.containsKey(roomID);
    }

    /**
     * Checks to see if a given actor Exists
     * @param actorID the ID of the actor to check
     * @return true if it exists, false otherwise
     */
    public static boolean actorExists(String actorID) {
        return actors.containsKey(actorID);
    }

    /**
     * Checks to see if a given item Exists
     * @param itemID the ID of the item to check
     * @return true if it exists, false otherwise
     */
    public static boolean itemExists(String itemID) {
        return items.containsKey(itemID);
    }



}
