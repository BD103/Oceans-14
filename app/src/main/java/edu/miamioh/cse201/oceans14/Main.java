package edu.miamioh.cse201.oceans14;

import org.jline.terminal.Attributes;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp.Capability;
import org.jline.utils.NonBlockingReader;

import java.io.IOException;
import java.io.PrintWriter;

public class Main {
    public static void main(String[] args) throws IOException {
        try (Terminal term = TerminalBuilder.builder().system(true).devTty(true).build()) {
            Attributes originalAttributes = null;

            try {
                // Enter raw terminal mode. This lets us read each character as it is typed, as
                // compared to reading only after "Enter" is pressed. It also prevents key presses
                // from being echoed to the terminal.
                originalAttributes = term.enterRawMode();
                // Switch to the alternate terminal screen, a blank screen without any scrollback.
                term.puts(Capability.enter_ca_mode);

                PrintWriter writer = term.writer();
                NonBlockingReader reader = term.reader();

                // Move to the top-left of the screen.
                term.puts(Capability.cursor_address, 0, 0);
                writer.println("Hello, world!");
                writer.flush();

                // Read input, exit program when "q" is pressed.
                int i;
                while ((i = reader.read()) != 'q') {
                    writer.println("Typed: " + (char)i + " (" + i + ")");
                    writer.flush();
                }
            } finally {
                // Exit the alternate screen, restore the original terminal screen.
                term.puts(Capability.exit_ca_mode);

                // Exit out of raw terminal mode, returning to "cooked" mode.
                if (originalAttributes != null)
                    term.setAttributes(originalAttributes);
            }
        }
    }
}
