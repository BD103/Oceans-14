package edu.miamioh.cse201.oceans14.actors;

import edu.miamioh.cse201.oceans14.engine.*;

import java.util.ArrayList;
import java.util.List;

public class CrazyGambler extends Actor {

    public CrazyGambler() {
        super("crazy_gambler", "Crazy Gambler");
    }

    /**
     * Returns the description of the actor displayed to the player.
     *
     * @return String representing the actor description text.
     */
    @Override
    public String getDescription() {
        return "A grey haired old man with knocked out teeth and a craze in his eyes. He is petting his pet rat.";
    }

    @Override
    public List<ActionOption> getAvailableActions() {
        List<ActionOption> actions = new ArrayList<>();



        actions.add(new ActionOption("ask_about_rat", "Ask about his rat."));

        // Only give the rat poison if the player doesn't already have it.
        if (!GameState.hasItem("rat_poison") && !GameState.flagIsActive("has_taken_rat_poison")) {
            actions.add(new ActionOption("ask_for_advice", "Ask the old man for his wisdom"));
        }

        // Three part interaction,
        // 1. The player can ask his advice, he will tell them something and say
        //      they should return with a high rollers card
        // 2. Then if they have the high rollers card he will give them his lucky rock
        // 3. After giving the rock default back to a generic go gettem message.
        if (!GameState.hasItem("high_rollers_card")) {
            actions.add(new ActionOption("ask_his_story", "Ask the old man his story"));
        } else if (GameState.hasItem("high_rollers_card") && !GameState.hasItem("lucky_rock")) {
            actions.add(new ActionOption("flash_your_card", "Show him your high rollers card."));
        } else {
            actions.add(new ActionOption("ask_question", "Ask the old man for advice"));
        }



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

        return switch (actionID) {
            case "ask_his_story" -> askHisStory();
            case "flash_your_card" -> flashYourCard();
            case "ask_about_rat" -> askAboutRat();
            case "ask_for_advice" -> askForAdvice();
            case "ask_question" -> askQuestion();

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
     * Allows the player to ask the Crazy Gambler about his past.
     * Explains how the casino's management ended his gambling career
     * and encourages the player to return with a high rollers card.
     *
     * @return a NONE result containing the Crazy Gambler's story.
     */
    private InteractionResult askHisStory() {
        return new InteractionResult(
                new Dialogue.Actor("Crazy Gambler", "Kid, I was the best, but the higher ups, the big bosses were scared and struck me down like Icarus. Maybe one day you can take over the legacy. Come talk to me when you are a high roller."),
                InteractionTransition.NONE,
                null
        );
    }

    /**
     * Gives the player the Crazy Gambler's lucky rock after
     * they show him their high rollers card.
     *
     * @return a NONE result containing the gambler's response.
     */
    private InteractionResult flashYourCard() {
        GameState.addItem("lucky_rock");
        return new InteractionResult(
                new Dialogue.Actor("Crazy Gambler", "So, kid, ya made it! You got this. Go take down those rich assholes. Here, it's my lucky rock. Always served me well."),
                InteractionTransition.NONE,
                null
        );
    }

    /**
     * Allows the player to ask the Crazy Gambler about his pet rat.
     *
     * @return a NONE result containing the gambler's response.
     */
    private InteractionResult askAboutRat() {
        return new InteractionResult(
                new Dialogue.Actor("Crazy Gambler", "Oh this, this is my baby Remy, he is such a sweet little boy. He is the only one to recognize the shit this city has come to."),
                InteractionTransition.NONE,
                null
        );
    }

    /**
     * Allows the player to ask the Crazy Gambler for advice.
     * Gives the player rat poison that can be used during
     * the Big Boss interaction.
     *
     * @return a NONE result containing the gambler's advice.
     */
    private InteractionResult askForAdvice() {
        GameState.addItem("rat_poison");
        GameState.addFlag("has_taken_rat_poison");
        return new InteractionResult(
                new Dialogue.Actor("Crazy Gambler", "I ain't much for advice, kid, but all I know is that assholes like the Big Boss need to be taken down a peg. They tried to poison my baby Remy! Lucky for you, I kept the bottle they left behind. Here, take it. You should give the Big Boss some poison and see how he likes it."),
                InteractionTransition.NONE,
                null
        );
    }

    /**
     * Provides additional dialogue after the player has
     * obtained the Crazy Gambler's lucky rock.
     *
     * @return a NONE result containing the gambler's encouragement.
     */
    private InteractionResult askQuestion() {
        return new InteractionResult(
                new Dialogue.Actor("Crazy Gambler", "What do you need me for now, kid? Get out there and take their money!"),
                InteractionTransition.NONE,
                null
        );
    }

    /**
     * Ends the conversation
     *
     * @return a POP result returning to the previous interaction
     */
    private InteractionResult endConversation() {
        // TODO update end convo text
        return new InteractionResult(
                new Dialogue.System("The guy goes back to stroking his pet rat."),
                InteractionTransition.POP,
                null
        );
    }


}
