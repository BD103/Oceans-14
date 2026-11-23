package edu.miamioh.cse201.oceans14.ui.text;

import org.jline.utils.AttributedCharSequence;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

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
        // `LineBreaks` is an inner class. Internally, it reads `this.paragraph` and `this.tokens`,
        // writing to `this.lines`.
        new LineBreaks(maxWidth).compute();
    }

    /**
     * Class responsible for computing line breaks.
     *
     * @see compute
     */
    private class LineBreaks {
        /** The maximum width of a single line. */
        private final int maxWidth;

        /** The beginning index (inclusive) of the current line being processed. */
        private int lineStart = 0;
        /** The ending index (exclusive) of the current line being processed. */
        private int lineEnd = 0;

        /** The index of the token currently being processed. */
        private int i = 0;

        /**
         * A mutable string containing every processed escape code. It is used to compute the
         * current style across line breaks with {@link AttributedStringBuilder#style()}.
         */
        private final AttributedStringBuilder styleTracker = new AttributedStringBuilder(0);

        /**
         * The number of invisible characters in the current line, which don't contribute to the
         * width of the line.
         */
        private int invisibleChars = 0;

        /** The style of the beginning of the line. */
        private AttributedStyle lineStartStyle = AttributedStyle.DEFAULT;

        public LineBreaks(int maxWidth) throws IllegalArgumentException {
            if (maxWidth < 2) {
                throw new IllegalArgumentException("Wrapped paragraph max width was " + maxWidth + ", but must be 2 or greater");
            }

            this.maxWidth = maxWidth;
        }

        /**
         * Computes line breaks for {@link paragraph} using {@link tokens}, write results to
         * {@link lines}.
         */
        public void compute() {
            // Clear the list so we can write new results to it.
            lines.clear();

            while (this.i < tokens.size()) {
                Token token = this.getCurrentToken();

                if (token instanceof Token.Word word) {
                    this.processWord(word);
                } else if (token instanceof Token.Whitespace whitespace) {
                   this.processWhitespace(whitespace);
                } else if (token instanceof Token.Newline newline) {
                    this.processNewline(newline);
                } else if (token instanceof Token.EscapeCode escapeCode) {
                    this.processEscapeCode(escapeCode);
                } else if (token instanceof Token.Invalid invalid) {
                    this.processInvalid(invalid);
                } else {
                    throw new IllegalArgumentException("Found unknown token type " + token);
                }
            }

            // Add the leftover line at the end, if it exists.
            if (!this.lineIsEmpty()) {
                this.softBreak(this.lineEnd);
            }
        }

        private void processWord(Token.Word word) {
            // Check if there's not enough room to include the word on the line.
            if (this.lineLength() + word.length() > this.maxWidth) {
                // Check if the line is currently empty. If it is, that means the word itself
                // is longer than the max width and requires a hard break.
                if (this.lineIsEmpty()) {
                    // There's not enough room to include the word. Introduce a hard break,
                    // splitting the word in two.
                    this.hardBreak(lineStart + maxWidth - 1);

                    // Process the remaining word.
                    this.processWord(new Token.Word(lineStart, word.end()));
                } else {
                    // There's not enough room to include the word. Introduce a soft break
                    // before the word.
                    this.softBreak(word.start());
                }
            } else {
                // Grow line to include word.
                this.growLine(word.end());

                // Move on to the next token.
                this.consumeCurrentToken();
            }
        }

        private void processWhitespace(Token.Whitespace whitespace) {
            // Check if there's not enough room to include the whitespace on the line.
            if (this.lineLength() + whitespace.length() > maxWidth) {
                // There's not enough room on the line to include the whitespace. Coerce the
                // whitespace into a soft break and start a new line.
                this.softBreak(whitespace.end());
            } else {
                // Grow line to include whitespace.
                this.growLine(whitespace.end());
            }

            // Move on to the next token.
            this.consumeCurrentToken();
        }

        private void processNewline(Token.Newline newline) {
            // Explicitly newline, add a soft break.
            this.softBreak(newline.end());

            // Move on to the next token.
            this.consumeCurrentToken();
        }

        private void processEscapeCode(Token.EscapeCode escapeCode) {
            // Add escape code to the style tracker.
            this.styleTracker.appendAnsi(paragraph.substring(escapeCode.start(), escapeCode.end()));

            // Record how many invisible chars this escape code takes up. (This number is used to
            // track a line's length in characters vs. its width when printed to the terminal.)
            this.invisibleChars += escapeCode.length();

            // Commit this escape code to the line, consuming it.
            this.growLine(escapeCode.end());
            this.consumeCurrentToken();
        }

        private void processInvalid(Token.Invalid invalid) {
            throw new IllegalArgumentException("Found invalid token " + paragraph.substring(invalid.start(), invalid.end()) + " at " + invalid);
        }

        private Token getCurrentToken() {
            return tokens.get(this.i);
        }

        private void consumeCurrentToken() {
            this.i++;
        }

        private int lineLength() {
            return this.lineEnd - this.lineStart - this.invisibleChars;
        }

        private boolean lineIsEmpty() {
            return this.lineStart == this.lineEnd;
        }

        private void softBreak(int newStart) {
            String line = paragraph.substring(this.lineStart, this.lineEnd);

            if (this.lineStartsStyled()) {
                // Prepend style to the beginning of line.
                line = styleToAnsi(this.lineStartStyle) + line;
                this.lineStartStyle = AttributedStyle.DEFAULT;
            }

            if (this.lineEndsStyled()) {
                // Reset style at the end and record what style the next line should begin with.
                line = line + "\033[0m";
                this.lineStartStyle = this.styleTracker.style();
            }

            // Commit current line.
            lines.add(line);

            // Configure the index of the next line.
            this.lineStart = this.lineEnd = newStart;

            // Reset invisible chars.
            this.invisibleChars = 0;
        }

        private void hardBreak(int newStart) {
            // Add "-" to the end of the line to signal the word is being split in two.
            String line = paragraph.substring(this.lineStart, newStart) + "-";

            if (this.lineStartsStyled()) {
                // Prepend style to the beginning of line.
                line = styleToAnsi(this.lineStartStyle) + line;
                this.lineStartStyle = AttributedStyle.DEFAULT;
            }

            if (this.lineEndsStyled()) {
                // Reset style at the end and record what style the next line should begin with.
                line = line + "\033[0m";
                this.lineStartStyle = this.styleTracker.style();
            }

            // Commit current line.
            lines.add(line);

            // Configure the index of the next line.
            this.lineStart = this.lineEnd = newStart;

            // Reset invisible chars.
            this.invisibleChars = 0;
        }

        private void growLine(int newEnd) {
            this.lineEnd = newEnd;
        }

        private boolean lineStartsStyled() {
            return !this.lineStartStyle.equals(AttributedStyle.DEFAULT);
        }

        private boolean lineEndsStyled() {
            return !this.styleTracker.style().equals(AttributedStyle.DEFAULT);
        }

        private static String styleToAnsi(AttributedStyle style) {
            String ansi = new AttributedString(" ", style)
                    .toAnsi(AttributedCharSequence.TRUE_COLORS, AttributedCharSequence.ForceMode.None);

            return ansi.substring(0, ansi.indexOf(' '));
        }
    }
}
