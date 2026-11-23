package edu.miamioh.cse201.oceans14.ui.text;

import org.jline.utils.AttributedCharSequence;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.util.List;

/**
 * Class responsible for computing line breaks.
 *
 * @see compute
 */
class LineBreaks {
    /** The original, unwrapped paragraph. */
    private final String paragraph;
    /** The paragraph parsed as a list of {@link Token}s. */
    private final List<Token> tokens;
    /** The wrapped paragraph, where each line is a separate entry in the list. */
    private final List<String> lines;

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

    public LineBreaks(String paragraph, List<Token> tokens, List<String> lines, int maxWidth) throws IllegalArgumentException {
        if (maxWidth < 2) {
            throw new IllegalArgumentException("Wrapped paragraph max width was " + maxWidth + ", but must be 2 or greater");
        }

        this.paragraph = paragraph;
        this.tokens = tokens;
        this.lines = lines;
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
            this.breakLine(this.lineEnd, BreakKind.Soft);
        }
    }

    private void processWord(Token.Word word) {
        // Check if there's not enough room to include the word on the line.
        if (this.lineLength() + word.length() > this.maxWidth) {
            // Check if the line is currently empty. If it is, that means the word itself
            // is longer than the max width and requires a hard break.
            if (this.lineIsEmpty()) {
                // There's not enough room to include the full word. Grow the line to include as
                // much of the word as possible.
                this.growLine(lineStart + this.invisibleChars + maxWidth - 1);

                // Introduce a hard break, splitting the word in two.
                this.breakLine(lineStart + this.invisibleChars + maxWidth - 1, BreakKind.Hard);

                // Process the remaining word.
                this.processWord(new Token.Word(lineStart, word.end()));
            } else {
                // There's not enough room to include the word. Introduce a soft break
                // before the word.
                this.breakLine(word.start(), BreakKind.Soft);
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
            this.breakLine(whitespace.end(), BreakKind.Soft);
        } else {
            // Grow line to include whitespace.
            this.growLine(whitespace.end());
        }

        // Move on to the next token.
        this.consumeCurrentToken();
    }

    private void processNewline(Token.Newline newline) {
        // Explicitly newline, add a soft break.
        this.breakLine(newline.end(), BreakKind.Soft);

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

    /**
     * @return The current token being processed.
     * @see consumeCurrentToken
     */
    private Token getCurrentToken() {
        return tokens.get(this.i);
    }

    /**
     * Marks the current token as processed, moving on to the next one.
     *
     * @see getCurrentToken
     */
    private void consumeCurrentToken() {
        this.i++;
    }

    /**
     * Calculates the display length of the current line, when printed to the terminal.
     * <p>
     * Note that the reported length may be smaller than the characters in the line. This is
     * because some characters may be invisible, and won't render when printed to the terminal.
     *
     * @return The line length.
     */
    private int lineLength() {
        // The line may contain "invisible characters", which usually ANSI escape codes that don't
        // take up any space when printed to the terminal.
        return this.lineEnd - this.lineStart - this.invisibleChars;
    }

    /**
     * @return True if the line is visually empty (it may still contain invisible characters).
     */
    private boolean lineIsEmpty() {
        return this.lineLength() == 0;
    }

    /**
     * Grows the length of the current line to end at a new index.
     *
     * @param newEnd The new end index of the line (exclusive).
     */
    private void growLine(int newEnd) {
        this.lineEnd = newEnd;
    }

    /**
     * Introduce a new line break, ending the current line and starting a new one.
     *
     * @param newStart The index to start the new line at.
     * @param breakKind Whether this is a soft break or a hard break.
     */
    private void breakLine(int newStart, BreakKind breakKind) {
        String line = paragraph.substring(this.lineStart, this.lineEnd);

        if (breakKind == BreakKind.Hard) {
            // Add "-" to the end of the line to signal the word is being split in two.
            line += "-";
        }

        if (this.lineStartsStyled()) {
            // Prepend style to the beginning of line.
            line = styleToAnsi(this.lineStartStyle) + line;
            this.lineStartStyle = AttributedStyle.DEFAULT;
        }

        if (this.lineEndsStyled()) {
            // Reset style at the end and record what style the next line should begin with.
            line += "\033[0m";
            this.lineStartStyle = this.styleTracker.style();
        }

        // Commit current line.
        lines.add(line);

        // Configure the index of the next line.
        this.lineStart = this.lineEnd = newStart;

        // Reset invisible chars.
        this.invisibleChars = 0;
    }

    /**
     * @return True if the current line starts with a non-default style.
     */
    private boolean lineStartsStyled() {
        return !this.lineStartStyle.equals(AttributedStyle.DEFAULT);
    }

    /**
     * @return True if the current line ends with a non-default style.
     */
    private boolean lineEndsStyled() {
        return !this.styleTracker.style().equals(AttributedStyle.DEFAULT);
    }

    /**
     * Converts an {@link AttributedStyle} into an ANSI escape code.
     *
     * @param style The style to convert.
     * @return The ANSI escape code for that style.
     */
    private static String styleToAnsi(AttributedStyle style) {
        // Render the style on a string. If the style was `AttributedStyle.BOLD`, for example, this
        // string would be "\033[1m \033[0m".
        String ansi = new AttributedString(" ", style)
                .toAnsi(AttributedCharSequence.TRUE_COLORS, AttributedCharSequence.ForceMode.None);

        // Return only the escape code before the space. If `ansi` was "\033[1m \033[0m", this
        // would return "\033[1m".
        return ansi.substring(0, ansi.indexOf(' '));
    }

    /**
     * Represents the kind of line break.
     */
    private enum BreakKind {
        /** A soft break, where the paragraph is split on whitespace. */
        Soft,
        /** A hard break, where the paragraph is split in the middle of a word. */
        Hard;
    }
}
