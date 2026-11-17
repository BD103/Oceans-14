package edu.miamioh.cse201.oceans14.rooms;

import edu.miamioh.cse201.oceans14.actors.Dealer;
import edu.miamioh.cse201.oceans14.engine.*;

import java.util.List;

public class HighRollersRoom extends Room{

    public HighRollersRoom() { super("high_rollers_room", "High Rollers Lounge"); }

    /**
     * Returns the room description, this is the base text that displays
     * what a player sees when entering a room.
     * @return String representing the room description text.
     */
    @Override
    public String getDescription() {
        // TODO change this to reflect better writing, current just placeholder text that makes some sense.
        return "The room is a palace of opulence. Gilded doors and beautiful tables, all to incentivize losing your money!";
    }

    /**
     * Gives the list of available actions in a record containing the ID for the action
     * and the Label which is the display friendly name.
     * @return the list of ActionOption that the player can choose from.
     */
    @Override
    public List<ActionOption> getAvailableActions() {
        // This was done to ensure a immutable list is returned. This prevents some other
        // part of the program from attempting to alter the list of AvailableActions.
        return List.of(
                new ActionOption("enter_main_room", "Enter the main casino room"),
                new ActionOption("talk_to_dealer", "Talk to the dealer")
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

        // This switch statement matches the action ID to the appropriate function
        // The convention for writing these functions is to use underscores in the ID and
        // camel case for the corresponding function.
        // Ex. enter_casino -> enterCasino()

        // Additional Note: this is an "enhanced" switch statement which helps prevent fall through.
        // Pretty neat idea!
        return switch (actionID) {
            case "enter_main_room" -> enterCasinoRoom();
            case "talk_to_dealer" -> talkToDealer();
            default -> throw new IllegalStateException(
                    "A valid action does not exist in the performAction for " + this.getID() +
                            "\n Please make sure the list of available actions all have an action function."
            );
        };
    }

    /**
     * Enter the casino
     * @return The interactionResult telling GameEngine to enter the casino.
     */
    private InteractionResult enterCasinoRoom() {
        return new InteractionResult(
                new Dialogue.System("You walk through the gilded doors and enter the main room."),
                InteractionTransition.CHANGE_ROOM,
                GameWorld.getRoom("casino_room")
        );
    }

    /**
     * Walk up to the Crazy Gambler / Old Man and begin interacting with him.
     * @return InteractionResult telling GameEngine to talk to the guy.
     */
    private InteractionResult talkToDealer() {
        return new InteractionResult(
                new Dialogue.System("You walk up to the primly dressed casino dealer."),
                InteractionTransition.PUSH,
                GameWorld.getActor("dealer")
        );
    }

}
