package edu.miamioh.cse201.oceans14.ui.views;

import edu.miamioh.cse201.oceans14.ui.Rect;
import edu.miamioh.cse201.oceans14.ui.View;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;

import java.io.PrintWriter;

/**
 * The stats view, responsible for displaying player stats and inventory.
 */
public class StatsView implements View {
    private Rect viewport;

    private final static String TITLE = "Ocean's 14";

    @Override
    public void draw(Terminal term) {
        PrintWriter writer = term.writer();

        term.puts(Capability.cursor_address, this.viewport.y(), (this.viewport.width() / 2) - (TITLE.length() / 2) + this.viewport.x());
        writer.print(TITLE);

        term.puts(Capability.cursor_address, this.viewport.y() + 2, this.viewport.x());
        writer.print("Cash: $0 :(");

        term.puts(Capability.cursor_address, this.viewport.y() + 4, this.viewport.x());
        writer.print("Inventory:");
        term.puts(Capability.cursor_address, this.viewport.y() + 5, this.viewport.x());
        writer.print("- 1 Screwdriver");
        term.puts(Capability.cursor_address, this.viewport.y() + 6, this.viewport.x());
        writer.print("- 1 Lucky Rock");
    }

    @Override
    public void computeViewport(Rect parentViewport) {
        int x = parentViewport.width() * 2 / 3 + 3;
        int y = parentViewport.y() + 1;
        int width = parentViewport.width() - x - 3;
        int height = parentViewport.height() - 4;

        this.viewport = new Rect(x, y, width, height);
    }
}
