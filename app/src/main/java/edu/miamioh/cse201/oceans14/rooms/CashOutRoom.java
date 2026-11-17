package edu.miamioh.cse201.oceans14.rooms;

import edu.miamioh.cse201.oceans14.engine.*;

import java.util.ArrayList;
import java.util.List;



public class CashOutRoom extends Room {


    public CashOutRoom() {
        super("cash_out_room", "Cash out room");
    }

    /**
     * Returns the room description, this is the base text that displays
     * what a player sees when entering a room.
     * @return String representing the room description text.
     */
    @Override
    public String getDescription() {
        return "money room";
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
        actions.add(new ActionOption("go_to_vault_checkpoint", "Head into the vault checkpoint"));
        actions.add(new ActionOption("go_to_casino", "Head into the casino"));

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
            case "go_to_casino" -> goToCasino();
            case "go_to_vault_checkpoint" -> goToVaultCheckpoint();
            // Detect action IDs that are available but lack an implementation.
            default -> throw new IllegalStateException(
                    "A valid action does not exist in the performAction for " + this.getID() +
                            "\n Please make sure the list of available actions all have an action function."
            );
        };
    }
    /**
     * Lets the player go to casino from cash out
     * @return The interactionResult room change to casino
     */
    private InteractionResult goToCasino() {
        return new InteractionResult(
                new Dialogue.System("You head into the casino..."),
                InteractionTransition.CHANGE_ROOM,
                GameWorld.getRoom("casino_room")
        );
    }
    /**
     * Lets the player go to vault from cash out
     * @return The interactionResult room change to vault
     */
    private InteractionResult goToVaultCheckpoint() {
        return new InteractionResult(
                new Dialogue.System("You head into the vault..."),
                InteractionTransition.CHANGE_ROOM,
                GameWorld.getRoom("checkpoint_room")
        );
    }
}
