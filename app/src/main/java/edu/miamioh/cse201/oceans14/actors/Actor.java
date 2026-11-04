package edu.miamioh.cse201.oceans14.actors;

import edu.miamioh.cse201.oceans14.engine.ActionOption;
import edu.miamioh.cse201.oceans14.engine.Interactable;

/**
 * Abstract class for all Actors
 * Contains information shared by all actors.
 */
public abstract class Actor extends Interactable {
    /**
     * Creates a new actor with the given id and name
     *
     * @param id the unique id of the actor
     * @param name the name of the actor.
     */
    protected Actor(String id, String name) {
        super(id, name);
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
