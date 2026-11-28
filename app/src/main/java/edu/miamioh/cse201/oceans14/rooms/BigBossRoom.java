package edu.miamioh.cse201.oceans14.rooms;

import edu.miamioh.cse201.oceans14.engine.*;

import java.util.ArrayList;
import java.util.List;


public class BigBossRoom extends Room {

    public BigBossRoom() {
        super("big_boss_room", "The Big Boss's Office");
    }

    /**
     * Returns the room description, this is the base text that displays
     * what a player sees when entering a room.
     * @return String representing the room description text.
     */
    @Override
    public String getDescription() {
        return "A lavish executive office. On the walls are many photos with famous celebrities and casino memorabilia. In the center is a large desk with stacks of paperwork, files, and photos. Sitting in a large chair is the Big Boss himself, dressed in a sharp suit.";
    }

    /**
     * Gives the list of available actions in a record containing the ID for the action
     * and the Label which is the display friendly name.
     * @return the list of ActionOption that the player can choose from.
     */
    @Override
    public List<ActionOption> getAvailableActions() {
        List<ActionOption> actions = new ArrayList<>();


        if (!GameState.flagIsActive("boss_interaction_complete")) {
            actions.add(new ActionOption("talk_to_big_boss", "Talk to the Big Boss"));
        } else {
            if (GameState.flagIsActive("boss_poisoned") && !GameState.hasItem("level_two_disguise")) {
                actions.add(new ActionOption("steal_guard_outfit", "Steal a guard outfit"));
            }
            if (GameState.hasItem("level_two_disguise")) {
                actions.add(new ActionOption("enter_panopticon_room", "Enter the Panopticon"));
            }
        }
        return List.copyOf(actions);
    }

    /**
     * Performs the requested action in the Big Boss's office.
     *
     * @param actionID the ID of the requested interaction.
     * @return an InteractionResult representing the outcome.
     */
    @Override
    public InteractionResult performAction(String actionID) {
        if (!this.hasValidAction(actionID)) {
            return new InteractionResult(
                    new Dialogue.Error("No Such Action Exists"),
                    InteractionTransition.NONE,
                    null
            );
        }

        return switch (actionID) {
            case "enter_panopticon_room" -> enterPanopticonRoom();
            case "talk_to_big_boss" -> talkToBigBoss();
            case "steal_guard_outfit" -> stealGuardOutfit();

            default -> throw new IllegalStateException(
                    "A valid action does not exist in the performAction for " + this.getID() +
                            "\n Please make sure the list of available actions all have an action function."
            );
        };
    }

    /**
     * Initiates an interaction with the Big Boss.
     * Pushes the Big Boss actor onto the interaction stack.
     *
     * @return a PUSH result beginning the Big Boss interaction.
     */
    private InteractionResult talkToBigBoss() {
        return new InteractionResult(
                new Dialogue.System("You saunter up to the Big Boss, feeling confident with every stride that you will empty this man's pockets by day's end."),
                InteractionTransition.PUSH,
                GameWorld.getActor("big_boss")
        );
    }

    /**
     * Allows the player to take a Level 2 disguise after poisoning
     * the Big Boss. Adds the disguise to the player's inventory.
     *
     * @return a NONE result describing the acquisition of the disguise.
     */
    private InteractionResult stealGuardOutfit() {
        GameState.addItem("level_two_disguise");
        return new InteractionResult(
                new Dialogue.System("You snag a \033[1mguard outfit\033[0m from a box of new uniforms. After fixing your hat in the mirror you are looking snazzy."),
                InteractionTransition.NONE,
                null
        );
    }

    /**
     * Allows the player to leave the Big Boss's office and
     * enter the Panopticon after acquiring a Level 2 disguise.
     *
     * @return a CHANGE_ROOM result targeting the Panopticon.
     */
    private InteractionResult enterPanopticonRoom() {
        return new InteractionResult(
                new Dialogue.System("You check that your guard uniform is straight, then walk through the security door into the Panopticon."),
                InteractionTransition.CHANGE_ROOM,
                GameWorld.getRoom("panopticon_room")
        );
    }


}
