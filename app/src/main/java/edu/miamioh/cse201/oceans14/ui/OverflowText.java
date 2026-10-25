package edu.miamioh.cse201.oceans14.ui;

/**
 * Takes a string and limits its length to a maximum width, adding "..." to indicate overflow.
 */
public class OverflowText {
    private final String line;
    private String overflowed;

    /**
     * Creates a new overflow string.
     *
     * @param line The original line.
     * @param maxWidth The maximum width of the line.
     * @throws IllegalArgumentException If maxWidth is less than 3.
     */
    public OverflowText(String line, int maxWidth) throws IllegalArgumentException {
        this.line = line;
    }

    /**
     * Resizes this overflow string to a new max width.
     *
     * @param maxWidth The maximum width of the line.
     * @throws IllegalArgumentException If maxWidth is less than 3.
     */
    public void resize(int maxWidth) throws IllegalArgumentException {
        if (maxWidth < 3) {
            throw new IllegalArgumentException("Overflow text max width was " + maxWidth + ", but must be 3 or greater");
        }

        if (this.line.length() > maxWidth) {
            this.overflowed = this.line.substring(0, maxWidth - 3) + "...";
        } else {
            // Line is smaller than max width, do nothing.
            this.overflowed = line;
        }
    }

    @Override
    public String toString() {
        return overflowed;
    }
}
