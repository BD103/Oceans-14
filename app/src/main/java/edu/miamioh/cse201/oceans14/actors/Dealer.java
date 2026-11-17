package edu.miamioh.cse201.oceans14.actors;

import edu.miamioh.cse201.oceans14.engine.*;

import java.util.ArrayList;
import java.util.List;


public class Dealer extends Actor{
    public Dealer() { super( "dealer", "Dealer");}

    @Override
    public String getDescription() {
        return "A well dressed woman wearing the casino insignia";
    }

    @Override
    public List<ActionOption> getAvailableActions() {
        List<ActionOption> actions = new ArrayList<>();

        //Area for always present interactions



        // Only give the ability to ask what games once
        if(!GameState.flagIsActive("asked_about_games")) {
            actions.add(new ActionOption("ask_about_games", "Ask her about the games at the casino"));
        }

        //Can ask for rules any number of times, only after asking about the games
        if(GameState.flagIsActive("asked_about_games")) {
            actions.add(new ActionOption("rules_for_slots", "Ask her about the rules of the slot machines"));
        }

        if(GameState.flagIsActive("asked_about_games")) {
            actions.add(new ActionOption("rules_for_blackjack", "Ask her about the rules of blackjack"));
        }

//        if(GameState.flagIsActive("asked_about_games")) {
//            actions.add(new ActionOption("rules_for_baccarat", "Ask her about the rules of baccarat"));
//        }

        //After reading the rules at least once, you can ask the dealer to play the games

        if(GameState.flagIsActive("asked_rules_for_slots")) {
            actions.add(new ActionOption("play_slots", "Ask to play the slot machines"));
        }

        if(GameState.flagIsActive("asked_rules_for_blackjack")) {
            actions.add(new ActionOption("play_blackjack", "Ask to play blackjack"));
        }

//        if(GameState.flagIsActive("asked_rules_for_baccarat")) {
//            actions.add(new ActionOption("play_baccarat", "Ask to play baccarat"));
//        }

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
            case "ask_about_games" -> askAboutGames();
            case "end_conversation" -> endConversation();
            case "rules_for_slots" -> askRulesSlots();
            case "rules_for_blackjack" -> askRulesBlackjack();
            case "play_slots" -> playSlots();
            case "play_blackjack" -> playBlackjack();
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
    private InteractionResult askAboutGames() {
        // TODO temp dialogue, change later
        GameState.addFlag("asked_about_games");
        return new InteractionResult(
                new Dialogue.Actor(this.getName(), "There are three major games offered here: blackjack, slots, and +" +
                        " baccarat."),
                InteractionTransition.NONE,
                null
        );
    }

    private InteractionResult askRulesSlots() {
        // TODO temp dialogue, change later
        GameState.addFlag("asked_rules_for_slots");
        return new InteractionResult(
                new Dialogue.Actor(this.getName(), "Bet an amount and pull the lever, hope you get lucky"),
                InteractionTransition.NONE,
                null
        );
    }

    private InteractionResult askRulesBlackjack() {
        // TODO temp dialogue, change later
        GameState.addFlag("asked_rules_for_blackjack");
        return new InteractionResult(
                new Dialogue.Actor(this.getName(), "Bet an amount, then, 'Hit' to draw a card," +
                        " try to get cards that equal or are close to 21 without going over. " +
                        "Beat the dealer. Face cards are ten. Aces are always 11 in Jersey."),
                InteractionTransition.NONE,
                null
        );
    }

//    private InteractionResult askRulesBaccarat() {
//        // TODO temp dialogue, change later
//        GameState.addFlag("asked_about_games");
//        return new InteractionResult(
//                new Dialogue.Actor(this.getName(), "Bet an amount, then, decide who will win dealer, player or tie"),
//                InteractionTransition.NONE,
//                null
//        );
//    }

    // TODO change this to move the player
    private InteractionResult playSlots() {
        // TODO temp dialogue, change later
        GameState.addFlag("asked_rules_for_slots");
        return new InteractionResult(
                new Dialogue.Actor(this.getName(), "You walk over to the slot machines and start gambling"),
                InteractionTransition.PUSH,
                GameWorld.getActor("slot_machine")
        );
    }

    // TODO change this to move the player
    private InteractionResult playBlackjack() {
        // TODO temp dialogue, change later
        GameState.addFlag("asked_rules_for_slots");
        return new InteractionResult(
                new Dialogue.Actor(this.getName(), "You walk over to the blackjack tables and start gambling"),
                InteractionTransition.PUSH,
                GameWorld.getActor("blackjack")
        );
    }

    // TODO change this to move the player
//    private InteractionResult playBaccarat() {
//        // TODO temp dialogue, change later
//        GameState.addFlag("asked_rules_for_slots");
//        return new InteractionResult(
//                new Dialogue.Actor(this.getName(), "You walk over to the baccarat tables and start gambling"),
//                InteractionTransition.NONE,
//                null
//        );
//    }


    /**
     * Ends the conversation
     *
     * @return a POP result returning to the previous interaction
     */
    private InteractionResult endConversation() {
        return new InteractionResult(
                new Dialogue.System("You thank the dealer and go on your merry way."),
                InteractionTransition.POP,
                null
        );
    }


}
