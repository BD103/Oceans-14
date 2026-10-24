package edu.miamioh.cse201.oceans14.ui;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class WrappedText implements Iterable<String> {
    private final String paragraph;
    private final List<Line> lines = new ArrayList<>();

    public WrappedText(String paragraph, int maxWidth) {
        this.paragraph = paragraph;
        this.calculateBreaks(maxWidth);
    }

    private void calculateBreaks(int maxWidth) {
        // There are two spans represented here. The span of the line being constructed,
        // [lineStart, lineEndWordStart), and the span of the next word found, [lineEndWordStart, wordEnd).
        int lineStart = 0;
        int lineEndWordStart = 0;
        int wordEnd;

        // Repeat until there are no spaces left in the paragraph.
        while ((wordEnd = this.paragraph.indexOf(' ', lineEndWordStart + 1)) > 0) {
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
                    // TODO: handle multiple spaces
                    lineStart = lineEndWordStart + 1;
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

    // TODO: remove
    public static void main(String[] args) {
        // TODO: test "A AB  ABC ABCDEF AB"
        WrappedText wrapped = new WrappedText("A AB ABC ABCDEF AB ABCDEFGHIJKLMNOPQRSTUVWXYZ", 4);

        for (String line : wrapped) {
            System.out.println(line);
        }
    }
}
