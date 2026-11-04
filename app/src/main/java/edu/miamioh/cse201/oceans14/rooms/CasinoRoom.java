package edu.miamioh.cse201.oceans14.rooms;

import edu.miamioh.cse201.oceans14.engine.*;


import java.util.List;

/**
 * Represents the main casino floor.
 *
 * Provides access to surrounding rooms, NPC interactions,
 * and gambling activities. Certain room transitions require
 * items or disguises before access is granted.
 */
public class CasinoRoom extends Room {

    public CasinoRoom() {
        super("casino_room", "Casino Floor");
    }

    /**
     * Returns the description displayed when the player enters the casino floor.
     *
     * @return the casino floor's description
     */
    @Override
    public String getDescription() {
        // TODO UPDATE DESCRIPTION
        return "You enter the vast hall of greed, the Casino Floor";
    }

    /**
     * Gives the list of available actions in a record containing the ID for the action
     * and the Label which is the display friendly name.
     * @return the list of ActionOption that the player can choose from.
     */
    @Override
    public List<ActionOption> getAvailableActions() {
        return List.of(
                new ActionOption("exit_casino", "Leave the Casino"),
                new ActionOption("enter_staff_room", "Enter Staff Room"),
                new ActionOption("enter_bar_room", "Enter Hotel Bar"),
                new ActionOption("enter_high_rollers_room", "Enter High Rollers Room"),
                new ActionOption("enter_cash_out_room", "Enter Cash Out Room"),
                new ActionOption("enter_panopticon_room", "Enter Panopticon"),
                new ActionOption("enter_maintenance_room", "Enter Maintenance Room"),
                new ActionOption("enter_hotel_lobby_room", "Enter Hotel Lobby"),
                new ActionOption("talk_to_contractors", "Talk to Contractors"),
                new ActionOption("start_gambling", "Start Gambling")
        );
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
        // Ex. enter_casino -> enterCasino()
        return switch (actionID) {
            case "exit_casino" -> exitCasino();
            case "enter_staff_room" -> enterStaffRoom();
            case "enter_bar_room" -> enterBarRoom();
            case "enter_high_rollers_room" -> enterHighRollersRoom();
            case "enter_cash_out_room" -> enterCashOutRoom();
            case "enter_panopticon_room" -> enterPanopticonRoom();
            case "enter_maintenance_room" -> enterMaintenanceRoom();
            case "enter_hotel_lobby_room" -> enterHotelLobbyRoom();
            case "talk_to_contractors" -> talkToContractors();
            case "start_gambling" -> startGambling(); // TODO replace me
            // Detect action IDs that are available but lack an implementation.
            default -> throw new IllegalStateException(
                    "A valid action does not exist in the performAction for " + this.getID() +
                            "\n Please make sure the list of available actions all have an action function."
            );
        };
    }

    /**
     * Returns the player to the casino entrance.
     * @return CHANGE_ROOM to entrance room
     */
    private InteractionResult exitCasino() {
        return new InteractionResult(
                new Dialogue.System("You leave the casino and enter the street"),
                InteractionTransition.CHANGE_ROOM,
                GameWorld.getRoom("entrance_room")
        );
    }
    /**
     * Attempts to enter the staff room.
     *
     * Requires the player to possess a staff room keycard.
     * Without the keycard, access is denied.
     *
     * @return a CHANGE_ROOM result if the player has the keycard,
     *         or a NONE result if access is denied
     */
    private InteractionResult enterStaffRoom() {
        if (GameState.hasItem("staff_room_keycard")) {
            return new InteractionResult(
                    new Dialogue.System("You swipe in using your stolen keycard"),
                    InteractionTransition.CHANGE_ROOM,
                    GameWorld.getRoom("staff_room")
            );
        } else {
            return new InteractionResult(
                    new Dialogue.System("You walk up to the staff door, but you don't have a keycard"),
                    InteractionTransition.NONE,
                    null
            );
        }
    }

    /**
     * Moves the player from the casino floor to the hotel bar.
     * @return CHANGE_ROOM to the bar
     */
    private InteractionResult enterBarRoom() {
        return new InteractionResult(
                new Dialogue.System("You walk into the hotel bar"),
                InteractionTransition.CHANGE_ROOM,
                GameWorld.getRoom("bar_room")
        );
    }

