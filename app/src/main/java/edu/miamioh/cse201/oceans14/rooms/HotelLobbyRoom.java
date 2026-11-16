package edu.miamioh.cse201.oceans14.rooms;

import edu.miamioh.cse201.oceans14.engine.*;


import java.util.ArrayList;
import java.util.List;

public class HotelLobbyRoom extends Room {

    public HotelLobbyRoom() {
        super("hotel_lobby_room", "The Hotel Lobby");
    }

    /**
     * Returns the room description, this is the base text that displays
     * what a player sees when entering a room.
     * @return String representing the room description text.
     */
    @Override
    public String getDescription() {
        // TODO Replace the description Text
        return "The hotel lobby";
    }

    /**
     * Gives the list of available actions in a record containing the ID for the action
     * and the Label which is the display friendly name.
     * @return the list of ActionOption that the player can choose from.
     */
    @Override
    public List<ActionOption> getAvailableActions() {
        List<ActionOption> actions = new ArrayList<>();

        // Navigation
        actions.add(new ActionOption("enter_casino_room", "Return to the Casino Floor"));

        // Hotel interactions
        actions.add(new ActionOption("grab_food_from_buffet", "Grab Breakfast"));
        actions.add(new ActionOption("read_newspaper", "Read the Newspaper"));

        if (!GameState.flagIsActive("pickpocketed_hotel_guest")) {
            actions.add(new ActionOption("pickpocket_customer", "Pickpocket a Customer"));
        }

        // Actors
        actions.add(new ActionOption("talk_to_hotel_staff", "Talk to Hotel Staff"));
        actions.add(new ActionOption("talk_to_elevator", "Use the Elevator"));

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
            case "enter_casino_room" -> enterCasinoRoom();
            case "grab_food_from_buffet" -> grabFoodFromBuffet();
            case "pickpocket_customer" -> pickpocketCustomer();
            case "read_newspaper" -> readNewspaper();
            case "talk_to_hotel_staff" -> talkToHotelStaff();
            case "talk_to_elevator" -> talkToElevator();
            // Detect action IDs that are available but lack an implementation.
            default -> throw new IllegalStateException(
                    "A valid action does not exist in the performAction for " + this.getID() +
                            "\n Please make sure the list of available actions all have an action function."
            );
        };
    }

    /**
     * Allows the player to grab breakfast from the hotel buffet.
     * This interaction does not modify the game state.
     * This is just for fun
     *
     * @return an InteractionResult describing the breakfast with no transition.
     */
    private InteractionResult grabFoodFromBuffet() {
        return new InteractionResult(
                new Dialogue.System("You walk up to the continental breakfast and grab a mediocre breakfast sandwich. Can't rob on an empty stomach."),
                InteractionTransition.NONE,
                null
        );
    }

    /**
     * Allows the player to pickpocket a hotel guest for chips.
     * Adds 500 chips to the player's balance and activates the
     * pickpocketed_hotel_guest flag to prevent repeated theft.
     *
     * @return an InteractionResult describing the theft with no transition.
     */
    private InteractionResult pickpocketCustomer() {
        // TODO change to be reflective of the economy we use
        GameState.addChips(500);
        GameState.addFlag("pickpocketed_hotel_guest");
        return new InteractionResult(
            new Dialogue.System("You walk up to a sharply dressed older man and compliment his hat, all while you pilfer some chips from his back pocket."),
            InteractionTransition.NONE,
            null
        );
    }

    /**
     * Allows the player to read a newspaper in the hotel lobby.
     * Provides information about the casino's security system
     * without modifying the game state.
     *
     * @return an InteractionResult containing the newspaper text with no transition.
     */
    private InteractionResult readNewspaper() {
        return new InteractionResult(
                new Dialogue.System("You open up the newspaper and on the front page is an article about the casino. \"New State of the Art Security System, claims of impenetrability from casino owners.\" How ironic"),
                InteractionTransition.NONE,
                null
        );
    }

    /**
     * Allows the player to leave the hotel lobby and return to the casino.
     * Changes the current room to casino_room.
     *
     * @return an InteractionResult that transitions to the casino room.
     */
    private InteractionResult enterCasinoRoom() {
        return new InteractionResult(
                new Dialogue.System("You leave the Hotel Lobby to return to the main casino room."),
                InteractionTransition.CHANGE_ROOM,
                GameWorld.getRoom("casino_room")
        );
    }

    /**
     * Initiates an interaction with the hotel front desk staff.
     * Pushes the hotel_staff actor onto the interaction stack.
     *
     * @return an InteractionResult that begins the hotel staff interaction.
     */
    private InteractionResult talkToHotelStaff() {
        return new InteractionResult(
                new Dialogue.System("You walk up to the two ladies at the front desk."),
                InteractionTransition.PUSH,
                GameWorld.getActor("hotel_staff")
        );
    }

    /**
     * Initiates an interaction with the hotel elevator.
     * Pushes the elevator actor onto the interaction stack, allowing
     * the player to select floors and interact with the elevator.
     *
     * @return an InteractionResult that begins the elevator interaction.
     */
    private InteractionResult talkToElevator() {
        return new InteractionResult(
                new Dialogue.System("You walk up to the old elevator"),
                InteractionTransition.PUSH,
                GameWorld.getActor("elevator")
        );
    }

}
