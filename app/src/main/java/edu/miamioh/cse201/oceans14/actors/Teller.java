package edu.miamioh.cse201.oceans14.actors;

import edu.miamioh.cse201.oceans14.engine.*;

import java.util.ArrayList;
import java.util.List;


public class Teller extends Actor {

    // TODO update constructor
    public Teller() {
        super("teller", "Teller");
    }

    /**
     * Returns the description of the actor displayed to the player.
     *
     * @return String representing the actor description text.
     */
    @Override
    public String getDescription() {
        // TODO replace description text
        return "Its a teller";
    }

    // TODO update getAvailableActions
    @Override
    public List<ActionOption> getAvailableActions() {
        List<ActionOption> actions = new ArrayList<>();

        //Area for always present interactions
        actions.add(new ActionOption("function_name", "Function Description"));

        // Area for conditional Interactions

        // Only offer function two if player has debug_item TODO delete me
        if (GameState.hasItem("debug_item")) {
            actions.add(new ActionOption("function_name_two", "Function Description Two"));
        }

        // End conversation
        actions.add(new ActionOption("end_conversation", "End the conversation"));

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
        // Ex. end_conversation -> endConversation()
        // TODO update perform action
        return switch (actionID) {
            case "function_name" -> functionName();
            case "function_name_two" -> functionNameTwo();

            // End the conversation always exists
            case "end_conversation" -> endConversation();
            // Detect action IDs that are available but lack an implementation.
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
                // TODO change dialogue message / type
                new Dialogue.System("This is a system message for " + this.getID()),
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
                // TODO change dialogue message / type
                new Dialogue.Actor(this.getName(), "This is an actor message for " + this.getID()),
                // TODO Make sure the InteractionTransition is correct
                InteractionTransition.CHANGE_ROOM,
                // TODO make sure we point to the right room / actor
                null
        );
    }

    /**
     * Ends the conversation
     *
     * @return a POP result returning to the previous interaction
     */
    private InteractionResult endConversation() {
        // TODO update end convo text
        return new InteractionResult(
                new Dialogue.System("REPLACE ME"),
                InteractionTransition.POP,
                null
        );
    }


}
