package edu.miamioh.cse201.oceans14.rooms;

import edu.miamioh.cse201.oceans14.engine.ActionOption;
import edu.miamioh.cse201.oceans14.engine.InteractionResult;
import edu.miamioh.cse201.oceans14.engine.InteractionTransition;


import java.util.List;

/* TODO DELETE ME WHEN DONE IMPLEMENTING
---------------------------------------------------------------------------------------------------
TEMPLATE FOR ALL ROOMS

This is a template for creating rooms. This will walk you through the conventions for programming
rooms correctly. All areas requiring a change have a TODO on them. Please ensure all areas are
changed and cleaned up.

Naming Convention:
All rooms should follow the format for class name -> NameRoom
ex. EntranceRoom, HotelLobbyRoom, PanopticonRoom

Room ID's should be in snake case -> name_room
ex. entrance_room, panopticon_room

Action IDs and their corresponding function follow snake case for the ID and camel case
for the function
Ex. ID: enter_casino, Function: enterCasino

---------------------------------------------------------------------------------------------------
 */

// TODO change the room class name
public class TemplateRoom extends Room {

    // TODO change the constructor
    public TemplateRoom() {
        // TODO change the ID and Name
        super("entrance_room", "Casino Entrace");
    }

    /**
     * Returns the room description, this is the base text that displays
     * what a player sees when entering a room.
     * @return String representing the room description text.
     */
    @Override
    public String getDescription() {
        // TODO Replace the description Text
        return "Replace the template text for " + this.getID();
    }

    /**
     * Gives the list of available actions in a record containing the ID for the action
     * and the Label which is the display friendly name.
     * @return the list of ActionOption that the player can choose from.
     */
    @Override
    public List<ActionOption> getAvailableActions() {
        // This was done to ensure a immutable list is returned. This prevents some other
        // part of the program from attempting to alter the list of AvailableActions.
        // TODO add the list of actions for this room
        return List.of(
                new ActionOption("function_name", "Function Description"),
        new ActionOption("function_name_two", "Function Description Two")

        );
    }

    /**
     * Performs the action ID requested
     * @param actionID The string ID of the interaction to run
     * @return InteractionResult representing the outcome of the action.
     *         In the case of an invalid action returns "No Such Action Exists" in the form
     *         of an interaction result.
     */
    @Override
    public InteractionResult performAction(String actionID) {
        // Check that the action requested exists. If not
        // Return a No Such Action Exists InteractionResult.
        if(!this.hasValidAction(actionID)) {
            return new InteractionResult(
                    "No Such Action Exists",
                    InteractionTransition.NONE,
                    null
            );
        }

        // This switch statement matches the action ID to the appropriate function
        // The convention for writing these functions is to use underscores in the ID and
        // camel case for the corresponding function.
        // Ex. enter_casino -> enterCasino()

        // Additional Note: this is an "enhanced" switch statement which helps prevent fall through.
        // Pretty neat idea!
        //TODO add to the switch statement with action ID's and the function names
        return switch (actionID) {
            case "function_name" -> functionName();
            case "function_name_two" -> functionNameTwo();
            // This is the case that we has programmers messed up. It gives an error
            // message so we can make sure all valid action ID's have corresponding functions.
            default -> throw new IllegalStateException(
                    "A valid action does not exist in the performAction for " + this.getID() +
                            "\n Please make sure the list of available actions all have an action function."
            );
        };
    }

    /**
     * TODO edit the javadoc for this
     * Do the function
     * @return The interactionResult telling GameEngine to do the function
     */
    private InteractionResult functionName() {
        return new InteractionResult(
                // TODO change function description
                "THIS SHOULD NOT BE SHOWN CHECK" + this.getID(),
                // TODO Make sure the InteractionTransition is correct
                InteractionTransition.CHANGE_ROOM,
                // TODO make sure we point to the right room / actor
                null
        );
    }
    /**
     * TODO edit the javadoc for this
     * Do the function
     * @return The interactionResult telling GameEngine to do the function
     */
    private InteractionResult functionNameTwo() {
        return new InteractionResult(
                // TODO change function description
                "THIS SHOULD NOT BE SHOWN CHECK" + this.getID(),
                // TODO Make sure the InteractionTransition is correct
                InteractionTransition.CHANGE_ROOM,
                // TODO make sure we point to the right room / actor
                null
        );
    }

}
