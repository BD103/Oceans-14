package edu.miamioh.cse201.oceans14.ui.input;

/**
 * An editable text buffer with a cursor, used to by the player to write commands sent to the game.
 * <p>
 * The cursor is in "insert" mode, which means new inputs are inserted at the cursor's position and
 * whatever was previous at that position is shifted to the right. This matches the behavior of
 * text editors like Notepad, VSCode, and Nano.
 *
 * @see edu.miamioh.cse201.oceans14.ui.CommandView
 */
public class CommandBuffer {
    /** The editable text buffer. */
    private final StringBuilder buffer = new StringBuilder();

    /**
     * The cursor's position.
     * <p>
     * This must remain between 0 and {@code buffer.length()}, inclusive.
     */
    private int cursor = 0;

    /**
     * Inserts a character at the cursor's position.
     *
     * @param c The character to insert.
     */
    public void insert(char c) {
        this.buffer.insert(this.cursor, c);
        this.cursor++;
    }

    /**
     * Clears the entire buffer's contents, making it empty.
     */
    public void clear() {
        this.buffer.delete(0, this.buffer.length());
        this.cursor = 0;
    }

    /**
     * Deletes the character behind the cursor.
     */
    public void delete() {
        if (this.cursor > 0) {
            this.buffer.deleteCharAt(this.cursor - 1);
            this.cursor--;
        }
    }

    /**
     * Moves the cursor one position to the left.
     */
    public void left() {
        if (this.cursor > 0) {
            this.cursor--;
        }
    }

    /**
     * Moves the cursor one position to the right.
     */
    public void right() {
        if (this.cursor < this.buffer.length()) {
            this.cursor++;
        }
    }

    /**
     * @return The contents of the buffer.
     */
    public String getBuffer() {
        return this.buffer.toString();
    }

    /**
     * @return The cursor's position.
     */
    public int getCursor() {
        return this.cursor;
    }
}
