package edu.miamioh.cse201.oceans14.ui;

import java.util.ListIterator;

/**
 * Tracks the history of commands run by the player, and scrolling through that history.
 */
public class CommandHistory {
    private final FixedCapacityList<String> entries = new FixedCapacityList<>(250);

    /**
     * An iterator over {@link entries} for when the player scrolls back through the history.
     * <p>
     * This iterator is used to traverse the history, and tracks where in history the player is
     * currently scrolling through.
     */
    private ListIterator<String> it = null;

    /**
     * Whether {@link it}'s cursor is immediately before or after the most recently scrolled
     * history entry.
     */
    private CursorPosition cursorPosition = null;

    /**
     * Adds a new command to the history.
     *
     * @param command The command just entered by the player.
     */
    public void add(String command) {
        this.entries.add(command);
    }

    /**
     * Scrolls up in the command history.
     *
     * @return The previous command, or null if there is no command available.
     */
    public String scrollUp() {
        if (this.entries.size() == 0) {
            // There are no entries, don't do anything.
            return null;
        }

        if (this.it == null) {
            // Initialize the iterator.
            this.it = this.entries.listIterator();
            this.cursorPosition = CursorPosition.AfterCurrent;
        }

        if (this.cursorPosition == CursorPosition.BeforeCurrent) {
            // Move cursor to be after current element.
            this.it.next();
            this.cursorPosition = CursorPosition.AfterCurrent;
        }

        if (this.it.hasNext()) {
            return this.it.next();
        } else {
            // At the very end of the list, do nothing.
            return null;
        }
    }

    /**
     * Scrolls down in the command history.
     *
     * @return The latter command, or null if there is no command available.
     */
    public String scrollDown() {
        if (this.it == null) {
            // We aren't scrolling, don't do anything.
            return null;
        }

        if (this.cursorPosition == CursorPosition.AfterCurrent) {
            // Move cursor to be before current element.
            this.it.previous();
            this.cursorPosition = CursorPosition.BeforeCurrent;
        }

        if (this.it.hasPrevious()) {
            return this.it.previous();
        } else {
            // At the very beginning of the list, stop scrolling and give a blank line.
            this.it = null;
            this.cursorPosition = null;
            return "";
        }
    }

    /**
     * Resets scrolling.
     */
    public void clearScroll() {
        this.it = null;
        this.cursorPosition = null;
    }

    /**
     * Represents where the {@link ListIterator} cursor is relative to the currently displayed
     * command.
     */
    private enum CursorPosition {
        /**
         * The {@link ListIterator} cursor is immediately before the current command.
         */
        BeforeCurrent,
        /**
         * The {@link ListIterator} cursor is immediately after the current command.
         */
        AfterCurrent
    }
}
