package edu.miamioh.cse201.oceans14.ui.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

public class TestTextTokenizer {
    @Test
    void empty() {
        List<Token> tokens = TextTokenizer.tokenize("");

        Assertions.assertTrue(tokens.isEmpty());
    }

    @ParameterizedTest
    @ValueSource(strings = { "Hello", "!", "~" })
    void word(String text) {
        List<Token> tokens = TextTokenizer.tokenize(text);

        Assertions.assertIterableEquals(
                List.of(new Token.Word(0, text.length())),
                tokens
        );
    }

    @ParameterizedTest
    @ValueSource(strings = { " ", "         " })
    void whitespace(String text) {
        List<Token> tokens = TextTokenizer.tokenize(text);

        Assertions.assertIterableEquals(
                List.of(new Token.Whitespace(0, text.length())),
                tokens
        );
    }

    @Test
    void newline() {
        List<Token> tokens = TextTokenizer.tokenize("\n");

        Assertions.assertIterableEquals(
                List.of(new Token.Newline()),
                tokens
        );
    }

    @ParameterizedTest
    @ValueSource(strings = { "\033[m", "\033[0m", "\033[31m", "\033[38;5;94m", "\033[38;2;0;255;255m" })
    void escapeCode(String text) {
        List<Token> tokens = TextTokenizer.tokenize(text);

        Assertions.assertIterableEquals(
                List.of(new Token.EscapeCode(0, text.length())),
                tokens
        );
    }

    @ParameterizedTest
    @ValueSource(strings = { "\t", "\r", "\u007f", "\033" })
    void invalid(String text) {
        List<Token> tokens = TextTokenizer.tokenize(text);

        Assertions.assertIterableEquals(
                List.of(new Token.Invalid(0, text.length())),
                tokens
        );
    }

    @Test
    void sequence() {
        List<Token> tokens = TextTokenizer.tokenize("Hello, there!\n\033[31mWoah!\033[0m");

        Assertions.assertIterableEquals(
                List.of(
                        new Token.Word(0, 6),
                        new Token.Whitespace(6, 7),
                        new Token.Word(7, 13),
                        new Token.Newline(),
                        new Token.EscapeCode(14, 19),
                        new Token.Word(19, 24),
                        new Token.EscapeCode(24, 28)
                ),
                tokens
        );
    }

    @Test
    void tokenize() {
        List<Token> tokens = TextTokenizer.tokenize("\033[97mYes\n");

        Assertions.assertIterableEquals(
                List.of(
                        new Token.EscapeCode(0, 5),
                        new Token.Word(5, 8),
                        new Token.Newline()
                ),
                tokens
        );
    }
}
