package edu.miamioh.cse201.oceans14.engine;

import java.util.List;
import java.util.Stack;


/**
 * The master function of the backend. This is the class that handles the interaction between
 * the front and back end.
 */
public class GameEngine {
    // The current GameState including inventory, flags, and currentRoom
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
        this.interactionStack = new Stack<Interactable>();

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
        return getCurrentInteractable().getAvailableActions(gameState);
    }

    /**
     * Gets the description of the current controlling interactable.
     * This is the primary display information such as room description, actor dialogue
     * etc.
     * @return String representing the description text.
     */
    public String getCurrentDescription() {
        return getCurrentInteractable().getDescription(gameState);
    }
}
