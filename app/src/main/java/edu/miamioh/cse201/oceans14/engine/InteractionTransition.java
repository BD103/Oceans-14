package edu.miamioh.cse201.oceans14.engine;

/**
 * Describes how the GameEngine should update
 * the current GameState after an interaction.
 */
public enum InteractionTransition {
    NONE, // Stay in the current interaction
    PUSH, // Push another interactable onto the stack
    POP, // Remove the current interactable from the stack
    CHANGE_ROOM // Move the player to another room and clear the stack
}
