package edu.miamioh.cse201.oceans14.actors;

import edu.miamioh.cse201.oceans14.engine.*;

import java.util.ArrayList;
import java.util.List;

public class HotelStaff extends Actor {

    public HotelStaff() {
        super("hotel_staff", "Hotel Staff");
    }

    /**
     * Returns the description of the actor displayed to the player.
     *
     * @return String representing the actor description text.
     */
    @Override
    public String getDescription() {
        return "Two ladies behind the hotel front desk, whispering about the latest casino gossip.";
    }

    @Override
    public List<ActionOption> getAvailableActions() {
        List<ActionOption> actions = new ArrayList<>();

        actions.add(new ActionOption("ask_about_elevator", "Ask About the Elevator"));
        actions.add(new ActionOption("ask_about_contractors", "Ask About the Contractors"));
        actions.add(new ActionOption("ask_about_casino_staff", "Ask About Casino Staff"));
        actions.add(new ActionOption("ask_about_security", "Ask About Casino Security"));

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
            case "ask_about_elevator" -> askAboutElevator();
            case "ask_about_contractors" -> askAboutContractors();
            case "ask_about_casino_staff" -> askAboutCasinoStaff();
            case "ask_about_security" -> askAboutSecurity();

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
     * Provides a hint about the hotel's unreliable elevator.
     * Suggests that the elevator has problems when traveling
     * to the thirteenth floor.
     *
     * @return a NONE result containing the elevator gossip.
     */
    private InteractionResult askAboutElevator() {
        return new InteractionResult(
                new Dialogue.Actor("First Receptionist", "Oh, that old thing? Yeah, I wouldn't trust it. I prefer the stairs. It especially makes a weird noise when you request the 13th floor. Management insists it's perfectly safe."),
                InteractionTransition.NONE,
                null
        );
    }

    /**
     * Provides information about the contractors working around
     * the casino and their responsibility for electrical repairs.
     *
     * @return a NONE result containing the contractor gossip.
     */
    private InteractionResult askAboutContractors() {
        return new InteractionResult(
                new Dialogue.Actor("Second Receptionist", "You know those contractors all around the casino? They are here to handle all sorts of maintenance problems, including the electricity. Turns out the system is so out of date that they have to shut off half the power in order to make repairs."),
                InteractionTransition.NONE,
                null
        );
    }

    /**
     * Provides a hint about the casino's careless employees
     * and the availability of staff uniforms.
     *
     * @return a NONE result containing the staff gossip.
     */
    private InteractionResult askAboutCasinoStaff() {
        return new InteractionResult(
                new Dialogue.Actor("First Receptionist", "Everyone around here is super careless! Uniforms are lying about everywhere in the staff room. Half the time, I can't even tell who is supposed to be working since their name tags fall off all the time. The kitchen staff especially."),
                InteractionTransition.NONE,
                null
        );
    }

    /**
     * Provides a hint about casino security and a guard
     * who frequently drinks at the bar.
     *
     * @return a NONE result containing the security gossip.
     */
    private InteractionResult askAboutSecurity() {
        return new InteractionResult(
                new Dialogue.Actor("Second Receptionist", "That new article is talking all about how the new security is state-of-the-art and impenetrable, all while one of the guards is getting wasted at the bar. Rumor has it his wife is ruthless about child support. The guy is really loose-lipped after a few drinks."),
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
        return new InteractionResult(
                new Dialogue.System("You thank the ladies for the stories and head off."),
                InteractionTransition.POP,
                null
        );
    }


}
