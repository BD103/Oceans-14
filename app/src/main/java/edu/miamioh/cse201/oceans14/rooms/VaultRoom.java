package edu.miamioh.cse201.oceans14.rooms;

import edu.miamioh.cse201.oceans14.engine.ActionOption;
import edu.miamioh.cse201.oceans14.engine.GameState;
import edu.miamioh.cse201.oceans14.engine.InteractionResult;
import edu.miamioh.cse201.oceans14.engine.InteractionTransition;
import edu.miamioh.cse201.oceans14.engine.Dialogue;


import java.util.ArrayList;
import java.util.List;

public class VaultRoom extends Room {

    public VaultRoom() {
        super("vault_room", "The Vault");
    }

    /**
     * Returns the room description, this is the base text that displays
     * what a player sees when entering a room.
     * @return String representing the room description text.
     */
    @Override
    public String getDescription() {
        // TODO Replace the description Text
        return "A vault filled to the brim with cash, gold, and rare collectible figures!";
    }

    /**
     * Gives the list of available actions in a record containing the ID for the action
     * and the Label which is the display friendly name.
     * @return the list of ActionOption that the player can choose from.
     */
    @Override
    public List<ActionOption> getAvailableActions() {
        return List.of(
                new ActionOption("rob_and_loot", "Rob and Loot the Vault")
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
                    new Dialogue.Error("No Such Action Exists"),
                    InteractionTransition.NONE,
                    null
            );
        }

        return switch (actionID) {
            case "rob_and_loot" -> robAndLoot();
            // Detect action IDs that are available but lack an implementation.
            default -> throw new IllegalStateException(
                    "A valid action does not exist in the performAction for " + this.getID() +
                            "\n Please make sure the list of available actions all have an action function."
            );
        };
    }

    /**
     * Allows the player to rob the casino vault and win the game.
     * Intentionally throws an error to end the game
     *
     * @return never returns because the game ends with an error.
     */
    private InteractionResult robAndLoot() {
        throw new IllegalAccessError("Wait, how did you get in there????? 404 SOLUTION NOT FOUND");
    }

}
