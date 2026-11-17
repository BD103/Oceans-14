package edu.miamioh.cse201.oceans14.actors;

import edu.miamioh.cse201.oceans14.engine.*;

import java.util.ArrayList;
import java.util.List;

public class Blackjack extends Actor {
    public Blackjack() { super( "blackjack", "Blackjack");}

    @Override
    public String getDescription() {
        return "A standard blackjack table, dealer stand on 17";
    }

    @Override
    public List<ActionOption> getAvailableActions() {
        List<ActionOption> actions = new ArrayList<>();

        //Area for always present interactions
        actions.add(new ActionOption("check_out_tables", "Check out the different tables"));

        // Tiered amounts for the bets
        // I don't want to have to do multiple inputs at once or two inputs in a row
        // 5, 25 50 are low rollers
        // In the high rollers you can bet 100, 250 and 500
        // Unlike slots, the card games take the bets immediately
        if (GameState.getChipCount() >= 10 && !GameState.flagIsActive("playing_blackjack")) {
            actions.add(new ActionOption("bet_10", "Sit down and bet ten chips"));
        }

        if (GameState.getChipCount() >= 25 && !GameState.flagIsActive("playing_blackjack")) {
            actions.add(new ActionOption("bet_25", "Sit down and bet twenty-five chips"));
        }

        if (GameState.getChipCount() >= 50 && !GameState.flagIsActive("playing_blackjack")) {
            actions.add(new ActionOption("bet_50", "Sit down and bet fifty chips"));
        }

        if (GameState.getChipCount() >= 100 && !GameState.flagIsActive("playing_blackjack") && (GameState.getCurrentRoomID().equals("high_rollers_room"))) {
            actions.add(new ActionOption("bet_100", "Sit down and bet one hundred chips"));
        }

        if (GameState.getChipCount() >= 250 && !GameState.flagIsActive("playing_blackjack") && (GameState.getCurrentRoomID().equals("high_rollers_room"))) {
            actions.add(new ActionOption("bet_250", "Sit down and bet two hundred fifty chips"));
        }

        if (GameState.getChipCount() >= 500 && !GameState.flagIsActive("playing_blackjack") && (GameState.getCurrentRoomID().equals("high_rollers_room"))) {
            actions.add(new ActionOption("bet_500", "Sit down and bet five hundred chips"));
        }

        if (GameState.flagIsActive("playing_blackjack")) {
            actions.add(new ActionOption("hit", "Hit"));
            actions.add(new ActionOption("stay", "Stay"));
        }

        if (GameState.flagIsActive("playing_blackjack")
                && !GameState.flagIsActive("cannot_double_down")
                && GameState.getChipCount() >= bet) {
            actions.add(new ActionOption("double_down", "Double down"));
        }

        // Only allow leaving between hands
        if (!GameState.flagIsActive("playing_blackjack")) {
            actions.add(new ActionOption("end_conversation", "Stop gambling"));
        }

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
            case "hit" -> hit();
            case "stay" -> stay();
            case "double_down" -> doubleDown();
            case "end_conversation" -> endConversation();
            // Detect action IDs that are available but lack an implementation.
            default -> throw new IllegalStateException(
                    "A valid action does not exist in the performAction for " + this.getID() +
                            "\n Please make sure the list of available actions all have an action function."
            );
        };
    }

    private InteractionResult checkOutTables() {
        return new InteractionResult(new Dialogue.System("There are many tables of blackjack, all for different stakes. " +
                "Many people are loudly counting their cards"),
                InteractionTransition.NONE,
                null);
    }

    //These are the player's and the dealer's hand
    ArrayList<Cards> playerHand = new ArrayList<>();
    ArrayList<Cards> dealerHand = new ArrayList<>();

    int bet = 0;

    private InteractionResult bet10()  { return startHand(10); }
    private InteractionResult bet25()  { return startHand(25); }
    private InteractionResult bet50()  { return startHand(50); }
    private InteractionResult bet100() { return startHand(100); }
    private InteractionResult bet250() { return startHand(250); }
    private InteractionResult bet500() { return startHand(500); }

    private InteractionResult startHand(int amount) {
        bet = amount;
        GameState.removeChips(bet);   // bets were never being deducted

        playerHand.clear();
        dealerHand.clear();

        playerHand.add(Cards.draw());
        playerHand.add(Cards.draw());
        dealerHand.add(Cards.draw());
        dealerHand.add(Cards.draw());

        boolean playerBlackjack = checkHand(playerHand).equals("blackjack");
        boolean dealerBlackjack = checkHand(dealerHand).equals("blackjack");

        if (playerBlackjack && dealerBlackjack) {
            GameState.addChips(bet);   // push
            return new InteractionResult(
                    new Dialogue.System("Both of you have blackjack, push. Player: " +
                            handToString(playerHand) + " Dealer: " + handToString(dealerHand)),
                    InteractionTransition.NONE,
                    null
            );
        } else if (playerBlackjack) {
            GameState.addChips(bet * 3);
            return new InteractionResult(
                    new Dialogue.System("Hey nice, blackjack! three times your bet! " +
                            handToString(playerHand)),
                    InteractionTransition.NONE,
                    null
            );
        } else if (dealerBlackjack) {
            return new InteractionResult(
                    new Dialogue.System("Unfortunate, dealer has blackjack. " + handToString(dealerHand)),
                    InteractionTransition.NONE,
                    null
            );
        } else {
            GameState.removeFlag("cannot_double_down");
            GameState.addFlag("playing_blackjack");
            return new InteractionResult(
                    new Dialogue.System("All bets are in, player's showing: " +
                            handToString(playerHand) + " Dealer is showing: " +
                            dealerHandToString(dealerHand)),
                    InteractionTransition.NONE,
                    null
            );
        }
    }

    private InteractionResult hit() {
        playerHand.add(Cards.draw());
        GameState.addFlag("cannot_double_down");   // can only double on the first action

        String condition = checkHand(playerHand);

        if (condition.equals("win")) {
            // Hit to 21: the dealer still gets to play, since they could tie
            return dealerPlays();
        } else if (condition.equals("bust")) {
            GameState.removeFlag("playing_blackjack");
            GameState.removeFlag("cannot_double_down");
            return new InteractionResult(
                    new Dialogue.System("Unfortunate, you've busted, Player: " +
                            handToString(playerHand) + " Dealer: " + handToString(dealerHand)),
                    InteractionTransition.NONE,
                    null
            );
        } else {
            return new InteractionResult(
                    new Dialogue.System("Player: " + handToString(playerHand) +
                            " Dealer is showing: " + dealerHandToString(dealerHand)),
                    InteractionTransition.NONE,
                    null
            );
        }
    }

    private InteractionResult stay() {
        return dealerPlays();
    }

    private InteractionResult dealerPlays() {
        // Dealer draws until 17 or more (stands on all 17s)
        while (handToInt(dealerHand) < 17) {
            dealerHand.add(Cards.draw());
        }

        GameState.removeFlag("playing_blackjack");
        GameState.removeFlag("cannot_double_down");

        int dealer = handToInt(dealerHand);
        int player = handToInt(playerHand);

        if (dealer > 21 || player > dealer) {
            GameState.addChips(2 * bet);
            return new InteractionResult(
                    new Dialogue.System("Well done, you win! Player: " + handToString(playerHand)
                            + " Dealer: " + handToString(dealerHand)),
                    InteractionTransition.NONE,
                    null
            );
        } else if (player == dealer) {
            GameState.addChips(bet);
            return new InteractionResult(
                    new Dialogue.System("Push, you get your bet back. Dealer: " + handToString(dealerHand)
                            + " Player: " + handToString(playerHand)),
                    InteractionTransition.NONE,
                    null
            );
        } else {
            return new InteractionResult(
                    new Dialogue.System("Unfortunate, dealer wins. Dealer: " + handToString(dealerHand)
                            + " Player: " + handToString(playerHand)),
                    InteractionTransition.NONE,
                    null
            );
        }
    }

    private InteractionResult doubleDown() {
        GameState.removeChips(bet);
        bet *= 2;
        playerHand.add(Cards.draw());

        if (checkHand(playerHand).equals("bust")) {
            GameState.removeFlag("playing_blackjack");
            GameState.removeFlag("cannot_double_down");
            return new InteractionResult(
                    new Dialogue.System("Unfortunate, you've busted, Player: " +
                            handToString(playerHand) + " Dealer: " + handToString(dealerHand)),
                    InteractionTransition.NONE,
                    null
            );
        }
        return dealerPlays();   // one card, then you stay
    }

    private InteractionResult endConversation() {
        return new InteractionResult(
                new Dialogue.System("You decide to stop playing blackjack"),
                InteractionTransition.POP,
                null
        );
    }


    // Checks the value of the hand
    // Returns "bust" on bust
    // Returns "blackjack" if hand value is 21 and hand size is 2
    // Returns "win" on 21
    // Returns "okay" on less than 21
    // Returns "bust" over 21, "blackjack" on 21 with 2 cards, "win" on 21, "okay" otherwise
    private String checkHand(ArrayList<Cards> hand) {
        int sum = handToInt(hand);

        if (sum > 21) {                          // was "< 21"
            return "bust";
        } else if (sum == 21 && hand.size() == 2) {
            return "blackjack";
        } else if (sum == 21) {
            return "win";
        } else {
            return "okay";
        }
    }

    // Aces count as 11, dropping to 1 as needed to avoid busting
    private int handToInt(ArrayList<Cards> hand) {
        int sum = 0;
        int aces = 0;
        for (Cards card : hand) {
            sum += card.blackjackValue();
            if (card.rank() == Cards.Rank.ACE) {
                aces++;
            }
        }
        while (sum > 21 && aces > 0) {
            sum -= 10;
            aces--;
        }
        return sum;
    }

    private String handToString(ArrayList<Cards> hand) {
        StringBuilder str = new StringBuilder("Hand: ");
        for (Cards card : hand) {
            str.append(card.getName()).append(", ");
        }
        str.append("Hand Value: ").append(handToInt(hand));
        return str.toString();
    }

    private String dealerHandToString(ArrayList<Cards> hand) {
        return "Hand: " + hand.get(0).getName();   // was printing the raw record
    }
}
