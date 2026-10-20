package edu.miamioh.cse201.oceans14.engine;

import java.util.List;


/**
 * Defines the common interaction behavior for game objects
 * Implemented by Room and Actor, allowing GameEngine
 * to interact with them through a consistent interface.
 */
public interface Interactable {
    /**
     * Returns a description of the current interaction
     * @param state the current state of the game.
     * @return A string representing the description of the current action.
     */

    String getDescription(GameState state);

    /**
     * Returns the current list of available actions.
     * ActionOption are just a Java Record which holds an actions id and its
     * Description.
     * Ex. if option = ActionOption("listen_to_gossip", "Listen to the secretaries gossip");
     *      option.id() -> "listen_to_gossip"
     *      option.label() -> "Listen to the secretaries gossip"
     * @param state The current state of the game.
     * @return A list of ActionOption that represent possible actions.
     */
    List<ActionOption> getAvailableActions(GameState state);

    /**
     * Performs the given action and returns information about how to change the GameState.
     * InteractionResult is a record that contains three data points
     * 1. A message describing the outcome to the player
     * 2. An InteractionTransition which tells GameEngine how to modify the current GameState
     *      ex. POP, PUSH, NONE, CHANGE_ROOM
     * 3. InteractableTarget which tells the GameEngine what interactable to target next.
     *      Used when switching the focus on the interaction. Primarily for when a room
     *      needs to designate an Actor as the current top of the stack, or when changing rooms.
     * @param actionID The string ID of the interaction to run
     * @param state The current state of the game.
     * @return InteractionResult which is a record containing the results of the action and how
     *          to modify the GameState.
     */
    InteractionResult performAction(String actionID, GameState state);
}
