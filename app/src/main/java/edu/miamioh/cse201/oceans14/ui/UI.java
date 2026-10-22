package edu.miamioh.cse201.oceans14.ui;

import org.jline.terminal.Attributes;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;
import org.jline.utils.NonBlockingReader;

import java.io.IOException;

/**
 * Manages the user interface.
 * <p>
 * This class is indirectly responsible for drawing to the screen, reading user input, and
 * interfacing with the game engine.
 */
public class UI implements AutoCloseable {
    private final Terminal term;
    private final Attributes originalAttributes;
    private final RootView rootView;

    public UI() throws IOException {
        this.term = TerminalBuilder.builder().system(true).devTty(true).build();

        // Enter raw terminal mode. This lets us read each character as it is typed, as compared to
        // reading only after "Enter" is pressed. It also prevents key presses from being echoed to
        // the terminal.
        this.originalAttributes = this.term.enterRawMode();

        // Switch to the alternate terminal screen, a blank screen without any scrollback.
        this.term.puts(InfoCmp.Capability.enter_ca_mode);

        Size size = this.term.getSize();
        Rect screenViewport = new Rect(0, 0, size.getColumns(), size.getRows());

        this.rootView = new RootView();
        this.rootView.computeViewport(screenViewport);
    }

    public void run() throws IOException {
        this.rootView.draw(this.term);
        this.term.flush();

        NonBlockingReader reader = term.reader();

        while (reader.read() != 'q') {
            // TODO: update screen
            Thread.yield();
        }
    }

    @Override
    public void close() throws IOException {
        // Exit the alternate screen, restore the original terminal screen.
        this.term.puts(InfoCmp.Capability.exit_ca_mode);

        // Exit out of raw terminal mode, returning to "cooked" mode.
        this.term.setAttributes(this.originalAttributes);

        this.term.close();
    }
}