    /**
     * Attempts to enter the high rollers room.
     * Entry requires a high rollers card. Players without the card
     * are denied access by the bouncer.
     *
     * @return a CHANGE_ROOM result if the player has the card,
     *         or a NONE result if access is denied
     */
    private InteractionResult enterHighRollersRoom() {
        if (GameState.hasItem("high_rollers_card")) {
            return new InteractionResult(
                    new Dialogue.System("You enter the high rollers room"),
                    InteractionTransition.CHANGE_ROOM,
                    GameWorld.getRoom("high_rollers_room")
            );
        } else {
            return new InteractionResult(
                     new Dialogue.System("The bouncer denies you access, you don't have sufficient status"),
                    InteractionTransition.NONE,
                    null
            );
        }
    }

    /**
     * Moves the player to the cash-out room.
     * @return CHANGE_ROOM to the cash-out room
     */
    private InteractionResult enterCashOutRoom() {
        return new InteractionResult(
                new Dialogue.System("You head to the cash out room"),
                InteractionTransition.CHANGE_ROOM,
                GameWorld.getRoom("cash_out_room")
        );
    }

    /**
     * Attempts to enter the panopticon.
     *
     * Access is granted if the player meets either requirement:
     * 1. Possesses a level-two disguise.
     * 2. Possesses a level-one disguise, food for the boss,
     *    and permission from the chef.
     *
     * Players who meet neither requirement are denied entry.
     *
     * @return a CHANGE_ROOM result if access is granted,
     *         or a NONE result if access is denied
     */
    private InteractionResult enterPanopticonRoom() {
        if (GameState.hasItem("level_two_disguise")) {
            return new InteractionResult(
                    new Dialogue.System("Your fellow guard nods and lets you in"),
                    InteractionTransition.CHANGE_ROOM,
                    GameWorld.getRoom("panopticon_room")
            );
        } else if (
                GameState.hasItem("boss_food") &&
                GameState.hasItem("level_one_disguise") &&
                GameState.hasItem("chef_permission")) {
            return new InteractionResult(
                    new Dialogue.System("The guard nods you in and points you toward the bosses room"),
                    InteractionTransition.CHANGE_ROOM,
                    GameWorld.getRoom("panopticon_room")
            );
        } else {
            return new InteractionResult(
                    new Dialogue.Actor("Guard", "Hold up there buddy, you can't come in here"),
                    InteractionTransition.NONE,
                    null
            );
        }

    }

    /**
     * Attempt to enter the maintenance room
     * Entry requires the maintenance_key gained from the boss food event
     *
     * @return a CHANGE_ROOM result if access is granted,
     *         or a NONE result if access is denied
     */
    private InteractionResult enterMaintenanceRoom() {
        if (GameState.hasItem("maintenance_key")) {
            return new InteractionResult(
                    new Dialogue.System("You use the key to enter the maintenance room"),
                    InteractionTransition.CHANGE_ROOM,
                    GameWorld.getRoom("maintenance_room")
            );
        } else {
            return new InteractionResult(
                    new Dialogue.System("You attempt to turn the door key, but its locked"),
                    InteractionTransition.NONE,
                    null
            );
        }
    }

    /**
     * Moves the player from the casino floor to the hotel lobby.
     * @return CHANGE_ROOM to hotel lobby
     */
    private InteractionResult enterHotelLobbyRoom() {
        return new InteractionResult(
                new Dialogue.System("You enter the hotel lobby"),
                InteractionTransition.CHANGE_ROOM,
                GameWorld.getRoom("hotel_lobby_room")
        );
    }

    /**
     * Begins an interaction with the contractors
     * @return PUSH contractors
     */
    private InteractionResult talkToContractors() {
        return new InteractionResult(
                new Dialogue.System("You walk up to the contractors"),
                InteractionTransition.PUSH,
                GameWorld.getActor("contractors")
        );
    }
    // TODO reminder to make the gambling system.
    private InteractionResult startGambling() {
        return new InteractionResult(
                new Dialogue.Error("NOT IMPLEMENTED, JUST A PLACEHOLDER"),
                InteractionTransition.NONE,
                null
        );
    }

}
