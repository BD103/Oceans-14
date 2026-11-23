package edu.miamioh.cse201.oceans14.ui.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

public class TestWrapParagraph {
    @Test
    void emptyParagraph() {
        WrapParagraph wrapped = new WrapParagraph("", 10);
        Assertions.assertLinesMatch(
                List.of(),
                wrapped.getLines()
        );
    }

    @Test
    void noBreaks() {
        WrapParagraph wrapped = new WrapParagraph("short", 40);
        Assertions.assertLinesMatch(
                List.of("short"),
                wrapped.getLines()
        );
    }

    @Test
    void softBreaks() {
        WrapParagraph wrapped = new WrapParagraph("A AB ABC AB A ABCD A A A", 4);
        Assertions.assertLinesMatch(
                List.of(
                        "A AB",
                        "ABC ",
                        "AB A",
                        "ABCD",
                        "A A ",
                        "A"
                ),
                wrapped.getLines()
        );
    }

    @Test
    void hardBreaks() {
        WrapParagraph wrapped = new WrapParagraph("ABCDEFGHIJKLM", 5);
        Assertions.assertLinesMatch(
                List.of(
                        "ABCD-",
                        "EFGH-",
                        "IJKLM"
                ),
                wrapped.getLines()
        );
    }

    @Test
    void hangingHardBreak() {
        WrapParagraph wrapped = new WrapParagraph("A REALLYLONGWORD", 7);
        Assertions.assertLinesMatch(
                List.of(
                        "A ",
                        "REALLY-",
                        "LONGWO-",
                        "RD"
                ),
                wrapped.getLines()
        );
    }

    @Test
    void muchWhitespace() {
        WrapParagraph wrapped = new WrapParagraph("A AAA   A  AA     AA    AAAAA", 5);
        Assertions.assertLinesMatch(
                List.of(
                        "A AAA",
                        "A  AA",
                        "AA",
                        "AAAAA"
                ),
                wrapped.getLines()
        );
    }

    @Test
    void whitespaceAffix() {
        WrapParagraph wrapped = new WrapParagraph("  Hello Joe ", 7);
        Assertions.assertLinesMatch(
                List.of(
                        "  Hello",
                        "Joe "
                ),
                wrapped.getLines()
        );
    }

    @Test
    void manualNewlines() {
        WrapParagraph wrapped = new WrapParagraph("Favorite colors:\n- \033[31mRed\033[0m\n- \033[32mGreen\033[0m\n- \033[34mBlue\033[0m", 20);
        Assertions.assertLinesMatch(
                List.of(
                        "Favorite colors:",
                        "- \033[31mRed\033[0m",
                        "- \033[32mGreen\033[0m",
                        "- \033[34mBlue\033[0m"
                ),
                wrapped.getLines()
        );
    }

    @Test
    void colorBeforeLineBreak() {
        WrapParagraph wrapped = new WrapParagraph("\033[31mHi\033[0m Nayrb!", 6);
        Assertions.assertLinesMatch(
                List.of(
                        "\033[31mHi\033[0m ",
                        "Nayrb!"
                ),
                wrapped.getLines()
        );
    }

    @Test
    void colorAcrossLines() {
        WrapParagraph wrapped = new WrapParagraph("\033[32mSome text", 4);
        Assertions.assertLinesMatch(
                List.of(
                        "\033[32mSome\033[0m",
                        "\033[32mtext\033[0m"
                ),
                wrapped.getLines()
        );
    }

    @Test
    void fgBgColorMerge() {
        WrapParagraph wrapped = new WrapParagraph("\033[35mYes \033[41mthere is", 9);
        Assertions.assertLinesMatch(
                List.of(
                        "\033[35mYes \033[41mthere\033[0m",
                        "\033[35;41mis\033[0m"
                ),
                wrapped.getLines()
        );
    }

    @Test
    void longColor() {
        WrapParagraph wrapped = new WrapParagraph(
                "You \033[31mstand at the entrance\033[0m of the Beach View Casino",
                24
        );

        Assertions.assertLinesMatch(
                List.of(
                        "You \033[31mstand at the \033[0m",
                        "\033[31mentrance\033[0m of the Beach ",
                        "View Casino"
                ),
                wrapped.getLines()
        );
    }

    @Test
    void hardBreakWithColor() {
        WrapParagraph wrapped = new WrapParagraph(
                "\033[35mABCD\033[32mEFGHI",
                3
        );

        Assertions.assertLinesMatch(
                List.of(
                        "\033[35mAB-\033[0m",
                        "\033[35mCD\033[32m\033[0m",
                        "\033[32mEF-\033[0m",
                        "\033[32mGHI\033[0m"
                ),
                wrapped.getLines()
        );
    }

    @ParameterizedTest
    @ValueSource(strings = { "✨", "\1", "\r", "🧑‍🧑‍🧒‍🧒" })
    void invalid(String input) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new WrapParagraph(input, 20));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1})
    void tooSmallMaxWidth(int maxWidth) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new WrapParagraph("Hello", maxWidth));
    }
}
