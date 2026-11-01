package edu.miamioh.cse201.oceans14.rooms;

import edu.miamioh.cse201.oceans14.engine.ActionOption;
import edu.miamioh.cse201.oceans14.engine.InteractionResult;
import edu.miamioh.cse201.oceans14.engine.InteractionTransition;
import edu.miamioh.cse201.oceans14.engine.Dialogue;


import java.util.List;

public class EntranceRoom extends Room {

    public EntranceRoom() {
        super("entrance_room", "Casino Entrance");
    }

    /**
     * Returns the room description, this is the base text that displays
     * what a player sees when entering a room.
     * @return String representing the room description text.
     */
    @Override
    public String getDescription() {
        // TODO change this to reflect better writing, current just placeholder text that makes some sense.
        return "CHANGE_ME: You stand at the entrance of the CASINO NAME TDB, a massive building, and your Target";
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
                new ActionOption("enter_casino", "Enter the Casino"),
                new ActionOption("talk_to_crazy", "Talk to the Crazy Gambler"),
                new ActionOption("talk_to_greeter", "Talk to the Casino Greeter")

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
            case "enter_casino" -> enterCasino();
            case "talk_to_crazy" -> talkToCrazy();
            case "talk_to_greeter" -> talkToGreeter();
            // This is the case that we has programmers messed up. It gives an error
            // message so we can make sure all valid action ID's have corresponding functions.
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
    private InteractionResult enterCasino() {
        return new InteractionResult(
                new Dialogue.System("You walk through the glass door and enter the casino."),
                InteractionTransition.CHANGE_ROOM,
                null // TODO Add the casino main room here
                );
    }

    /**
     * Walk up to the Crazy Gambler / Old Man and begin interacting with him.
     * // TODO formalize gamblers name / is he an old man or what?
     * @return InteractionResult telling GameEngine to talk to the guy.
     */
    private InteractionResult talkToCrazy() {
        return new InteractionResult(
                new Dialogue.System("You walk up to the shaggy old man"),
                InteractionTransition.PUSH,
                null// TODO add the crazy old man.
        );
    }

    /**
     * Talk to the Casino Greeter
     * @return InteractionResult telling GameEngine to talk to the casino greeter.
     */
    private InteractionResult talkToGreeter() {
        return new InteractionResult(
                new Dialogue.System("You walk up to the Casino greeter."),
                InteractionTransition.PUSH,
                null // TODO add the casino greeter
        );
    }
}
