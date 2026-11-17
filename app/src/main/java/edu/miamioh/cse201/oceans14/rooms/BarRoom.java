package edu.miamioh.cse201.oceans14.rooms;

import edu.miamioh.cse201.oceans14.engine.*;

import java.util.ArrayList;
import java.util.List;


public class BarRoom extends Room {

    public BarRoom() {
        super("bar_room", "Bar");
    }

    /**
     * Returns the room description, this is the base text that displays
     * what a player sees when entering a room.
     * @return String representing the room description text.
     */
    @Override
    public String getDescription() {
        return "It is a bar. The floor is sticky, and the guard seems to have had either a really rough or really good night.";
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
        actions.add(new ActionOption("look_for_drink", "Look around the bar for a drink"));
        actions.add(new ActionOption("talk_to_guard", "Talk to the guard"));
        actions.add(new ActionOption("go_to_kitchen", "Head into the kitchen"));
        actions.add(new ActionOption("go_to_casino", "Head into the casino"));
        // Area for conditional Interactions
        // Only offer function two if player has debug_item
        if (!GameState.hasItem("debug_item")) {
            actions.add(new ActionOption("function_name_two", "Function Description Two"));
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
        //TODO add to the switch statement with action ID's and the function names
        return switch (actionID) {
            case "look_for_drink" -> lookForDrink();
            case "talk_to_guard" -> talkToGuard();
            case "function_name_two" -> functionNameTwo();
            case "go_to_kitchen" -> goToKitchen();
            case "go_to_casino" -> goToCasino();
            // Detect action IDs that are available but lack an implementation.
            default -> throw new IllegalStateException(
                    "A valid action does not exist in the performAction for " + this.getID() +
                            "\n Please make sure the list of available actions all have an action function."
            );
        };
    }

    /**
     * Lets the player look for a drink and responds appropriately
     * @return The interactionResult with appropriate dialogue
     */
    private InteractionResult lookForDrink() {
        if (!GameState.flagIsActive("has_found_drink")) {
            GameState.addItem("Drink");
            return new InteractionResult(
                    new Dialogue.System("You found a drink. It's five o'clock somewhere..."),
                    InteractionTransition.NONE,
                    null
            );
        } else {
            return new InteractionResult(
                    new Dialogue.System("Nothing looks particularly good..."),
                    InteractionTransition.NONE,
                    null
            );
        }
    }

    /**
     * Lets the player talk to the Guard
     * @return The interactionResult with appropriate dialogue
     */
    private InteractionResult talkToGuard() {
        return new InteractionResult(
                new Dialogue.System("Maybe that guard knows something..."),
                InteractionTransition.PUSH,
                GameWorld.getActor("Guard")
        );
    }

    /**
     * Lets the player go to kitchen from bar
     * @return The interactionResult room change to kitchen
     */
    private InteractionResult goToKitchen() {
        return new InteractionResult(
                new Dialogue.System("You head into the kitchen..."),
                InteractionTransition.CHANGE_ROOM,
                GameWorld.getRoom("KitchenRoom")
        );
    }

    /**
     * Lets the player go to casino from bar
     * @return The interactionResult room change to casino
     */
    private InteractionResult goToCasino() {
        return new InteractionResult(
                new Dialogue.System("You head into the casino..."),
                InteractionTransition.CHANGE_ROOM,
                GameWorld.getRoom("CasinoRoom")
        );
    }

    /**
     * Debug
     * @return Debug message
     */
    private InteractionResult functionNameTwo() {
        return new InteractionResult(
                new Dialogue.Actor("Actor Name", "This is an actor message for " + this.getID()),
                InteractionTransition.CHANGE_ROOM,
                null
        );
    }

}
