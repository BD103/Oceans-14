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
        // Compute line breaks, modifying `this.lines` with the results.
        new LineBreaks(this.paragraph, this.tokens, this.lines, maxWidth).compute();
    }
}
