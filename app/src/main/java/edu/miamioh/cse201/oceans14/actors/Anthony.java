package edu.miamioh.cse201.oceans14.actors;

import edu.miamioh.cse201.oceans14.engine.*;

import java.util.ArrayList;
import java.util.List;

public class Anthony extends Actor {

    public Anthony() {
        super("anthony", "Anthony");
    }

    /**
     * Returns the description of the actor displayed to the player.
     *
     * @return String representing the actor description text.
     */
    @Override
    public String getDescription() {
        // TODO replace description text
        return "A younger man who is playing on his phone";
    }

    @Override
    public List<ActionOption> getAvailableActions() {
        List<ActionOption> actions = new ArrayList<>();

        //Area for always present interactions
        actions.add(new ActionOption("ask_about_day", "Ask About Day"));

        // Area for conditional Interactions

        // Only offer screwdriver once
        if (!GameState.flagIsActive("taken_screwdriver") && GameState.hasItem("screwdriver_checkout_note")) {
            actions.add(new ActionOption("ask_for_screwdriver", "Ask For Screwdriver"));
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
            case "ask_about_day" -> askAboutDay();
            case "ask_for_screwdriver" -> askForScrewdriver();

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
     * Allows the player to ask Anthony about his day.
     *
     * @return a NONE result containing Anthony's response
     */
    private InteractionResult askAboutDay() {
        return new InteractionResult(
                new Dialogue.Actor(this.getName(), "My day? Eh, I've had better, and I've had worse."),
                InteractionTransition.NONE,
                null
        );
    }

    /**
     * Allows the player to retrieve the screwdriver from Anthony.
     *
     * Requires the player to possess the screwdriver checkout note.
     * Adds the screwdriver to the player's inventory and activates
     * the taken_screwdriver flag, preventing repeated collection.
     *
     * @return a NONE result confirming the screwdriver was received
     */
    private InteractionResult askForScrewdriver() {
        GameState.addItem("screwdriver");
        GameState.addFlag("taken_screwdriver");
        return new InteractionResult(
                new Dialogue.Actor(this.getName(), "Oh shoot, I forgot I borrowed that! Here you go. Just return it to the storage room when you're done."),
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
                new Dialogue.System("Anthony wishes you luck before returning to Candy Crush."),
                InteractionTransition.POP,
                null
        );
    }


}
