package edu.miamioh.cse201.oceans14.ui.text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents a paragraph that is wrapped so each line is no longer than a specified maximum width.
 */
public class WrapParagraph {
    /** The original, unwrapped paragraph. */
    private final String paragraph;
    /** The paragraph parsed as a list of {@link Token}s. */
    private final List<Token> tokens;
    /** The wrapped paragraph, where each line is a separate entry in the list. */
    private final List<String> lines = new ArrayList<>();

    /**
     * Creates a new wrapped paragraph.
     *
     * @param paragraph The text to wrap.
     * @param maxWidth The initial max width of the wrapped paragraph.
     */
    public WrapParagraph(String paragraph, int maxWidth) {
        this.paragraph = paragraph;
        this.tokens = TextTokenizer.tokenize(this.paragraph);
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

        // The beginning index (inclusive) of the current line being processed.
        int lineStart = 0;
        // The ending index (exclusive) of the current line being processed.
        int lineEnd = 0;

        // The index of the token currently being processed.
        int i = 0;

        while (i < this.tokens.size()) {
            Token token = this.tokens.get(i);

            if (token instanceof Token.Word word) {
                // Check if there's not enough room to include the word on the line.
                if (word.end() - lineStart > maxWidth) {
                    // Check if the line is currently empty. If it is, that means the word itself
                    // is longer than the max width and requires a hard break.
                    if (lineStart == lineEnd) {
                        // There's not enough room to include the word. Introduce a hard break,
                        // splitting the word in two, adding a "-" on the end of the line.
                        this.lines.add(this.paragraph.substring(lineStart, lineStart + maxWidth - 1) + "-");

                        // Move parser to middle of word, where the hard break was.
                        lineStart = lineEnd = lineStart + maxWidth - 1;
                    } else {
                        // There's not enough room to include the word. Introduce a soft break
                        // before the word.
                        this.lines.add(this.paragraph.substring(lineStart, lineEnd));

                        // Move parser directly before word.
                        lineStart = lineEnd = word.start();
                    }
                } else {
                    // Grow line to include word.
                    lineEnd = word.end();

                    // Move on to the next token.
                    i++;
                }
            } else if (token instanceof Token.Whitespace whitespace) {
                // Check if there's not enough room to include the whitespace on the line.
                if (whitespace.end() - lineStart > maxWidth) {
                    // There's not enough room on the line to include the whitespace. Coerce the
                    // whitespace into a soft break and start a new line.
                    this.lines.add(this.paragraph.substring(lineStart, lineEnd));

                    // Move parser directly after whitespace.
                    lineStart = lineEnd = whitespace.end();
                } else {
                    // Grow line to include whitespace.
                    lineEnd = whitespace.end();
                }

                // Move on to the next token.
                i++;
            } else if (token instanceof Token.Newline newline) {
                // Explicitly newline, add a soft break.
                this.lines.add(this.paragraph.substring(lineStart, lineEnd));

                // Move parser to directly after newline.
                lineStart = lineEnd = newline.end();

                // Move on to the next token.
                i++;
            } else if (token instanceof Token.EscapeCode escapeCode) {
                // TODO
                // Move on to the next token.
                i++;
            } else {
                // Invalid token, skip it and move on to the next.
                i++;
            }
        }

        // Add the leftover line at the end, if it exists.
        if (lineStart != lineEnd) {
            this.lines.add(this.paragraph.substring(lineStart, lineEnd));
        }
    }
}
