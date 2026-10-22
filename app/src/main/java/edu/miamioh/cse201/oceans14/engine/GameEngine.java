package edu.miamioh.cse201.oceans14.engine;

import java.util.List;
import java.util.Stack;


/**
 * The master function of the backend. This is the class that handles the interaction between
 * the front and back end.
 */
public class GameEngine {
    // The current GameState including inventory, flags, and currentRoom
    // TODO delete me
    private final GameState gameState;
    // The interaction stack which tracks what interactable is currently in control
    // along with tracking what interactable to return to after the current one is finished.
    private final Stack<Interactable> interactionStack;

    /**
     *  The constructor for the GameEngine. This is what will be used to initialize the backend.
     * @param gameState the starting GameState. Good if we want to load a specific GameState.
     * @param startingRoom The room to start in, this is so GameEngine can initialize the InteractionStack.
     */
    public GameEngine(GameState gameState, Interactable startingRoom) {
        this.gameState = gameState;
        this.interactionStack = new Stack<>();

        // Add the starting room to the interaction stack
        this.interactionStack.push(startingRoom);
    }

    /**
     * Function to get the current interactable. This can be used to check what room we
     * are in or who we are talking to.
     * @return The Interactable at the top of the InteractionStack
     */
    public Interactable getCurrentInteractable() {
        return interactionStack.peek();
    }

    /**
     * Gets the currently available actions of the controlling interactable.
     * This is a primary interface point for the frontend to get the current actions.
     * @return A list of the currently available actions.
     */
    public List<ActionOption> getAvailableActions() {
        return getCurrentInteractable().getAvailableActions();
    }

    /**
     * Gets the description of the current controlling interactable.
     * This is the primary display information such as room description, actor dialogue
     * etc.
     * @return String representing the description text.
     */
    public String getCurrentDescription() {
        return getCurrentInteractable().getDescription();
    }

    /**
     * Tells the controllingInteractable to run a specific action.
     * @param actionToPerform String representing the actionID
     * @return InteractionResult which provides description as to the result as well as what changes on the interaction stack
     *         On an invalid action (non-existent, or one without the requirements met) returns an
     *         InteractionResult containing the message "Invalid Action"
     */
    public InteractionResult performAction(String actionToPerform) {
        Interactable controllingInteractable = getCurrentInteractable();

        // Validate that the desired action actually exists and is valid to run
        if (!(controllingInteractable.hasValidAction(actionToPerform))) {
            // If the action is none-existent or contains unmet requirements
            // returns an InteractionResult with the message "Invalid Action"
            return new InteractionResult("Invalid Action", InteractionTransition.NONE, null);
        }

        // If the action is valid, tell the interactable to run.
        InteractionResult result = controllingInteractable.performAction(actionToPerform);

        // Store the transition requested by the action.
        // This is what informs us if we need to modify the interactionStack.
        InteractionTransition transition = result.transition();
        switch (transition) {
            // Used when a non-room interactable is leaving the interactionStack
            case POP:
                // Validate we aren't removing the root room.
                if (interactionStack.size() <= 1) {
                    throw new IllegalStateException("Cannot pop the root room.");
                }
                interactionStack.pop();
                break;
            // Used when the current controlling interactable wants to push another
            // interactable onto the top of the interactionStack.
            case PUSH: // TODO validate the push target exists
                // Verify the target is not null
                if (result.target() == null) {
                    throw new IllegalArgumentException("PUSH requires a target");
                }
                interactionStack.push(result.target());
                break;
            // Used when a change of the root room is requested.
            // This is called when the player is shifting what room they are in.
            case CHANGE_ROOM: // TODO validate the room exists
                interactionStack.clear();
                interactionStack.push(result.target());
                GameState.setCurrentRoomID(result.target().getID());
                break;
            // Used when no change in the controlling interactable is in order.
            case NONE:
                break;
        }

        // return the InteractionResult
        return result;
    }
}
