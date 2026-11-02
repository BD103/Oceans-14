package edu.miamioh.cse201.oceans14.rooms;

import edu.miamioh.cse201.oceans14.engine.*;


import java.util.ArrayList;
import java.util.List;

public class KitchenRoom extends Room {

    public KitchenRoom() {
        super("kitchen_room", "Kitchen");
    }

    /**
     * Returns the room description, this is the base text that displays
     * what a player sees when entering a room.
     * @return String representing the room description text.
     */
    @Override
    public String getDescription() {
        // TODO Replace the description Text
        return "The sound of cooking fills your ears as you are in the kitchen";
    }

    /**
     * Gives the list of available actions in a record containing the ID for the action
     * and the Label which is the display friendly name.
     * @return the list of ActionOption that the player can choose from.
     */
    @Override
    public List<ActionOption> getAvailableActions() {
        List<ActionOption> actions = new ArrayList<>();

        //Area for always present interactions
        actions.add(new ActionOption("enter_staff_room", "Enter the Staff Room"));
        actions.add(new ActionOption("enter_bar_room", "Enter the Bar"));
        actions.add(new ActionOption("talk_to_chef", "Talk to the Chef"));
        return List.copyOf(actions);
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
        return switch (actionID) {
            case "enter_staff_room" -> enterStaffRoom();
            case "enter_bar_room" -> enterBarRoom();
            case "talk_to_chef" -> talkToChef();
            // Detect action IDs that are available but lack an implementation.
            default -> throw new IllegalStateException(
                    "A valid action does not exist in the performAction for " + this.getID() +
                            "\n Please make sure the list of available actions all have an action function."
            );
        };
    }

    /**
     * Returns the player to the staff room.
     *
     * @return a CHANGE_ROOM result targeting the staff room
     */
    private InteractionResult enterStaffRoom() {
        return new InteractionResult(
                "You leave the bustling kitchen and enter the staff room",
                InteractionTransition.CHANGE_ROOM,
                GameWorld.getRoom("staff_room")
        );
    }

    /**
     * Moves the player from the kitchen to the hotel bar.
     *
     * @return a CHANGE_ROOM result targeting the bar
     */
    private InteractionResult enterBarRoom() {
        return new InteractionResult(
                "You push through the swinging door and head to the bar",
                InteractionTransition.CHANGE_ROOM,
                GameWorld.getRoom("bar_room")
        );
    }

    /**
     * Begins a conversation with the chef running the kitchen.
     *
     * @return a PUSH result targeting the chef
     */
    private InteractionResult talkToChef() {
        return new InteractionResult(
                "You walk up to the chef running the kitchen",
                InteractionTransition.PUSH,
                GameWorld.getActor("chef")
        );
    }

}
