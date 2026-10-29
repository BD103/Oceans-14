package edu.miamioh.cse201.oceans14.ui.input;

import org.jline.utils.NonBlockingReader;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Objects;
import java.util.Queue;

public class TestInputParser {
    private MockReader reader;
    private InputParser parser;

    @BeforeEach
    void beforeEach() {
        this.reader = new MockReader();
        this.parser = new InputParser(this.reader);
    }

    @AfterEach
    void afterEach() throws IOException {
        this.reader.close();
    }

    @Test
    void empty() throws IOException {
        Assertions.assertNull(this.parser.readAndParse());
    }

    @Test
    void newline() throws IOException {
        // When in terminal raw mode, pressing "Enter" enters a single carriage return. It doesn't
        // emit a line feed.
        reader.write("\r");
        Assertions.assertEquals(Input.Special.ENTER, this.parser.readAndParse());
    }

    @Test
    void backspaceWindows() throws IOException {
        // BS (Backspace) character, used on Windows.
        reader.write(8);
        Assertions.assertEquals(Input.Special.DELETE, this.parser.readAndParse());
    }

    @Test
    void backspaceUnix() throws IOException {
        // DEL (Delete) character, used on Linux and MacOS.
        reader.write(127);
        Assertions.assertEquals(Input.Special.DELETE, this.parser.readAndParse());
    }

    @Test
    void ctrlC() throws IOException {
        reader.write(3);
        Assertions.assertEquals(Input.Special.EXIT, this.parser.readAndParse());
    }

    @ParameterizedTest
    @ValueSource(chars = {' ', '!', '(', '0', '9', ':', '@', 'A', 'Z', '[', 'a', 'y', 'z', '|', '~'})
    void basicLatin(char c) throws IOException {
        reader.write(c);
        Assertions.assertEquals(new Input.Character(c), this.parser.readAndParse());
    }

    @Test
    void invalidESC() throws IOException {
        reader.write("\033[");
        Assertions.assertNull(this.parser.readAndParse());
    }

    @Test
    void right() throws IOException {
        reader.write("\033OC");
        Assertions.assertEquals(Input.Special.RIGHT, this.parser.readAndParse());
    }

    @Test
    void left() throws IOException {
        reader.write("\033OD");
        Assertions.assertEquals(Input.Special.LEFT, this.parser.readAndParse());
    }

    /**
     * A {@link NonBlockingReader} that lets tests simulate terminal keypresses.
     */
    private static class MockReader extends NonBlockingReader {
        private final Queue<Integer> queue = new ArrayDeque<>();

        /**
         * Simulates typing the given string to the terminal.
         */
        public void write(String s) {
            for (char c : s.toCharArray()) {
                this.queue.add((int) c);
            }
        }

        /**
         * Simulates typing the given character to the terminal.
         */
        public void write(int c) {
            this.queue.add(c);
        }

        @Override
        protected int read(long timeout, boolean isPeek) {
            Integer response;

            if (isPeek) {
                response = queue.peek();
            } else {
                response = queue.poll();
            }

            // If the queue is empty, pretend we timed out and return -2.
            return Objects.requireNonNullElse(response, -2);
        }

        @Override
        public int readBuffered(char[] b, int off, int len, long timeout) {
            throw new UnsupportedOperationException();
        }
    }
}
