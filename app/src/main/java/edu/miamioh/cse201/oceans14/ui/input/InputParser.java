package edu.miamioh.cse201.oceans14.ui.input;

import org.jline.utils.NonBlockingReader;

import java.io.IOException;

/**
 * Reads input from the terminal and parses it into an easily-processed form.
 *
 * @see Input
 */
public class InputParser {
    /** The time in milliseconds the parser should block while reading the terminal. */
    private static final long READ_TIMEOUT = 10;

    /** The terminal reader. */
    private final NonBlockingReader reader;

    public InputParser(NonBlockingReader reader) {
        this.reader = reader;
    }

    /**
     * Reads and parses input from the terminal, returning the result.
     *
     * @return The parsed input, null if reading timed out or the input could not be parsed.
     * @throws IOException If reading from the terminal fails.
     */
    public Input parse() throws IOException {
        int input = reader.read(READ_TIMEOUT);

        switch (input) {
            // Timed out
            case -2:
                return null;
            // CR (Carriage return)
            case 13:
                return Input.Special.ENTER;
            // DEL (Delete)
            case 127:
                return Input.Special.DELETE;
            // EOF or Ctrl+C
            case -1:
            case 3:
                return Input.Special.EXIT;
            // ESC (Escape)
            case 27:
                return this.parseESC();
            default:
                // If within Unicode's basic Latin block (excluding C0 controls), return the raw
                // character. See <https://en.wikipedia.org/wiki/Basic_Latin_(Unicode_block)>.
                if (input >= 32 && input <= 126) {
                    return new Input.Character((char)input);
                } else {
                    return null;
                }
        }
    }

    /**
     * Parses the sequence after an ESC (Escape, 127) is read.
     */
    private Input parseESC() throws IOException {
        int input = reader.read(READ_TIMEOUT);

        if (input == '[') {
            return this.parseCSI();
        } else {
            return null;
        }
    }

    /**
     * Parses the sequence after a CSI (Control Sequence Introducer, `ESC [`) is read.
     *
     * @see <a href="https://ghostty.org/docs/vt/concepts/sequences#csi">Control Sequences - CSI</a>
     */
    private Input parseCSI() throws IOException {
        int input = reader.read(READ_TIMEOUT);

        return switch (input) {
            // ESC [ C
            case 'C' -> Input.Special.RIGHT;
            // ESC [ D
            case 'D' -> Input.Special.LEFT;
            default -> null;
        };
    }
}
