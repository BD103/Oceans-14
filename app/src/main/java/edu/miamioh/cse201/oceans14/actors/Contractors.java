package edu.miamioh.cse201.oceans14.actors;

import edu.miamioh.cse201.oceans14.engine.*;

import java.util.ArrayList;
import java.util.List;

public class Contractors extends Actor {

    private static final int VENT_MAP_PRICE = 250;

    public Contractors() {
        super("contractors", "Contractors");
    }

    /**
     * Returns the description of the actor displayed to the player.
     *
     * @return String representing the actor description text.
     */
    @Override
    public String getDescription() {
        return "A group of contractors stands around the casino floor discussing future maintenance and arguing over electrical diagrams.";
    }

    /**
     * Returns the available interactions with the contractors.
     * The vent map purchase becomes available after discovering the offer,
     * and the elevator report becomes available after the elevator breaks.
     *
     * @return a list of currently available ActionOption objects.
     */
    @Override
    public List<ActionOption> getAvailableActions() {
        List<ActionOption> actions = new ArrayList<>();

        actions.add(new ActionOption("ask_about_work", "Ask About Their Work"));

        // Prevent the player from hearing about the vent map deal after getting the vent
        if (!GameState.hasItem("vent_map")) {
            actions.add(new ActionOption("ask_about_vents", "Ask About the Ventilation System"));
        }

        // If the player has heard the offer and doesn't own the map give them an opportunity to purchase the vent map
        if (GameState.flagIsActive("contractor_map_offer") && !GameState.hasItem("vent_map")) {
            actions.add(new ActionOption("purchase_vent_map", "Make a Quiet Deal (" + VENT_MAP_PRICE + " Chips)"));
        }

        // Check that the elevator is broken before allowing a report
        if (GameState.flagIsActive("elevator_broken") && !GameState.flagIsActive("vault_security_disabled")) {
            actions.add(new ActionOption("report_broken_elevator", "Report the Broken Elevator"));
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
            case "ask_about_work" -> askAboutWork();
            case "ask_about_vents" -> askAboutVents();
            case "purchase_vent_map" -> purchaseVentMap();
            case "report_broken_elevator" -> reportBrokenElevator();

            case "end_conversation" -> endConversation();

            default -> throw new IllegalStateException(
                    "A valid action does not exist in the performAction for " + this.getID() +
                            "\n Please make sure the list of available actions all have an action function."
            );
        };
    }

    /**
     * Provides information about the contractors' maintenance responsibilities
     * and hints at the casino's outdated electrical system.
     *
     * @return a NONE result containing the contractor's dialogue.
     */
    private InteractionResult askAboutWork() {
        return new InteractionResult(
                new Dialogue.Actor("Contractor", "We're responsible for keeping this whole place running. The entire building is twice as old as I am and the grid is showing its age. We have to shut down half the power just to fix something like that stupid elevator."),
                InteractionTransition.NONE,
                null
        );
    }

    /**
     * Allows the player to ask about the casino's ventilation system.
     * One contractor discreetly offers to sell a map of the vents.
     * Activates the contractor_map_offer flag.
     *
     * @return a NONE result revealing the contractor's offer.
     */
    private InteractionResult askAboutVents() {
        GameState.addFlag("contractor_map_offer");

        return new InteractionResult(
                new Dialogue.Actor("Contractor", "The ventilation system? What do you wanna know about that for? Actually, never mind. Look, between you and me, I might have made an extra copy of the ventilation schematics. Shows all the vents, even the security lasers. Management would have my head if they found out. Slip me " + VENT_MAP_PRICE + " chips, and maybe that copy goes missing. Just keep your voice down, alright?"),
                InteractionTransition.NONE,
                null
        );
    }

    /**
     * Allows the player to secretly purchase a map of the casino's vents.
     * If the player has enough chips, deducts the purchase price and
     * adds the map to their inventory.
     *
     * @return a NONE result describing the outcome of the transaction.
     */
    private InteractionResult purchaseVentMap() {
        if (GameState.removeChips(VENT_MAP_PRICE)) {
            GameState.addItem("vent_map");

            return new InteractionResult(
                    new Dialogue.Actor("Contractor", "Pleasure doing business with you. Now, keep that thing hidden, alright? Officially, this map doesn't exist, and we never had this conversation. If the boss asks, I was on my lunch break."),
                    InteractionTransition.NONE,
                    null
            );
        } else {
            return new InteractionResult(
                    new Dialogue.Actor("Contractor", "Hey, I said " + VENT_MAP_PRICE + " chips. I'm not risking my job for pocket change. Come back when you've got the money, and don't go telling the other guys what we talked about."),
                    InteractionTransition.NONE,
                    null
            );
        }
    }

    /**
     * Allows the player to report the broken hotel elevator to the contractors.
     * Activates the vault_security_disabled flag when the contractors
     * shut down power to repair the elevator.
     *
     * @return a NONE result describing the electrical shutdown.
     */
    private InteractionResult reportBrokenElevator() {
        GameState.addFlag("vault_security_disabled");
        return new InteractionResult(
                new Dialogue.System("You tell the contractors the elevator is broken again. After some arguing, a few of them go to investigate. A moment later, the lights flicker. It looks like they've cut power to some of the security systems while they make repairs."),
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
                new Dialogue.System("You leave the contractors to their work and head back toward the main casino floor."),
                InteractionTransition.POP,
                null
        );
    }


}
