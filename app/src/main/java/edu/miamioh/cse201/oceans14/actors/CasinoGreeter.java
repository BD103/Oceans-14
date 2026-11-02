package edu.miamioh.cse201.oceans14.actors;

import edu.miamioh.cse201.oceans14.engine.ActionOption;
import edu.miamioh.cse201.oceans14.engine.GameState;
import edu.miamioh.cse201.oceans14.engine.InteractionResult;
import edu.miamioh.cse201.oceans14.engine.InteractionTransition;

import java.util.ArrayList;
import java.util.List;

public class CasinoGreeter extends Actor{

    public CasinoGreeter() {
        super("casino_greeter", "Casino Greeter");
    }

    /**
     * Returns the description of the actor displayed to the player.
     *
     * @return String representing the actor description text.
     */
    @Override
    public String getDescription() {
        return "A well dressed man wearing the casino insignia";
    }

    @Override
    public List<ActionOption> getAvailableActions() {
        List<ActionOption> actions = new ArrayList<>();

        //Area for always present interactions
        actions.add(new ActionOption("ask_about_casino", "Ask him about the Casino"));

        // Only give the ability to pickpocket once
        if (!GameState.hasItem("staff_room_keycard")) {
            actions.add(new ActionOption("pickpocket_greeter", "Pickpocket the staff keycard"));
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
                    "No Such Action Exists",
                    InteractionTransition.NONE,
                    null
            );
        }

        // This switch statement matches the action ID to the appropriate function
        // The convention for writing these functions is to use underscores in the ID and
        // camel case for the corresponding function.
        // Ex. end_conversation -> endConversation()
        return switch (actionID) {
            case "ask_about_casino" -> askAboutCasino();
            case "pickpocket_greeter" -> pickpocketGreeter();
            case "end_conversation" -> endConversation();
            // Detect action IDs that are available but lack an implementation.
            default -> throw new IllegalStateException(
                    "A valid action does not exist in the performAction for " + this.getID() +
                            "\n Please make sure the list of available actions all have an action function."
            );
        };
    }

    /**
     * Provides the player with information about the casino.
     *
     * @return a NONE result containing the greeter's response
     */
    private InteractionResult askAboutCasino() {
        // TODO change hotel info
        return new InteractionResult(
                "This is the Hotel Vista, the gem of Atlantic City. This is your top location for all" +
                        " your gaming desires. A place where the daring come out with riches untold",
                InteractionTransition.NONE,
                null
        );
    }

    /**
     * Allows the player to steal the greeter's staff keycard.
     *
     * Adds staff_room_keycard to the player's inventory.
     * The action is unavailable after the keycard has been stolen.
     *
     * @return a NONE result confirming the keycard was stolen
     */
    private InteractionResult pickpocketGreeter() {
        GameState.addItem("staff_room_keycard");
        return new InteractionResult(
                "You ask the greeter about the building's architecture. As he turns to examine "
                        + "the archway you're pointing at, you slip his staff keycard out of his pocket.",
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
                "You thank the greeter and go on your merry way.",
                InteractionTransition.POP,
                null
        );
    }


}
