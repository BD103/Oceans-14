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

    private final DialogueView dialogueView = new DialogueView();
    private final StatsView statsView = new StatsView();
    private final CommandView commandView = new CommandView();

    public RootView() {}

    @Override
    public void draw(Terminal term) {
        PrintWriter writer = term.writer();

        term.puts(Capability.clear_screen);

        term.puts(Capability.cursor_address, this.viewport.y(), this.viewport.x());
        writer.print('+');
        for (int i = 0; i < this.viewport.width() - 2; i++) writer.print('-');
        writer.print('+');

        for (int y = this.viewport.y() + 1; y < this.viewport.y() + this.viewport.height() - 1; y++) {
            term.puts(Capability.cursor_address, y, this.viewport.x());
            writer.print('|');
            term.puts(Capability.cursor_address, y, this.viewport.x() + this.viewport.width() - 1);
            writer.print('|');
        }

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
