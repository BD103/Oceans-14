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
            if (option.id().startsWith(buffer)) {
                if (match == null) {
                    // There's at least one match.
                    match = option.id().substring(buffer.length());
                } else {
                    // Multiple matches, don't complete anything.
                    return null;
                }
            }
        }

        return match;
    }
}
