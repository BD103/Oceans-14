package edu.miamioh.cse201.oceans14.actors;

import edu.miamioh.cse201.oceans14.engine.*;

import java.util.ArrayList;
import java.util.List;

public class BigBoss extends Actor {

    public BigBoss() {
        super("big_boss", "Big Boss");
    }

    /**
     * Returns the description of the actor displayed to the player.
     *
     * @return String representing the actor description text.
     */
    @Override
    public String getDescription() {
        // TODO in the future update for a dynamic description for when he has been poisoned.
        return "An imposing 6'9\" man who looks like he chews glass for breakfast and kicks orphans for exercise. He is smugly reading about the poor saps who lost it all in the past month.";
    }

    @Override
    public List<ActionOption> getAvailableActions() {
        List<ActionOption> actions = new ArrayList<>();

        if (!GameState.flagIsActive("boss_interaction_complete")) {
            actions.add(new ActionOption("ask_about_casino", "Ask About the Casino"));

            if (GameState.hasItem("boss_food")) {
                actions.add(new ActionOption("deliver_food", "Deliver the Big Boss's Food"));
            }

            if (GameState.hasItem("boss_food") && GameState.hasItem("rat_poison")) {
                actions.add(new ActionOption("poison_and_deliver_food", "Poison and Deliver the Food"));
            }
        }

        actions.add(new ActionOption("end_conversation", "End the Conversation"));

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

        return switch (actionID) {
            case "ask_about_casino" -> askAboutCasino();
            case "deliver_food" -> deliverFood();
            case "poison_and_deliver_food" -> poisonAndDeliverFood();

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
     * Allows the player to ask the Big Boss about the casino's history.
     * Provides dialogue about the casino and its previous visitors
     * without modifying the game state.
     *
     * @return a NONE result containing the Big Boss's dialogue.
     */
    private InteractionResult askAboutCasino() {
        return new InteractionResult(
                new Dialogue.Actor("Big Boss", "My Granddad started this casino 88 years ago. The Beach View Hotel is the premier spot for gambling in luxury. Too bad he picked New Jersey to settle. Something, something about lower taxes than New York. Almost lost this place when some rat bastard came in here with a \"Lucky Rock\" and nearly gambled his way to the top. A good sock to the head solved that. Anyways, do you have my food ready?"),
                InteractionTransition.NONE,
                null
        );
    }

    /**
     * Allows the player to deliver unpoisoned food to the Big Boss.
     * Removes the boss food and chef permission from the player's inventory,
     * awards the maintenance room key, and activates the
     * boss_interaction_complete flag.
     * Returns the player directly to the casino floor.
     *
     * @return a CHANGE_ROOM result targeting the casino room.
     */
    private InteractionResult deliverFood() {
        GameState.removeItem("boss_food");
        GameState.removeItem("chef_permission");
        GameState.addItem("maintenance_key");
        GameState.addFlag("boss_interaction_complete");
        return new InteractionResult(
                new Dialogue.Actor("Big Boss", "Mmmm, that is the good stuff. Oh by the way the chef had mentioned to me that he was having some kind of vermin problem with the vents, here is a key to the maintenance room, go grab a screwdriver and fix up those vents. Wouldn't want one to get loose. Now scram!"),
                // Note for reviewer, change room automatically clears the interaction stack so it is fine to call it here.
                InteractionTransition.CHANGE_ROOM,
                GameWorld.getRoom("casino_room")
        );
    }

    /**
     * Allows the player to poison and deliver food to the Big Boss.
     * Consumes the rat poison, boss food, and chef permission.
     * Activates the boss_interaction_complete and boss_poisoned flags,
     * allowing the player to steal the Level 2 disguise from the office.
     * The interaction remains active until the player ends the conversation.
     *
     * @return a NONE result containing the poisoning dialogue and narration.
     */
    private InteractionResult poisonAndDeliverFood() {
        GameState.removeItem("rat_poison");
        GameState.removeItem("boss_food");
        GameState.removeItem("chef_permission");
        GameState.addFlag("boss_interaction_complete");
        GameState.addFlag("boss_poisoned");
        return new InteractionResult(
                new Dialogue.List(List.of(
                        new Dialogue.Actor("Big Boss", "Uhhhhhhh, I am not feeling so -BLOUGHHHHHHHHHHHH"),
                        new Dialogue.System("Big Boss Is curled on the floor and unconscious, that definitely wasn't standard rat poison. Better Flee before catching a murder charge")
                )),
                InteractionTransition.NONE,
                null
        );
    }


    /**
     * Ends the conversation with the Big Boss.
     * The farewell changes depending on whether the boss was poisoned.
     *
     * @return a POP result returning to the Big Boss's office.
     */
    private InteractionResult endConversation() {
        if (GameState.flagIsActive("boss_poisoned")) {
            return new InteractionResult(
                    new Dialogue.System("You leave the Big Boss unconscious on the floor and quickly step away from his desk."),
                    InteractionTransition.POP,
                    null
            );
        }

        return new InteractionResult(
                new Dialogue.System("You step away from the Big Boss's desk as he returns to his business."),
                InteractionTransition.POP,
                null
        );
    }


}
