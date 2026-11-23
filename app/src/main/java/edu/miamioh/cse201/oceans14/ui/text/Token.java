package edu.miamioh.cse201.oceans14.ui.text;

/**
 * A parsed token of text.
 */
public sealed interface Token {
    /**
     * @return The length of the token in characters. (Note that the token may take up less space
     *         when printed to the terminal.)
     */
    int length();

    /**
     * One or more printable ASCII characters.
     *
     * @param start The start (inclusive) of the word.
     * @param end The end (exclusive) of the word.
     */
    record Word(int start, int end) implements Token {
        @Override
        public int length() {
            return this.end - this.start;
        }
    }

    /**
     * One or more space " " characters.
     * <p>
     * Other whitespace characters, such as carriage returns "\r" and tabs "\t" are
     * unsupported. Newline "\n" is represented as {@link Newline}.
     *
     * @param start The start (inclusive) of the whitespace.
     * @param end The end (exclusive) of the whitespace.
     */
    record Whitespace(int start, int end) implements Token {
        @Override
        public int length() {
            return this.end - this.start;
        }
    }

    /**
     * A manual newline in the text, represented by a "\n" character.
     *
     * @param start The start (inclusive) of the newline.
     * @param end The end (exclusive) of the newline.
     */
    record Newline(int start, int end) implements Token {
        @Override
        public int length() {
            throw new UnsupportedOperationException("Newlines do not have a length.");
        }
    }

    /**
     * A Select Graphic Rendition (SGR) escape code, in the format of "\033[...m".
     * <p>
     * This escape code does not take up any space when printed to the terminal.
     *
     * @param start The start (inclusive) of the escape code.
     * @param end The end (exclusive) of the escape code.
     */
    record EscapeCode(int start, int end) implements Token {
        @Override
        public int length() {
            return this.end - this.start;
        }
    }

    /**
     * An invalid string with unsupported characters.
     *
     * @param start The start (inclusive) of the invalid string.
     * @param end The end (exclusive) of the invalid string.
     */
    record Invalid(int start, int end) implements Token {
        @Override
        public int length() {
            return this.end - this.start;
        }
    }
}
