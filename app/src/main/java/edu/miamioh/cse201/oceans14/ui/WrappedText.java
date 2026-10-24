package edu.miamioh.cse201.oceans14.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Takes a contiguous string and breaks it up into lines that do not exceed a max width.
 */
public class WrappedText implements Iterable<String> {
    private final String paragraph;
    private final List<Line> lines = new ArrayList<>();

    /**
     * Wraps a paragraph into lines.
     *
     * @param paragraph The string to wrap. It should not contain any newline characters ('\n').
     * @param maxWidth The maximum width of a line, must be 2 or greater.
     * @throws IllegalArgumentException If maxWidth is less than 2.
     */
    public WrappedText(String paragraph, int maxWidth) throws IllegalArgumentException {
        this.paragraph = paragraph;
        this.calculateLines(maxWidth);
    }

    /**
     * Calculates line breaks and populates {@link lines} with the results.
     *
     * @param maxWidth The maximum width of a line.
     * @throws IllegalArgumentException If maxWidth is less than 2.
     */
    private void calculateLines(int maxWidth) throws IllegalArgumentException {
        if (maxWidth < 2) {
            throw new IllegalArgumentException("Wrapped text max width was " + maxWidth + ", but must be 2 or greater");
        }

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
                lines.add(new Line(lineStart, lineEndWordStart, BreakKind.SOFT));
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
                    lines.add(new Line(lineStart, lineStart + maxWidth - 1, BreakKind.HARD));

                    // Start the next blank line immediately where we left off.
                    lineStart = lineStart + maxWidth - 1;
                } else {
                    // Complete current line.
                    lines.add(new Line(lineStart, lineEndWordStart, BreakKind.SOFT));

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

    @Override
    public Iterator<String> iterator() {
        return new Iterator<>() {
            private final Iterator<Line> it = lines.iterator();

            @Override
            public boolean hasNext() {
                return it.hasNext();
            }

            @Override
            public String next() {
                Line line = it.next();
                String substring = paragraph.substring(line.start, line.end);

                return switch (line.kind) {
                    case SOFT -> substring;
                    case HARD -> substring + "-";
                };
            }
        };
    }

    /**
     * Represents a line in a paragraph.
     *
     * @param start The index (inclusive) of the paragraph where the line starts.
     * @param end The index (exclusive) of the paragraph where the line ends.
     * @param kind Whether the line ends in a soft or hard break.
     */
    private record Line(int start, int end, BreakKind kind) {}

    /**
     * Represents whether a line ends in a soft break (separated by whitespace) or a hard break
     * (dash splitting a word).
     */
    private enum BreakKind {
        SOFT,
        HARD;
    }
}
