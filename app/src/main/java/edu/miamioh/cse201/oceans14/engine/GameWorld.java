package edu.miamioh.cse201.oceans14.engine;

import edu.miamioh.cse201.oceans14.rooms.*;
import edu.miamioh.cse201.oceans14.actors.*;
import edu.miamioh.cse201.oceans14.items.Item;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    private static final Set<String> flags = new HashSet<>();

    private GameWorld() {}

    /*
     * Initializes the registry when GameWorld is first used.
     */
    static {
        registerRooms();
        registerActors();
        registerItems();
        registerFlags();
    }

    // --------------------------------------------------------------------------------------------
    // REGISTER ALL ROOMS, ACTORS, and ITEMS IN THIS AREA
    // --------------------------------------------------------------------------------------------

    /**
     * Area where all rooms are initialized and added to the rooms map
     */
    private static void registerRooms() {
        registerRoom(new EntranceRoom());
        registerRoom(new CasinoRoom());
        registerRoom(new StaffRoom());
        registerRoom(new KitchenRoom());
        registerRoom(new HighRollersRoom());
        registerRoom(new MaintenanceRoom());
        registerRoom(new HotelLobbyRoom());
        registerRoom(new CheckpointRoom());
        registerRoom(new BigBossRoom());
        registerRoom(new VaultRoom());
        registerRoom(new BarRoom());
        registerRoom(new CashOutRoom());
        registerRoom(new VentRoom());
        registerRoom(new PanopticonRoom());
    }

    /**
     * Area where all actors are initialized and added to the actors map
     */
    private static void registerActors() {
        registerActor(new CasinoGreeter());
        registerActor(new Anthony());
        registerActor(new Chef());
        registerActor(new Guard());
        registerActor(new Teller());
        registerActor(new Dealer());
        registerActor(new Slots());
        registerActor(new Blackjack());
        registerActor(new CheckpointGuard());
        registerActor(new HotelStaff());
        registerActor(new Elevator());
        registerActor(new Contractors());
        registerActor(new CrazyGambler());
        registerActor(new BigBoss());
        registerActor(new CameraSystem());

    }

    /**
     * Area where all items are initialized and added to the items map.
     */
    private static void registerItems() {
        // TODO change descriptions
        registerItem(new Item(
                "screwdriver",
                "Screwdriver",
                "SCREWDRIVER DESCRIPTIVE TEXT HERE"));
        registerItem(new Item(
                "staff_room_keycard",
                "Staff Keycard",
                "STAFF KEYCARD DESCRIPTIVE TEXT HERE"));
        registerItem(new Item(
                "level_one_disguise",
                "Staff Disguise",
                "LEVEL ONE DISGUISE DESCRIPTIVE TEXT HERE"));
        registerItem(new Item(
                "level_two_disguise",
                "Guard Disguise",
                "LEVEL TWO DISGUISE DESCRIPTIVE TEXT HERE"));
        registerItem(new Item(
                "boss_food",
                "Boss's food",
                "BOSS'S FOOD DESCRIPTIVE TEXT HERE"));
        registerItem(new Item(
                "chef_permission",
                "Chef Permission Note",
                "CHEF PERMISSION NOTE DESCRIPTIVE TEXT HERE"));
        registerItem(new Item(
                "maintenance_key",
                "Maintenance Room Key",
                "MAINTENANCE ROOM KEY DESCRIPTIVE TEXT HERE"));
        registerItem(new Item(
                "screwdriver_checkout_note",
                "Checkout Note",
                "Yo, borrowing this to fix a shelf - Anthony"));
        registerItem(new Item(
                "drink",
                "Drink",
                "A one way ticket to Booztown"));
        registerItem(new Item(
                "high_roller_card",
                "High Roller Card",
                "You sure you're not compensating for something?"));
        registerItem(new Item("vent_map",
                "Vent Map",
                "A detailed map of the casino's ventilation system, including the locations of its security lasers."));
        registerItem(new Item(
                "lucky_rock",
                "Gambler's Lucky Rock",
                "A small yellow rock that makes your fingers tingly, but it promises to bring great fortune."));
        registerItem(new Item(
                "high_rollers_card",
                "The High Rollers Card",
                "A card symbolizing your gambling addiction. . . i mean great skill in the ways of profiteering!"));
        registerItem(new Item(
                "rat_poison",
                "Rat Poison",
                "A unlabeled bottle described to be rat poison. Was being used by the casino to deal with their vermin infestation."));


    }

    private static void registerFlags() {
        registerFlag("taken_level_one_disguise");
        registerFlag("taken_boss_food");
        registerFlag("pickpocketed_hotel_guest");
        registerFlag("elevator_broken");
        registerFlag("contractor_map_offer");
        registerFlag("vault_security_disabled");
        registerFlag("boss_interaction_complete");
        registerFlag("boss_poisoned");
        registerFlag("has_taken_rat_poison");
        registerFlag("password_told_to_checkpoint_guard");
        registerFlag("asked_about_games");
        registerFlag("asked_rules_for_slots");
        registerFlag("asked_rules_for_blackjack");
        registerFlag("playing_blackjack");
        registerFlag("cannot_double_down");
        registerFlag("has_found_drink");
    }

    // Registration Methods

    /**
     * Register a new room
     * @param room the room to add to the registry
     * @throws IllegalArgumentException if the given room ID already exists
     */
    private static void registerRoom(Room room) {
        Room previous = rooms.putIfAbsent(room.getID(), room);
        if (previous != null) {
            throw new IllegalArgumentException("Duplicate Room ID: " + room.getID());
        }
    }

    /**
     * Register a new actor
     * @param actor the actor to add to the registry
     * @throws IllegalArgumentException if the given actor ID already exists
     */
    private static void registerActor(Actor actor) {
        Actor previous = actors.putIfAbsent(actor.getID(), actor);
        if (previous != null) {
            throw new IllegalArgumentException("Duplicate Actor ID: " + actor.getID());
        }
    }

    /**
     * Register a new item
     * @param item the item to add to the registry
     * @throws IllegalArgumentException if the given item ID already exists
     */
    private static void registerItem(Item item) {
        Item previous = items.putIfAbsent(item.id(), item);
        if (previous != null) {
            throw new IllegalArgumentException("Duplicate Item ID: " + item.id());
        }
    }

    /**
     * Register a new flag
     * @param flagID the ID of the flag to add
     * @throws IllegalArgumentException if the given flag ID already exists
     */
    private static void registerFlag(String flagID) {
        if (!flags.add(flagID)) {
            throw new IllegalArgumentException("Duplicate Flag ID: " + flagID);
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
        Room room = rooms.get(roomID);
        if (room == null) {
            throw new IllegalArgumentException("No such room exists with ID: " + roomID);
        }
        return room;

    }

    /**
     * Get the actor object associated with a specific ID
     * @param actorID the actor ID to find the actor of
     * @return Actor object of the given actor
     * @throws IllegalArgumentException if the actor ID does not exist
     */
    public static Actor getActor(String actorID) {
        Actor actor = actors.get(actorID);
        if (actor == null) {
            throw new IllegalArgumentException("No such actor exists with ID: " + actorID);
        }
        return actor;
    }

    /**
     * Get the item object associated with a specific ID
     * @param itemID the item ID to find the item of
     * @return Item record of the given item
     * @throws IllegalArgumentException if the item ID does not exist
     */
    public static Item getItem(String itemID) {
        Item item = items.get(itemID);
        if (item == null) {
            throw new IllegalArgumentException("No such item exists with ID: " + itemID);
        }
        return item;
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

    /**
     * Checks to see if a given interactable Exists
     * @param interactableID the ID of the interactable to check
     * @return true if it exists, false otherwise
     */
    public static boolean interactableExists(String interactableID) {
        return roomExists(interactableID) || actorExists(interactableID);
    }

    public static boolean flagExists(String flagID) { return flags.contains(flagID); }



}
