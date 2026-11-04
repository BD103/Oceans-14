package edu.miamioh.cse201.oceans14.rooms;

import edu.miamioh.cse201.oceans14.engine.*;


import java.util.ArrayList;
import java.util.List;

public class StaffRoom extends Room {
    public StaffRoom() {
        super("staff_room", "Staff Room");
    }

    /**
     * Returns the room description, this is the base text that displays
     * what a player sees when entering a room.
     * @return String representing the room description text.
     */
    @Override
    public String getDescription() {
        // TODO Replace the description Text
        return "The musty old staff room";
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
        actions.add(new ActionOption("enter_casino_room", "Return to the Casino Floor"));
        actions.add(new ActionOption("enter_kitchen_room", "Enter the Kitchen"));
        actions.add(new ActionOption("talk_to_anthony", "Talk to Anthony"));

        // Area for conditional Interactions
        // Only offer the uniform if the player doesn't have it
        if (!GameState.flagIsActive("taken_level_one_disguise")) {
            actions.add(new ActionOption("grab_staff_uniform", "Rummage Around for a staff outfit"));
        }

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
                    new Dialogue.Error("No Such Action Exists"),
                    InteractionTransition.NONE,
                    null
            );
        }

        // This switch statement matches the action ID to the appropriate function
        // The convention for writing these functions is to use underscores in the ID and
        // camel case for the corresponding function.
        // Ex. enter_casino -> enterCasino()
        return switch (actionID) {
            case "enter_casino_room" -> enterCasinoRoom();
            case "enter_kitchen_room" -> enterKitchenRoom();
            case "talk_to_anthony" -> talkToAnthony();
            case "grab_staff_uniform" -> grabStaffUniform();
            // Detect action IDs that are available but lack an implementation.
            default -> throw new IllegalStateException(
                    "A valid action does not exist in the performAction for " + this.getID() +
                            "\n Please make sure the list of available actions all have an action function."
            );
        };
    }

    /**
     * Returns the player to the main casino floor.
     * @return a CHANGE_ROOM result to casino_room
     */
    private InteractionResult enterCasinoRoom() {
        return new InteractionResult(
                new Dialogue.System("You leave the staff room for the main casino area."),
                InteractionTransition.CHANGE_ROOM,
                GameWorld.getRoom("casino_room")
        );
    }

    /**
     * Attempt to enter the kitchen from the staff room
     *
     * Requires a level_one_disguise to complete.
     * Anthony will stop the player otherwise
     *
     * @return a CHANGE_ROOM result if the player has the level_one_disguise
     *         otherwise NONE if access is denied.
     */
    private InteractionResult enterKitchenRoom() {
        // Validate having the level_one_disguise
        if (!GameState.hasItem("level_one_disguise")) {
            return new InteractionResult(
                    new Dialogue.Actor(GameWorld.getActor("anthony").getName(), "Hey buddy, you might want to suit up first"),
                    InteractionTransition.NONE,
                    null
            );
        } else {
            return new InteractionResult(
                    new Dialogue.System("You leave the staff area for the kitchen"),
                    InteractionTransition.CHANGE_ROOM,
                    GameWorld.getRoom("kitchen_room")
            );
        }
    }

    /**
     * Have the player begin talking to Anthony
     * @return a PUSH result targeting Anthony
     */
    private InteractionResult talkToAnthony() {
        return new InteractionResult(
                new Dialogue.System("You walk up to the bored looking staff member"),
                InteractionTransition.PUSH,
                GameWorld.getActor("anthony")
        );
    }

    /**
     * Gives the player the level one disguise in the form of a waiter outfit
     *
     * Adds level_one_disguise to GameState's inventory, this interaction will
     * not appear if the player already has the disguise
     *
     * @return a NONE result with a message affirming adding of the disguise.
     */
    private InteractionResult grabStaffUniform() {
        GameState.addItem("level_one_disguise");
        GameState.addFlag("taken_level_one_disguise");
        return new InteractionResult(
                new Dialogue.System("After shuffling around through a few lockers, you find a waiter outfit in your size"),
                InteractionTransition.NONE,
                null
        );
    }


}
