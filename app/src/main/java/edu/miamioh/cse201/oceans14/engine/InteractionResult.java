package edu.miamioh.cse201.oceans14.engine;

/**
 * Record for containing the resulting information from performing an action.
 *
 * @param dialogue The dialogue message that will be displayed to the player.
 * @param transition what action will be requested on the stack
 * @param target the target interactable to change focus too, null if N/A
 */
public record InteractionResult(Dialogue dialogue, InteractionTransition transition, Interactable target) {}
