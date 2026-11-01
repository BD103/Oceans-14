package edu.miamioh.cse201.oceans14.ui;

import edu.miamioh.cse201.oceans14.engine.Dialogue;
import edu.miamioh.cse201.oceans14.ui.views.RootView;
import edu.miamioh.cse201.oceans14.ui.input.CommandBuffer;
import edu.miamioh.cse201.oceans14.ui.input.Input;
import edu.miamioh.cse201.oceans14.ui.input.InputParser;
import org.jline.terminal.Attributes;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp.Capability;

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

    private final InputParser parser;
    private final CommandBuffer commandBuffer = new CommandBuffer();

    private final DialogueEntries dialogue = new DialogueEntries();

    private final RootView rootView = new RootView(this.commandBuffer, this.dialogue);

    /**
     * Set to true when the terminal is resized, signals that the UI should be redrawn.
     */
    private boolean resizeRequested = false;
    private boolean drawRequested = false;
    private boolean exitRequested = false;

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

        // Put the terminal in application mode. This ensures that both Windows and Unix input
        // arrow keys using the same format (`\x1bOC` instead of `\x1b[C`, for example).
        this.term.puts(Capability.keypad_xmit);

        // Configure the root view (and all of its children) with the terminal's current size.
        this.resize();

        // Track when the terminal is resized, so that the viewports can be recomputed next draw.
        this.term.handle(Terminal.Signal.WINCH, signal -> this.resizeRequested = true);

        // Create a new parser to read terminal input.
        this.parser = new InputParser(term.reader());
    }

    /**
     * Runs the user interface, and the game by consequence.
     * <p>
     * This function is blocking, and will not return until the user quits the game.
     *
     * @throws IOException If trying to read input results in an error.
     */
    public void run() throws IOException {
        // Draw the UI for the first time.
        this.draw();

        while (true) {
            this.processInput();

            if (this.exitRequested) {
                break;
            }

            // When the terminal is resized, recompute viewports.
            if (this.resizeRequested) {
                this.resize();

                // Recomputing viewports requires a redraw afterward.
                this.drawRequested = true;

                this.resizeRequested = false;
            }

            if (this.drawRequested) {
                this.draw();
                this.drawRequested = false;
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
        // Exit out of application mode.
        this.term.puts(Capability.keypad_local);

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
    private void resize() {
        Size size = this.term.getSize();
        this.rootView.resize(new Rect(0, 0, size.getColumns(), size.getRows()));
    }

    /**
     * Draws the user interface to the terminal.
     */
    private void draw() {
        this.term.puts(Capability.clear_screen);
        this.rootView.draw(this.term);
        this.term.flush();
    }

    /**
     * Reads the user input, updating the {@link CommandBuffer} and requesting a re-draw as
     * necessary.
     *
     * @throws IOException If reading from the terminal fails.
     */
    private void processInput() throws IOException {
        Input input = parser.readAndParse();

        // If no input, do nothing.
        if (input == null) {
            return;
        }

        // If a plain character, write it.
        if (input instanceof Input.Character inputCharacter) {
            this.commandBuffer.insert(inputCharacter.get());

            // Changing command buffer requires `CommandView` to be redrawn.
            this.drawRequested = true;

            return;
        }

        // If a special input, process it.
        if (input instanceof Input.Special inputSpecial) {
            switch (inputSpecial) {
                case ENTER -> {
                    // TODO: sent buffer's contents to command parsing system
                    this.commandBuffer.clear();
                    this.drawRequested = true;
                }
                case DELETE -> {
                    this.commandBuffer.delete();
                    this.drawRequested = true;
                }
                case LEFT -> {
                    this.commandBuffer.left();
                    this.drawRequested = true;
                }
                case RIGHT -> {
                    this.commandBuffer.right();
                    this.drawRequested = true;
                }
                case EXIT -> this.exitRequested = true;
            }
        }
    }
}
