package edu.miamioh.cse201.oceans14.ui.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a paragraph that is wrapped so each line is no longer than a specified maximum width.
 */
public class WrapParagraph {
    /** The original, unwrapped paragraph. It should contain no newlines. */
    private final String paragraph;
    /** The wrapped paragraph, where each line is a separate entry in the list. */
    private final List<String> lines = new ArrayList<>();

    /**
     * Creates a new wrapped paragraph.
     *
     * @param paragraph The text to wrap. This must not contain any line breaks {@code '\n'}.
     * @param maxWidth The initial max width of the wrapped paragraph.
     */
    public WrapParagraph(String paragraph, int maxWidth) {
        this.paragraph = paragraph;
        this.resize(maxWidth);
    }

    /**
     * Returns the wrapped paragraph as a list of lines.
     *
     * @return The paragraph.
     */
    public List<String> getLines() {
        return Collections.unmodifiableList(this.lines);
    }

    /**
     * Calculates line breaks and populates {@link lines} with the results.
     * <p>
     * Internally, this implements a greedy algorithm that minimizes the number of lines. For more
     * details, see <a href="https://en.wikipedia.org/wiki/Wrapping_(text)">Wikipedia</a>.
     *
     * @param maxWidth The maximum width of a line.
     * @throws IllegalArgumentException If maxWidth is less than 2.
     */
    public void resize(int maxWidth) throws IllegalArgumentException {
        if (maxWidth < 2) {
            throw new IllegalArgumentException("Wrapped paragraph max width was " + maxWidth + ", but must be 2 or greater");
        }

        this.lines.clear();

        // There are two spans represented here. The span of the line being constructed,
        // [lineStart, lineEndWordStart), and the span of the next word found,
        // [lineEndWordStart, wordEnd).
        int lineStart = 0;
        int lineEndWordStart = 0;
        int wordEnd;

        // Repeat until there are no more words that haven't been emitted as lines.
        while (true) {
            // Find where the next word ends by looking for the next space.
            wordEnd = this.paragraph.indexOf(' ', lineEndWordStart + 1);

            // If no space is found, the word spans to the end of the paragraph.
            if (wordEnd < 0) {
                wordEnd = this.paragraph.length();
            }

            // Next word is empty, we're reached the end of the paragraph. Add the last line and
            // exit.
            if (lineEndWordStart == wordEnd) {
                // Add a new line with a soft break at the end.
                lines.add(this.paragraph.substring(lineStart, lineEndWordStart));
                break;
            }

            // If adding the word would cause the line to grow to long.
            if (wordEnd - lineStart > maxWidth) {
                // If the word itself is longer than the max width, we need to insert a hard break.
                // This condition evaluates to true when we just added a break, but we need another
                // break due to word being too large.
                if (lineStart == lineEndWordStart) {
                    // Add a new line for the portion of the word. We're including only
                    // maxWidth - 1 characters in this line, as the hard break will add an "-" at
                    // the end.
                    lines.add(this.paragraph.substring(lineStart, lineStart + maxWidth - 1) + "-");

                    // Start the next blank line immediately where we left off.
                    lineStart = lineStart + maxWidth - 1;
                } else {
                    // Complete current line, adding a soft break at the end.
                    lines.add(this.paragraph.substring(lineStart, lineEndWordStart));

                    // Create new blank line, skipping the space.
                    lineStart = lineEndWordStart + 1;

                    // If there are multiple spaces, keep skipping forward until a non-space is
                    // found, or we reach EOF.
                    while (lineStart < this.paragraph.length() && this.paragraph.charAt(lineStart) == ' ') {
                        lineStart++;
                    }
                }

                lineEndWordStart = lineStart;
            } else {
                // Grow line to include word.
                lineEndWordStart = wordEnd;
            }
        }
    }
}
