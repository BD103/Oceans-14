package edu.miamioh.cse201.oceans14.actors;

import edu.miamioh.cse201.oceans14.engine.*;

import java.util.ArrayList;
import java.util.List;

public class Elevator extends Actor {

    public Elevator() {
        super("elevator", "Elevator");
    }

    /**
     * Returns the description of the actor displayed to the player.
     *
     * @return String representing the actor description text.
     */
    @Override
    public String getDescription() {
        // TODO replace description text
        if (GameState.flagIsActive("elevator_broken")) {
            return "The elevator is broken. The doors are stuck and command console unresponsive.";
        } else {
            return "An old elevator way past its prime. Its worn control panel has a numeric keypad. Enter a floor number to operate it.";
        }
    }



    /**
     * Elevator handles the actions uniquely, that is because it accepts any valid number
     * as an input for the floor, as such we only print out the end conversation tag as the numbers
     * Will be handled by another function
     *
     * @return the list of available actions
     */
    @Override
    public List<ActionOption> getAvailableActions() {
        List<ActionOption> actions = new ArrayList<>();

        actions.add(new ActionOption("end_conversation", "End the conversation"));

        return List.copyOf(actions);
    }

    /**
     * Determines whether the provided input is a valid elevator action.
     * Accepts the normal exit action or any integer floor number.
     *
     * @param actionID the input provided by the player.
     * @return true if the input is a valid action or integer.
     */
    @Override
    public boolean hasValidAction(String actionID) {
        if (actionID == null) {
            return false;
        }

        if (super.hasValidAction(actionID)) {
            return true;
        }

        String input = actionID.trim();

        try {
            Integer.parseInt(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
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
        if (!this.hasValidAction(actionID)) {
            return new InteractionResult(
                    new Dialogue.Error("Please enter a valid floor number."),
                    InteractionTransition.NONE,
                    null
            );
        }

        String input = actionID.trim();

        if (input.equals("end_conversation")) {
            return endConversation();
        }

        int floor = Integer.parseInt(input);
        return selectFloor(floor);
    }

    /**
     * Determines what happens when the player selects a floor.
     *
     * @param floor the floor number requested.
     * @return an InteractionResult for the selected floor.
     */
    private InteractionResult selectFloor(int floor) {
        if (GameState.flagIsActive("elevator_broken")) {
            return new InteractionResult(
                    new Dialogue.System("You press the button, but nothing happens. The elevator is completely broken."),
                    InteractionTransition.NONE,
                    null
            );
        }

        return switch (floor) {

            case 13 -> breakElevator();

            case 7 -> new InteractionResult(
                    new Dialogue.System("You press 7. The elevator lets out a cheerful ding, and the display flashes three lucky sevens. Somewhere in the distance, a slot machine hits the jackpot. Unfortunately, you don't."),
                    InteractionTransition.NONE,
                    null
            );

            case 14 -> new InteractionResult(
                    new Dialogue.System("You press 14. The elevator speakers crackle to life and announce, 'Ocean's 14, please report to the vault.' You freeze. How does it know?"),
                    InteractionTransition.NONE,
                    null
            );

            case 69 -> new InteractionResult(
                    new Dialogue.System("You press 69. The elevator pauses for an unusually long moment before displaying a single word: NICE."),
                    InteractionTransition.NONE,
                    null
            );

            case 67 -> new InteractionResult(
                    new Dialogue.System("You press 67. The elevator lets out an obnoxiously loud 'SIX SEVEN!' before displaying the words: ARE YOU FOUR YEARS OLD?"),
                    InteractionTransition.NONE,
                    null
            );

            case 42 -> new InteractionResult(
                    new Dialogue.System("You press 42. The elevator display flickers before revealing: THE ANSWER TO LIFE, THE UNIVERSE, AND EVERYTHING. Unfortunately, it still won't tell you what the question is."),
                    InteractionTransition.NONE,
                    null
            );

            case 0 -> new InteractionResult(
                    new Dialogue.System("You press 0. The elevator lets out a tired groan. You're already on the ground floor. Even the elevator seems disappointed in your decision-making."),
                    InteractionTransition.NONE,
                    null
            );

            case 404 -> new InteractionResult(
                    new Dialogue.System("You press 404. The elevator freezes for a moment before displaying: ERROR 404 — FLOOR NOT FOUND. Please contact your system administrator."),
                    InteractionTransition.NONE,
                    null
            );

            case 777 -> new InteractionResult(
                    new Dialogue.System("You press 777. The elevator erupts into flashing lights and triumphant casino music. JACKPOT! A small receipt prints from the control panel. It reads: 'Congratulations! You've won absolutely nothing.'"),
                    InteractionTransition.NONE,
                    null
            );

            case 9001 -> new InteractionResult(
                    new Dialogue.System("You press 9001. The elevator suddenly screams, 'IT'S OVER NINE THOUSAND!' The display immediately shorts out before quietly returning to normal. You decide not to question it."),
                    InteractionTransition.NONE,
                    null
            );

            case 8008 -> new InteractionResult(
                    new Dialogue.System("You press 8008. You stare at the number on the display and let out a childish giggle. The elevator responds with a deeply disappointed beep."),
                    InteractionTransition.NONE,
                    null
            );

            default -> new InteractionResult(
                    new Dialogue.System("You enter floor " + floor + ". The elevator's control panel blinks, but nothing particularly interesting happens."),
                    InteractionTransition.NONE,
                    null
            );
        };
    }

    /**
     * Breaks the elevator when the player selects floor 13.
     * Activates the elevator_broken flag, allowing the player
     * to report the malfunction to the contractors.
     *
     * @return a NONE result describing the elevator breaking.
     */
    private InteractionResult breakElevator() {
        GameState.addFlag("elevator_broken");

        return new InteractionResult(
                new Dialogue.System("You press 13. The elevator begins ascending before an awful screech echoes through the shaft, followed by a deafening crash. The control panel flickers and goes dark. It looks like you've broken the elevator."),
                InteractionTransition.NONE,
                null
        );
    }

    /**
     * Ends the conversation
     *
     * @return a POP result returning to the previous interaction
     */
    /**
     * Ends the elevator interaction and returns the player to the hotel lobby.
     *
     * @return a POP result returning to the previous interaction.
     */
    private InteractionResult endConversation() {
        return new InteractionResult(
                new Dialogue.System("You step away from the elevator and return to the hotel lobby."),
                InteractionTransition.POP,
                null
        );
    }


}
