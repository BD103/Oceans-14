package edu.miamioh.cse201.oceans14.items;

/**
 * Defines and item within the gameworld
 *
 * @param id the unique id of the item
 * @param name the display friendly name of the item
 * @param description a short description to show the player
 */
public record Item(
        String id,
        String name,
        String description
) {}
