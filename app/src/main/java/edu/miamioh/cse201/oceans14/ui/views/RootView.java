package edu.miamioh.cse201.oceans14.ui.views;

import edu.miamioh.cse201.oceans14.ui.Rect;
import edu.miamioh.cse201.oceans14.ui.View;
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
    private int dialogue_stats_divider;

    // Child views
    private final DialogueView dialogueView = new DialogueView();
    private final StatsView statsView = new StatsView();
    private final CommandView commandView = new CommandView();

    @Override
    public void draw(Terminal term) {
        PrintWriter writer = term.writer();

        // Draw the top horizontal border
        term.puts(Capability.cursor_address, this.viewport.y(), this.viewport.x());
        writer.print('+');
        for (int x = this.viewport.x() + 1; x < this.viewport.x() + this.viewport.width() - 1; x++) {
            if (x == this.dialogue_stats_divider) {
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
            term.puts(Capability.cursor_address, y, this.dialogue_stats_divider);
            writer.print('|');
            term.puts(Capability.cursor_address, y, this.viewport.x() + this.viewport.width() - 1);
            writer.print('|');
        }

        // Draw the horizontal border separating the command view from the other two views
        term.puts(Capability.cursor_address, this.viewport.y() + this.viewport.height() - 3, this.viewport.x());
        writer.print('+');
        for (int x = this.viewport.x() + 1; x < this.viewport.x() + this.viewport.width() - 1; x++) {
            if (x == this.dialogue_stats_divider) {
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
    public void resize(Rect viewport) {
        this.viewport = viewport;

        // Add margin for the border.
        Rect inner = this.viewport.margin(1);

        // `CommandView` is at the very bottom of the screen, spanning the width.
        Rect commandViewport = inner.marginTop(inner.height() - 1).marginHorizontal(2);

        // Excluding where the `CommandView` goes, create viewports for `DialogueView` and
        // `StatsView` by splitting the screen by two-thirds.
        Rect[] dialogueAndStatsViewports = inner.marginBottom(2).splitHorizontal(2.0 / 3.0);

        // Mark the x-coordinate of the divider between `DialogueView` and `StatsView`.
        this.dialogue_stats_divider = dialogueAndStatsViewports[0].x() + dialogueAndStatsViewports[0].width();

        Rect dialogueViewport = dialogueAndStatsViewports[0].marginHorizontal(2);
        Rect statsViewport = dialogueAndStatsViewports[1].marginHorizontal(2);

        this.dialogueView.resize(dialogueViewport);
        this.statsView.resize(statsViewport);
        this.commandView.resize(commandViewport);
    }
}
