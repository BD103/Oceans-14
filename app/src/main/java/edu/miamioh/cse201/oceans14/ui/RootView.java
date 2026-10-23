package edu.miamioh.cse201.oceans14.ui;

import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;

import java.io.PrintWriter;

/**
 * The root view of the program.
 * <p>
 * This view is a container for every other UI element in the application, and its viewport
 * stretches to encompass the entire screen.
 */
public class RootView implements View {
    private Rect viewport;

    // Child views
    private final DialogueView dialogueView = new DialogueView();
    private final StatsView statsView = new StatsView();
    private final CommandView commandView = new CommandView();

    public RootView() {}

    @Override
    public void draw(Terminal term) {
        PrintWriter writer = term.writer();

        term.puts(Capability.clear_screen);

        int dialogue_stats_divider = this.viewport.width() * 2 / 3 + this.viewport.x();

        // Draw the top horizontal border
        term.puts(Capability.cursor_address, this.viewport.y(), this.viewport.x());
        writer.print('+');
        for (int x = this.viewport.x() + 1; x < this.viewport.x() + this.viewport.width() - 1; x++) {
            if (x == dialogue_stats_divider) {
                writer.print('+');
            } else {
                writer.print('-');
            }
        }
        writer.print('+');

        // Draw the left, middle, and right vertical borders surrounding the dialogue and stats views
        for (int y = this.viewport.y() + 1; y < this.viewport.y() + this.viewport.height() - 3; y++) {
            term.puts(Capability.cursor_address, y, this.viewport.x());
            writer.print('|');
            term.puts(Capability.cursor_address, y, dialogue_stats_divider);
            writer.print('|');
            term.puts(Capability.cursor_address, y, this.viewport.x() + this.viewport.width() - 1);
            writer.print('|');
        }

        // Draw the horizontal border separating the command view from the other two views
        term.puts(Capability.cursor_address, this.viewport.y() + this.viewport.height() - 3, this.viewport.x());
        writer.print('+');
        for (int x = this.viewport.x() + 1; x < this.viewport.x() + this.viewport.width() - 1; x++) {
            if (x == dialogue_stats_divider) {
                writer.print('+');
            } else {
                writer.print('-');
            }
        }
        writer.print('+');

        // Draw the left and right vertical borders surrounding the command view
        term.puts(Capability.cursor_address, this.viewport.y() + this.viewport.height() - 2, this.viewport.x());
        writer.print('|');
        term.puts(Capability.cursor_address, this.viewport.y() + this.viewport.height() - 2, this.viewport.x() + this.viewport.width() - 1);
        writer.print('|');

        // Draw the bottom horizontal border
        term.puts(Capability.cursor_address, this.viewport.y() + this.viewport.height() - 1, this.viewport.x());
        writer.print('+');
        for (int i = 0; i < this.viewport.width() - 2; i++) writer.print('-');
        writer.print('+');

        this.dialogueView.draw(term);
        this.statsView.draw(term);
        this.commandView.draw(term);
    }

    @Override
    public void computeViewport(Rect screenViewport) {
        // This view expands to the entire screen, so we don't modify anything.
        this.viewport = screenViewport;

        this.dialogueView.computeViewport(this.viewport);
        this.statsView.computeViewport(this.viewport);
        this.commandView.computeViewport(this.viewport);
    }
}
