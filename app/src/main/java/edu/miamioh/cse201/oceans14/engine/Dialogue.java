package edu.miamioh.cse201.oceans14.engine;

import java.util.List;

/**
 * A message displayed to the user in the dialogue view.
 */
public sealed interface Dialogue {
    /**
     * @return The dialogue formatted as a string.
     */
    String display();

    /**
     * A system message.
     * <p>
     * This should be used when describing objects such as actors, rooms, and items to the player.
     * It should also be used to describe the player moving between locations.
     *
     * @param message The message to be displayed, should not contain any newlines.
     */
    record System(String message) implements Dialogue {
        @Override
        public String display() {
            return this.message;
        }
    }

    /**
     * An actor message.
     * <p>
     * This should be used when an actor speaks to the player.
     *
     * @param actorName The name of the actor.
     * @param message The message the actor says, should not contain any newlines.
     */
    record Actor(String actorName, String message) implements Dialogue {
        @Override
        public String display() {
            return this.actorName + ": " + this.message;
        }
    }

    /**
     * An error message.
     * <p>
     * This should be used when the player tries to do something impossible, such as typing in an
     * invalid command or trying to talk to an actor that doesn't exist. Don't use this if the
     * player doesn't have the requirements to perform an action, such as entering a locked door
     * without the key, use {@link System} instead.
     *
     * @param message The message to be displayed, should not contain any newlines.
     */
    record Error(String message) implements Dialogue {
        @Override
        public String display() {
            return "ERROR: " + this.message;
        }
    }

    /**
     * A list of dialogue entries.
     * <p>
     * This lets you return multiple dialogue entries from a single
     * {@link Interactable#performAction}.
     *
     * @param entries The dialogue entries, will be displayed in order.
     */
    record DialogueList(List<Dialogue> entries) implements Dialogue {
        @Override
        public String display() {
            throw new UnsupportedOperationException();
        }
    }
}
