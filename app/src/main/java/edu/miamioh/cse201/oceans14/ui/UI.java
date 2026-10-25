package edu.miamioh.cse201.oceans14.ui;

import edu.miamioh.cse201.oceans14.ui.views.RootView;
import org.jline.terminal.Attributes;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp.Capability;
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

    /**
     * The terminal's attributes before we switched to raw mode, used to restore "cooked" mode when
     * the program exits.
     */
    private final Attributes originalAttributes;

    private final RootView rootView = new RootView();

    /** Set to true when the terminal is resized, signals that the UI should be redrawn. */
    private boolean terminalResized = false;

    /** Set to true when computing viewports fails with {@link ViewportTooSmallException}. */
    private boolean terminalTooSmall = false;

    public UI() throws IOException {
        // Connect to this program's system terminal. `devTty` lets us still draw to the terminal
        // on POSIX systems, even when this program's stdin / stdout is piped.
        this.term = TerminalBuilder.builder().system(true).devTty(true).build();

        // Enter raw terminal mode. This lets us read each character as it is typed, as compared to
        // reading only after "Enter" is pressed. It also prevents key presses from being echoed to
        // the terminal.
        this.originalAttributes = this.term.enterRawMode();

        // Switch to the alternate terminal screen, an empty screen we can draw to without messing
        // up the user's main terminal history.
        this.term.puts(Capability.enter_ca_mode);

        // Configure the root view (and all of its children) with the terminal's current size.
        this.computeViewport();

        // Track when the terminal is resized, so that the viewports can be recomputed next draw.
        this.term.handle(Terminal.Signal.WINCH, signal -> this.terminalResized = true);
    }

    /**
     * Runs the user interface, and the game by consequence.
     * <p>
     * This function is blocking, and will not return until the user quits the game.
     *
     * @throws IOException If trying to read input results in an error.
     */
    public void run() throws IOException {
        NonBlockingReader reader = term.reader();

        // Draw the UI for the first time.
        this.draw();

        while (reader.read(10) != 'q') {
            // When the terminal is resized, recompute viewports and redraw.
            if (this.terminalResized) {
                this.computeViewport();
                this.draw();
                this.terminalResized = false;
            }
        }
    }

    /**
     * Closes resources opened by this class and returns the screen to a normal state.
     *
     * @throws IOException If closing the terminal fails.
     */
    @Override
    public void close() throws IOException {
        // Exit the alternate screen, restore the original terminal screen.
        this.term.puts(Capability.exit_ca_mode);

        // Exit out of raw terminal mode, returning to "cooked" mode.
        this.term.setAttributes(this.originalAttributes);

        this.term.close();
    }

    /**
     * Computes the viewports of all views based on the terminal's current size.
     * <p>
     * This should be called before the root view is drawn for the first time, as well as whenever
     * the terminal changes sizes.
     */
    private void computeViewport() {
        try {
            Size size = this.term.getSize();
            this.rootView.computeViewport(new Rect(0, 0, size.getColumns(), size.getRows()));
            this.terminalTooSmall = false;
        } catch (ViewportTooSmallException e) {
            this.terminalTooSmall = true;
        }
    }

    /**
     * Draws the user interface to the terminal.
     */
    private void draw() {
        this.term.puts(Capability.clear_screen);

        if (this.terminalTooSmall) {
            this.term.writer().print("\033[31mThe terminal is too small to display the game, please increase its size.\033[0m");
        } else {
            this.rootView.draw(this.term);
        }

        this.term.flush();
    }
}
