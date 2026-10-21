package edu.miamioh.cse201.oceans14.rooms;

import edu.miamioh.cse201.oceans14.engine.Interactable;

/**
 * Abstract Class for all rooms
 * Contains information and behavior shared by all rooms in the game.
 */
public abstract class Room implements Interactable {
    private final String id;
    private final String name;

    /**
     * Creates a new rooms with a unique ID and display name
     *
     * @param id the unique ID for the room.
     * @param name the display name for the room.
     */
    protected Room(String id, String name) {
        if (id == null || name == null) {
            throw new NullPointerException("Neither ID nor Name can be null");
        }
        this.id = id;
        this.name = name;
    }

    /**
     * Gets the ID of the room
     * @return the ID of the room.
     */
    public final String getID() {
        return id;
    }

    /**
     * Gets the display name of the room
     * @return the display name of the room.
     */
    public final String getName() {
        return name;
    }



}
