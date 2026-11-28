package edu.miamioh.cse201.oceans14.rooms;

import edu.miamioh.cse201.oceans14.engine.*;
import java.util.ArrayList;
import java.util.List;

public class PanopticonRoom extends Room {

    public PanopticonRoom() {
        super("panopticon_room", "Panopticon");
    }

    /**
     * Returns the room description, this is the base text that displays
     * what a player sees when entering a room.
     * @return String representing the room description text.
     */
    @Override
    public String getDescription() {
        return "A dimly lit surveillance room with camera feeds covering nearly every wall. Several stern-looking individuals in suits monitor the casino, watching everything from the vault doors to the last card dealt at the blackjack tables.";
    }

    /**
     * Gives the list of available actions in a record containing the ID for the action
     * and the Label which is the display friendly name.
     * @return the list of ActionOption that the player can choose from.
     */
    @Override
    public List<ActionOption> getAvailableActions() {
        List<ActionOption> actions = new ArrayList<>();

        actions.add(new ActionOption("enter_casino_room", "Return to the Casino Floor"));
        actions.add(new ActionOption("enter_big_boss_room", "Enter the Big Boss's Office"));
        actions.add(new ActionOption("use_security_cameras", "Use the Security Cameras"));
        actions.add(new ActionOption("inspect_mug", "Inspect the Coffee Mug"));
        actions.add(new ActionOption("read_poster", "Read the Security Posters"));
        actions.add(new ActionOption("note_from_it", "Read the Note from IT"));

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
        if (!this.hasValidAction(actionID)) {
            return new InteractionResult(
                    new Dialogue.Error("No Such Action Exists"),
                    InteractionTransition.NONE,
                    null
            );
        }

        return switch (actionID) {
            case "enter_casino_room" -> enterCasinoRoom();
            case "enter_big_boss_room" -> enterBigBossRoom();
            case "use_security_cameras" -> useSecurityCameras();
            case "inspect_mug" -> inspectMug();
            case "read_poster" -> readPoster();
            case "note_from_it" -> noteFromIT();
            // Detect action IDs that are available but lack an implementation.
            default -> throw new IllegalStateException(
                    "A valid action does not exist in the performAction for " + this.getID() +
                            "\n Please make sure the list of available actions all have an action function."
            );
        };
    }

    /**
     * Allows the player to leave the Panopticon and return
     * to the main casino floor.
     *
     * @return a CHANGE_ROOM result targeting the casino room.
     */
    private InteractionResult enterCasinoRoom() {
        return new InteractionResult(
                new Dialogue.System("You slip through the secure door and return to the casino floor."),
                InteractionTransition.CHANGE_ROOM,
                GameWorld.getRoom("casino_room")
        );
    }

    /**
     * Allows the player to leave the Panopticon and enter
     * the Big Boss's office.
     *
     * @return a CHANGE_ROOM result targeting the Big Boss's office.
     */
    private InteractionResult enterBigBossRoom() {
        return new InteractionResult(
                new Dialogue.System("You approach a large, dark oak door at the far end of the room. After a moment of hesitation, you turn the handle and step into the Big Boss's office."),
                InteractionTransition.CHANGE_ROOM,
                GameWorld.getRoom("big_boss_room")
        );
    }

    /**
     * Allows the player to interact with the casino's security cameras.
     * Pushes the camera system actor onto the interaction stack.
     *
     * @return a PUSH result beginning the camera system interaction.
     */
    private InteractionResult useSecurityCameras() {
        return new InteractionResult(
                new Dialogue.System("You approach an unattended surveillance station and begin cycling through the casino's security cameras."),
                InteractionTransition.PUSH,
                GameWorld.getActor("camera_system")
        );
    }

    /**
     * Allows the player to inspect a coffee mug left in the Panopticon.
     *
     * @return a NONE result containing the coffee mug description.
     */
    private InteractionResult inspectMug() {
        return new InteractionResult(
                new Dialogue.System("Sitting beside one of the monitors is a mug of coffee that has long since gone cold. Printed on the side are the words \"My #3 Daddy!\" Considering he works casino security, third place seems generous."),
                InteractionTransition.NONE,
                null
        );
    }

    /**
     * Allows the player to read the security posters in the Panopticon.
     *
     * @return a NONE result containing the security poster descriptions.
     */
    private InteractionResult readPoster() {
        return new InteractionResult(
                new Dialogue.System("The walls are covered with posters offering security advice: Passwords should be longer than 20 characters, use multifactor authentication on all accounts, and don't share company passwords. Blah, blah, blah."),
                InteractionTransition.NONE,
                null
        );
    }

    /**
     * Allows the player to read a note left by the casino's IT department.
     *
     * @return a NONE result containing the IT department's warning.
     */
    private InteractionResult noteFromIT() {
        return new InteractionResult(
                new Dialogue.System("An email printed and taped to a monitor reads: \"FINAL REMINDER: Our entire surveillance system runs in RAM. If the power goes out, we lose all camera footage and security backups. Please, for the love of God, tell the contractors NOT to shut off the electricity without notifying IT first. I am not explaining this to management again.\""),
                InteractionTransition.NONE,
                null
        );
    }


}
