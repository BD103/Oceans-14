package edu.miamioh.cse201.oceans14.engine;

/**
 * Record for containing the resulting information from performing an action.
 * @param message The text describing the result
 * @param transition what action will be requested on the stack
 * @param target the target interactable to change focus too, null if N/A
 */
public record InteractionResult(String message, InteractionTransition transition, Interactable target) {
}
