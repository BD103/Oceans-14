package edu.miamioh.cse201.oceans14.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Iterator;

public class TestWrappedText {
    @ParameterizedTest(name = "{0}")
    @CsvSource({
            "empty paragraph, '', '', 10",
            "no breaks, short, short, 40",
            "soft breaks, A AB ABC AB A ABCD A A A, 'A AB\nABC\nAB A\nABCD\nA A\nA', 4",
            "hard breaks, ABCDEFGHIJKLM, 'ABCD-\nEFGH-\nIJKLM', 5",
            "hanging hard break, A REALLYLONGWORD, 'A\nREALLY-\nLONGWO-\nRD', 7",
            "much whitespace, A AAA   A  AAA     AA    AAAAA, 'A AAA\n  A \nAAA  \n  AA \n \nAAAAA', 5"
    })
    void wrappedText(String _name, String input, String expected, int maxWidth) {
        WrappedText wrapped = new WrappedText(input, maxWidth);
        Assertions.assertEquals(expected, wrappedToString(wrapped));
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 0, 1})
    void tooSmallMaxWidth(int maxWidth) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new WrappedText("Hello", maxWidth));
    }

    /**
     * Converts a {@link WrappedText} to a string, where each line is separated by a LF character
     * ('\n').
     *
     * @param wrapped The text to be converted.
     * @return The converted text.
     */
    private static String wrappedToString(WrappedText wrapped) {
        StringBuilder builder = new StringBuilder();

        Iterator<String> it = wrapped.iterator();

        if (it.hasNext()) {
            builder.append(it.next());
        }

        while (it.hasNext()) {
            builder.append('\n');
            builder.append(it.next());
        }

        return builder.toString();
    }
}
