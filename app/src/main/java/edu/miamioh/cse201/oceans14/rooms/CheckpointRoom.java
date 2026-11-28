package edu.miamioh.cse201.oceans14.rooms;

import edu.miamioh.cse201.oceans14.engine.*;

import java.util.List;

/**
 * The checkpoint room, which verifies you are wearing the level 2 disguise (guard outfit) and know
 * the secret password before letting you into the vault.
 */
public class CheckpointRoom extends Room {
    public CheckpointRoom() {
        super("checkpoint_room", "Vault Checkpoint");
    }

    @Override
    public String getDescription() {
        return "A long hallway with a guard at the very end, standing in front of the vault door";
    }

    @Override
    public List<ActionOption> getAvailableActions() {
        return List.of(
                new ActionOption("talk_to_guard", "Talk to Guard"),
                new ActionOption("enter_cash_out_room", "Enter cash out room")
        );
    }

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
            case "talk_to_guard" -> talkToGuard();
            case "enter_cash_out_room" -> enterCashOutRoom();
            // Detect action IDs that are available but lack an implementation.
            default -> throw new IllegalStateException(
                    "A valid action does not exist in the performAction for " + this.getID() +
                            "\n Please make sure the list of available actions all have an action function."
            );
        };
    }

    /**
     * Talks to the checkpoint guard. If you don't have the level 2 disguise, you immediately get
     * kicked out to the casino floor.
     */
    private InteractionResult talkToGuard() {
        if (GameState.hasItem("level_two_disguise")) {
            return new InteractionResult(
                    new Dialogue.System("You walk up to the guard at the end of the hallway."),
                    InteractionTransition.PUSH,
                    GameWorld.getActor("checkpoint_guard")
            );
        } else {
            return new InteractionResult(
                    new Dialogue.Actor("Guard", "Hey, \033[1myou're not a guard\033[0m! Go back to the casino floor!"),
                    InteractionTransition.CHANGE_ROOM,
                    GameWorld.getRoom("casino_room")
            );
        }
    }

    private InteractionResult enterCashOutRoom() {
        return new InteractionResult(
                new Dialogue.System("You leave the checkpoint hallway, entering the cash out room."),
                InteractionTransition.CHANGE_ROOM,
                GameWorld.getRoom("cash_out_room")
        );
    }
}
