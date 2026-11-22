package edu.miamioh.cse201.oceans14.ui.input;

import edu.miamioh.cse201.oceans14.engine.ActionOption;
import edu.miamioh.cse201.oceans14.engine.GameEngine;

/**
 * Facilitates command auto-complete.
 */
public class Completions {
    /**
     * Searches through the {@link GameEngine#getAvailableActions()} for actions that begin with
     * the buffer string. If there is exactly one match, this returns the string necessary to
     * complete the buffer into a valid action.
     *
     * @param buffer The string to auto-complete.
     * @param engine The game engine.
     * @return The string to append to the end of the buffer to complete it, null if there is not
     *         exactly one candidate matched.
     */
    public static String complete(String buffer, GameEngine engine) {
        String match = null;

        for (ActionOption option : engine.getAvailableActions()) {
            // If one of the available actions starts with the text the player has typed, it is a
            // candidate auto-complete.
            if (option.id().startsWith(buffer)) {
                // Check if this is the first candidate found.
                if (match == null) {
                    // This is the first candidate we found. Compute the substring required to
                    // auto-complete the buffer.
                    match = option.id().substring(buffer.length());
                } else {
                    // We can't auto-complete if there is more than one candidate, so we exit and
                    // don't do anything.
                    return null;
                }
            }
        }

        // If we get to this point, there are either zero candidates to auto-complete (in which
        // case `match == null`) or there is exactly one candidate.
        return match;
    }
}
