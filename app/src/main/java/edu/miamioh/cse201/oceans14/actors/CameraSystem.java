package edu.miamioh.cse201.oceans14.actors;

import edu.miamioh.cse201.oceans14.engine.*;
import java.util.List;

public class CameraSystem extends Actor {

    public CameraSystem() {
        super("camera_system", "Camera System");
    }

    /**
     * Returns the description of the actor displayed to the player.
     *
     * @return String representing the actor description text.
     */
    @Override
    public String getDescription() {
        return "The corporate security system with a scattering of camera feeds across the casino.";
    }

    @Override
    public List<ActionOption> getAvailableActions() {
        return List.of(
                new ActionOption("view_cameras", "View Security Cameras"),
                new ActionOption("end_conversation", "Exit Camera System")
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
        if (!this.hasValidAction(actionID)) {
            return new InteractionResult(
                    new Dialogue.Error("No Such Action Exists"),
                    InteractionTransition.NONE,
                    null
            );
        }

        return switch (actionID) {
            case "view_cameras" -> viewCameras();

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
     * Allows the player to view the casino's surveillance cameras.
     *
     * @return a NONE result containing the camera feed description.
     */
    private InteractionResult viewCameras() {
        return new InteractionResult(
                new Dialogue.System("You cycle through the surveillance feeds, watching various rooms and hallways throughout the casino."),
                InteractionTransition.NONE,
                null
        );
    }

    /**
     * Closes the camera interface and returns the player to the Panopticon.
     *
     * @return a POP result returning to the previous interaction
     */
    private InteractionResult endConversation() {
        return new InteractionResult(
                new Dialogue.System("You exit the camera system and step away from the surveillance terminal."),
                InteractionTransition.POP,
                null
        );
    }


}
