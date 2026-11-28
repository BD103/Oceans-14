package edu.miamioh.cse201.oceans14.rooms;

import edu.miamioh.cse201.oceans14.engine.*;

import java.util.ArrayList;
import java.util.List;


public class VentRoom extends Room {


    public VentRoom() {

        super("vent_room", "Vent room");
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
        actions.add(new ActionOption("go_to_vault", "Head into the vault"));
        actions.add(new ActionOption("go_to_maintenance", "Head into the maintenance room"));

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
            case "go_to_maintenance" -> goToMaintenance();
            case "go_to_vault" -> goToVault();
            // Detect action IDs that are available but lack an implementation.
            default -> throw new IllegalStateException(
                    "A valid action does not exist in the performAction for " + this.getID() +
                            "\n Please make sure the list of available actions all have an action function."
            );
        };
    }
    /**
     * Lets the player go to maintenance from vent
     * @return The interactionResult room change to maintenance
     */
    private InteractionResult goToMaintenance() {
        return new InteractionResult(
                new Dialogue.System("You head into the maintenance room..."),
                InteractionTransition.CHANGE_ROOM,
                GameWorld.getRoom("CasinoRoom")
        );
    }
    /**
     * Lets the player go to vault from vent room
     * @return The interactionResult room change to vault
     */
    private InteractionResult goToVault() {
        if (!GameState.hasItem("vent_map")) {
            return new InteractionResult(
                    new Dialogue.System("You try to navigate the dark and twisting vent system, but quickly get lost. If only you had \033[1mdirections\033[0m on where to go..."),
                    InteractionTransition.NONE,
                    null
            );
        }

        if (!GameState.flagIsActive("vault_security_disabled")) {
            return new InteractionResult(
                    new Dialogue.System("You crawl through the vents, but encounter lasers that will alert security of your intrusion. You need to \033[1mdisable the vault's security\033[0m before continuing on this path."),
                    InteractionTransition.NONE,
                    null
            );
        }

        return new InteractionResult(
                new Dialogue.System("You head into the vault..."),
                InteractionTransition.CHANGE_ROOM,
                GameWorld.getRoom("VaultRoom")
        );
    }
}
