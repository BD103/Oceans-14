package edu.miamioh.cse201.oceans14.ui.text;

import java.util.ArrayList;
import java.util.List;

/**
 * Parses a string into a sequence of {@link Token}s.
 */
public class TextTokenizer {
    private final String text;
    private int i = 0;

    /**
     * Creates a new tokenizer over a string of text.
     *
     * @param text The text to tokenize, it must not contain null "\0" characters.
     */
    private TextTokenizer(String text) {
        this.text = text;
    }

    public static List<Token> tokenize(String text) {
        ArrayList<Token> tokens = new ArrayList<>();
        TextTokenizer it = new TextTokenizer(text);
        Token token;

        while ((token = it.next()) != null) {
            tokens.add(token);
        }

        return tokens;
    }

    /**
     * @return The next token, or null if at the end of the string.
     */
    private Token next() {
        int start = this.i;
        char c = this.peekChar();

        // End of text.
        if (c == '\0') {
            return null;
        }

        i++;

        // Parse words, one or more printable ASCII characters.
        if (isPrintableASCII(c)) {
            while (isPrintableASCII(this.peekChar())) {
                this.i++;
            }

            return new Token.Word(start, this.i);
        }

        // Parse whitespace, one or more spaces.
        if (c == ' ') {
            while (this.peekChar() == ' ') {
                this.i++;
            }

            return new Token.Whitespace(start, this.i);
        }

        // Parse newlines.
        if (c == '\n') {
            return new Token.Newline();
        }

        // Parse escape codes, which are in the format "\033[...m" where an arbitrary number of
        // characters can be between "[" and "m".
        if (c == '\033') {
            if (this.peekChar() == '[') {
                this.i++;

                while (true) {
                    c = this.peekChar();

                    if (c == 'm') {
                        // End of escape code.
                        i++;
                        return new Token.EscapeCode(start, this.i);
                    } else if (c == '\0') {
                        // End of string without properly terminating escape code.
                        return new Token.Invalid(start, this.i);
                    } else {
                        i++;
                    }
                }
            } else {
                // Freestanding ESC without a following "[".
                return new Token.Invalid(start, this.i);
            }
        }

        // If it doesn't pass any of the above checks, then it's an unsupported character.
        return new Token.Invalid(start, this.i);
    }

    /**
     * Gets the next character without incrementing {@link i}.
     *
     * @return The next character, or "\0" if at the end.
     */
    private char peekChar() {
        if (this.i < this.text.length()) {
            return this.text.charAt(this.i);
        } else {
            return '\0';
        }
    }

    /**
     * @param c The character to check.
     * @return True if the character is printable ASCII.
     */
    private static boolean isPrintableASCII(char c) {
        // <https://en.wikipedia.org/wiki/ASCII#Character_set>
        return c >= '!' && c <= '~';
    }
}
