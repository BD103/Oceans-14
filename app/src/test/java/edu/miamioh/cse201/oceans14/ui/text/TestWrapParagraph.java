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
                List.of(""),
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
                        "ABC",
                        "AB A",
                        "ABCD",
                        "A A",
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
                        "A",
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
                        "AA   ",
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

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1})
    void tooSmallMaxWidth(int maxWidth) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new WrapParagraph("Hello", maxWidth));
    }
}
