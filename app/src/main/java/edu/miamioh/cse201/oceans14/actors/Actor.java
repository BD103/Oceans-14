package edu.miamioh.cse201.oceans14.actors;

import edu.miamioh.cse201.oceans14.engine.ActionOption;
import edu.miamioh.cse201.oceans14.engine.Interactable;
import edu.miamioh.cse201.oceans14.engine.InteractionResult;
import edu.miamioh.cse201.oceans14.engine.InteractionTransition;

import java.util.List;

/**
 * Abstract class for all Actors
 * Contains information shared by all actors.
 */
public abstract class Actor implements Interactable {

    // The ID and name of the room. These are unchangeable after initialization
    private final String id;
    private final String name;

    /**
     * Creates a new actor with the given id and name
     *
     * @param id the unique id of the actor
     * @param name the name of the actor.
     * @throws IllegalArgumentException if a duplicate ID is provided
     * @throws NullPointerException if null is provided for either input
     */
    protected Actor(String id, String name) {
        if (id == null || name == null) {
            throw new NullPointerException("Neither ID nor Name can be null");
        }
        this.id = id;
        this.name = name;
    }

    /**
     * Retrieves the name of the actor
     * @return the display friendly name of the actor
     */
    public final String getName() {
        return name;
    }

    /**
     * Retrieves the ID of the actor
     * @return the unique id of the actor
     */
    public final String getID() {
        return id;
    }

    /**
     * Check to see if the given input is a valid action
     * @param actionID the ID of the action that you want to perform.
     * @return True if the action exists and is available, else false
     */
    public boolean hasValidAction(String actionID) {
        for (ActionOption action : getAvailableActions()) {
            if (action.id().equals(actionID)) {
                return true;
            }
        }
        return false;
    }

}
