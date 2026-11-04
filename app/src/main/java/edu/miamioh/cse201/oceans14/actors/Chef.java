package edu.miamioh.cse201.oceans14.actors;

import edu.miamioh.cse201.oceans14.engine.*;

import java.util.ArrayList;
import java.util.List;

public class Chef extends Actor {

    public Chef() {
        super("chef", "Chef");
    }

    /**
     * Returns the description of the actor displayed to the player.
     *
     * @return String representing the actor description text.
     */
    @Override
    public String getDescription() {
        // TODO replace description text
        return "A tall angry chef trying to make sure things run smoothly";
    }

    @Override
    public List<ActionOption> getAvailableActions() {
        List<ActionOption> actions = new ArrayList<>();

        if (!GameState.flagIsActive("taken_boss_food")) {
            actions.add(new ActionOption("ask_for_work", "Ask Chef for Work"));
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
        return switch (actionID) {
            case "ask_for_work" -> askForWork();
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
     * Allows the player to ask Chef for work.
     *
     * Gives the player the boss's food and chef permission note.
     * Activates the taken_boss_food flag, preventing the player
     * from requesting the assignment again.
     *
     * @return a NONE result confirming the assignment was accepted
     */
    private InteractionResult askForWork() {
        GameState.addItem("boss_food");
        GameState.addItem("chef_permission");
        GameState.addFlag("taken_boss_food");
        return new InteractionResult(
                new Dialogue.Actor(this.getName(), "Yeah, actually, can you deliver this food to the big boss? " +
                        "Here's a permission note so security will let you through."),
                InteractionTransition.NONE,
                null
        );
    }

    /**
     * Ends the conversation
     *
     * @return a POP result returning to the previous interaction
     */
    private InteractionResult endConversation() {
        return new InteractionResult(
                new Dialogue.System("Chef looks away as he gets back to work"),
                InteractionTransition.POP,
                null
        );
    }


}
