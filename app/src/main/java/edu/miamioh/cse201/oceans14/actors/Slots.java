package edu.miamioh.cse201.oceans14.actors;

import edu.miamioh.cse201.oceans14.engine.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Slots extends Actor{
    public Slots() { super( "slot_machine", "Slots Machine");}

    @Override
    public String getDescription() {
        return "A standard, crazily lit up slots machine";
    }

    @Override
    public List<ActionOption> getAvailableActions() {
        List<ActionOption> actions = new ArrayList<>();

        //Area for always present interactions
        actions.add(new ActionOption("check_out_machines", "Check out the slot machines"));

        // Tiered amounts for the bets
        // I don't want to have to do multiple inputs at once or two inputs in a row
        // 5, 25 50 are low rollers
        // In the high rollers you can bet 100, 250 and 500
        if (GameState.getChipCount() >= 10) {
           actions.add(new ActionOption("bet_10", "Play the slots for ten chips"));
        }
        if (GameState.getChipCount() >= 25) {
            actions.add(new ActionOption("bet_25", "Play the slots for twenty-five chips"));
        }
        if (GameState.getChipCount() >= 50) {
            actions.add(new ActionOption("bet_50", "Play the slots for fifty chips"));
        }
        if (GameState.getChipCount() >= 100 && (GameState.getCurrentRoomID().equals("high_rollers_room"))) {
            actions.add(new ActionOption("bet_100", "Play the slots for hundred chips"));
        }
        if (GameState.getChipCount() >= 250 && (GameState.getCurrentRoomID().equals("high_rollers_room"))) {
            actions.add(new ActionOption("bet_250", "Play the slots for two hundred fifty chips"));
        }
        if (GameState.getChipCount() >= 500 && (GameState.getCurrentRoomID().equals("high_rollers_room"))) {
            actions.add(new ActionOption("bet_500", "Play the slots for five hundred chips"));
        }

        // End conversation
        actions.add(new ActionOption("end_conversation", "Stop gambling"));
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
            //case "end_conversation" -> endConversation();
            case "bet_10" -> bet10();
            case "bet_25" -> bet25();
            case "bet_50" -> bet50();
            case "bet_100" -> bet100();
            case "bet_250" -> bet250();
            case "bet_500" -> bet500();
            case "end_conversation" -> endConversation();
            // Detect action IDs that are available but lack an implementation.
            default -> throw new IllegalStateException(
                    "A valid action does not exist in the performAction for " + this.getID() +
                            "\n Please make sure the list of available actions all have an action function."
            );
        };
    }

    Random rng = new Random();

    private InteractionResult bet10() {
        int randInt = rng.nextInt(100) + 1;
        int bet = 10;
        GameState.removeChips(bet);
        if (randInt == 100){
            GameState.addChips(bet * 10);
            return new InteractionResult(
                    new Dialogue.System("You win it big!! ten times your bet"),
                    InteractionTransition.NONE,
                    null
            );
        } else if (randInt > 90) {
            GameState.addChips(bet * 5);
            return new InteractionResult(
                    new Dialogue.System("You win a bunch! five times your bet"),
                    InteractionTransition.NONE,
                    null
            );
        } else if (randInt > 50) {
            GameState.addChips(bet * 2);
            return new InteractionResult(
                    new Dialogue.System("Nice! you double your bet!"),
                    InteractionTransition.NONE,
                    null
            );
        } else if (randInt > 25) {
            GameState.addChips(bet);
            return new InteractionResult(
                    new Dialogue.System("You don't win a single chip, don't lose any either"),
                    InteractionTransition.NONE,
                    null
            );
        } else {
            return new InteractionResult(
                    new Dialogue.System("Rats! you lose your bet"),
                    InteractionTransition.NONE,
                    null
            );
        }
    }

    private InteractionResult bet25() {
        int randInt = rng.nextInt(100) + 1;
        int bet = 25;
        GameState.removeChips(bet);
        if (randInt == 100){
            GameState.addChips(bet * 10);
            return new InteractionResult(
                    new Dialogue.System("You win it big!! ten times your bet"),
                    InteractionTransition.NONE,
                    null
            );
        } else if (randInt > 90) {
            GameState.addChips(bet * 5);
            return new InteractionResult(
                    new Dialogue.System("You win a bunch! five times your bet"),
                    InteractionTransition.NONE,
                    null
            );
        } else if (randInt > 50) {
            GameState.addChips(bet * 2);
            return new InteractionResult(
                    new Dialogue.System("Nice! you double your bet!"),
                    InteractionTransition.NONE,
                    null
            );
        } else if (randInt > 25) {
            GameState.addChips(bet);
            return new InteractionResult(
                    new Dialogue.System("You don't win a single chip, don't lose any either"),
                    InteractionTransition.NONE,
                    null
            );
        } else {
            return new InteractionResult(
                    new Dialogue.System("Rats! you lose your bet"),
                    InteractionTransition.NONE,
                    null
            );
        }
    }

    private InteractionResult bet50() {
        int randInt = rng.nextInt(100) + 1;
        int bet = 50;
        GameState.removeChips(bet);
        if (randInt == 100){
            GameState.addChips(bet * 10);
            return new InteractionResult(
                    new Dialogue.System("You win it big!! ten times your bet"),
                    InteractionTransition.NONE,
                    null
            );
        } else if (randInt > 90) {
            GameState.addChips(bet * 5);
            return new InteractionResult(
                    new Dialogue.System("You win a bunch! five times your bet"),
                    InteractionTransition.NONE,
                    null
            );
        } else if (randInt > 50) {
            GameState.addChips(bet * 2);
            return new InteractionResult(
                    new Dialogue.System("Nice! you double your bet!"),
                    InteractionTransition.NONE,
                    null
            );
        } else if (randInt > 25) {
            GameState.addChips(bet);
            return new InteractionResult(
                    new Dialogue.System("You don't win a single chip, don't lose any either"),
                    InteractionTransition.NONE,
                    null
            );
        } else {
            return new InteractionResult(
                    new Dialogue.System("Rats! you lose your bet"),
                    InteractionTransition.NONE,
                    null
            );
        }
    }

    private InteractionResult bet100() {
        int randInt = rng.nextInt(100) + 1;
        int bet = 100;
        GameState.removeChips(bet);
        if (randInt == 100){
            GameState.addChips(bet * 10);
            return new InteractionResult(
                    new Dialogue.System("You win it big!! ten times your bet"),
                    InteractionTransition.NONE,
                    null
            );
        } else if (randInt > 90) {
            GameState.addChips(bet * 5);
            return new InteractionResult(
                    new Dialogue.System("You win a bunch! five times your bet"),
                    InteractionTransition.NONE,
                    null
            );
        } else if (randInt > 50) {
            GameState.addChips(bet * 2);
            return new InteractionResult(
                    new Dialogue.System("Nice! you double your bet!"),
                    InteractionTransition.NONE,
                    null
            );
        } else if (randInt > 25) {
            GameState.addChips(bet);
            return new InteractionResult(
                    new Dialogue.System("You don't win a single chip, don't lose any either"),
                    InteractionTransition.NONE,
                    null
            );
        } else {
            return new InteractionResult(
                    new Dialogue.System("Rats! you lose your bet"),
                    InteractionTransition.NONE,
                    null
            );
        }
    }

    private InteractionResult bet250() {
        int randInt = rng.nextInt(100) + 1;
        int bet = 250;
        GameState.removeChips(bet);
        if (randInt == 100){
            GameState.addChips(bet * 10);
            return new InteractionResult(
                    new Dialogue.System("You win it big!! ten times your bet"),
                    InteractionTransition.NONE,
                    null
            );
        } else if (randInt > 90) {
            GameState.addChips(bet * 5);
            return new InteractionResult(
                    new Dialogue.System("You win a bunch! five times your bet"),
                    InteractionTransition.NONE,
                    null
            );
        } else if (randInt > 50) {
            GameState.addChips(bet * 2);
            return new InteractionResult(
                    new Dialogue.System("Nice! you double your bet!"),
                    InteractionTransition.NONE,
                    null
            );
        } else if (randInt > 25) {
            GameState.addChips(bet);
            return new InteractionResult(
                    new Dialogue.System("You don't win a single chip, don't lose any either"),
                    InteractionTransition.NONE,
                    null
            );
        } else {
            return new InteractionResult(
                    new Dialogue.System("Rats! you lose your bet"),
                    InteractionTransition.NONE,
                    null
            );
        }
    }

    private InteractionResult bet500() {
        int randInt = rng.nextInt(100) + 1;
        int bet = 500;
        GameState.removeChips(bet);
        if (randInt == 100){
            GameState.addChips(bet * 10);
            return new InteractionResult(
                    new Dialogue.System("You win it big!! ten times your bet"),
                    InteractionTransition.NONE,
                    null
            );
        } else if (randInt > 90) {
            GameState.addChips(bet * 5);
            return new InteractionResult(
                    new Dialogue.System("You win a bunch! five times your bet"),
                    InteractionTransition.NONE,
                    null
            );
        } else if (randInt > 50) {
            GameState.addChips(bet * 2);
            return new InteractionResult(
                    new Dialogue.System("Nice! you double your bet!"),
                    InteractionTransition.NONE,
                    null
            );
        } else if (randInt > 25) {
            GameState.addChips(bet);
            return new InteractionResult(
                    new Dialogue.System("You don't win a single chip, don't lose any either"),
                    InteractionTransition.NONE,
                    null
            );
        } else {
            return new InteractionResult(
                    new Dialogue.System("Rats! you lose your bet"),
                    InteractionTransition.NONE,
                    null
            );
        }
    }

    private InteractionResult endConversation() {
        return new InteractionResult(
                new Dialogue.System("You decide to stop playing slots"),
                InteractionTransition.POP,
                null
        );
    }
}
