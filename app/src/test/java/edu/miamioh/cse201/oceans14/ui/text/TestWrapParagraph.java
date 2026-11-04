package edu.miamioh.cse201.oceans14.ui.text;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

public class TestWrapParagraph {
    @ParameterizedTest(name = "{0}")
    @CsvSource({
            "empty paragraph, '', '', 10",
            "no breaks, short, short, 40",
            "soft breaks, A AB ABC AB A ABCD A A A, 'A AB\nABC\nAB A\nABCD\nA A\nA', 4",
            "hard breaks, ABCDEFGHIJKLM, 'ABCD-\nEFGH-\nIJKLM', 5",
            "hanging hard break, A REALLYLONGWORD, 'A\nREALLY-\nLONGWO-\nRD', 7",
            "much whitespace, A AAA   A  AA     AA    AAAAA, 'A AAA\nA  AA\nAA   \nAAAAA', 5"
    })
    void wrappedText(String _name, String input, String expected, int maxWidth) {
        WrapParagraph wrapped = new WrapParagraph(input, maxWidth);
        Assertions.assertEquals(expected, wrappedToString(wrapped));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1})
    void tooSmallMaxWidth(int maxWidth) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new WrapParagraph("Hello", maxWidth));
    }

    /**
     * Converts a {@link WrapParagraph} to a string, where each line is separated by a LF character
     * ('\n').
     *
     * @param wrapped The text to be converted.
     * @return The converted text.
     */
    private static String wrappedToString(WrapParagraph wrapped) {
        StringBuilder builder = new StringBuilder();
        List<String> lines = wrapped.getLines();

        if (!lines.isEmpty()) {
            builder.append(lines.get(0));
        }

        for (int i = 1; i < lines.size(); i++) {
            builder.append('\n');
            builder.append(lines.get(i));
        }

        return builder.toString();
    }
}
