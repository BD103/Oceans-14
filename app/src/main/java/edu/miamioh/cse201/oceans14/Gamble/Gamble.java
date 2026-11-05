package edu.miamioh.cse201.oceans14.engine;

import java.util.List;
import edu.miamioh.cse201.oceans14.engine.ActionOption;
import edu.miamioh.cse201.oceans14.engine.Interactable;

/**
 * Defines the common interaction behavior for game objects
 * Implemented by Room and Actor, allowing GameEngine
 * to interact with them through a consistent interface.
 */
public abstract Gamble implements Interactable {
    private final String id;
    private final String name;

    /**
     * Returns a description of the current interaction
     * @return A string representing the description of the current action.
     */

    protected String getDescription() {
        if (id == null || name == null) {
            throw new NullPointerException("Neither ID nor Name can be null");
        }
        this.id = id;
        this.name = name;
    }

    /**
     * Gives the ID of the current interactable
     * @return the ID of the current interactable.
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

    /**
     * Returns the current list of available actions.
     * ActionOption are just a Java Record which holds an actions id and its
     * Description.
     * Ex. if option = ActionOption("listen_to_gossip", "Listen to the secretaries gossip");
     *      option.id() -> "listen_to_gossip"
     *      option.label() -> "Listen to the secretaries gossip"
     * @return A list of ActionOption that represent possible actions.
     */
    List<ActionOption> getAvailableActions();

    /**
     * Checks to see if the proposed action ID is a valid action for the current Interactable
     * @param proposedActionID the ID of the action that you want to perform.
     * @return True if the given action is valid, false if the action doesn't exist or can't be performed.
     */
    boolean hasValidAction(String proposedActionID) {
        for (ActionOption action : getAvailableActions()) {
            if (action.id().equals(actionID)) {
                return true;
            }
        }
        return false;
    }

}
