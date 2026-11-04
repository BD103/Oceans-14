package edu.miamioh.cse201.oceans14.rooms;

import edu.miamioh.cse201.oceans14.engine.ActionOption;
import edu.miamioh.cse201.oceans14.engine.Interactable;

/**
 * Abstract Class for all rooms
 * Contains information and behavior shared by all rooms in the game.
 */
public abstract class Room extends Interactable {
    /**
     * Creates a new rooms with a unique ID and display name
     *
     * @param id the unique ID for the room.
     * @param name the display name for the room.
     */
    protected Room(String id, String name) {
        super(id, name);
    }
}
