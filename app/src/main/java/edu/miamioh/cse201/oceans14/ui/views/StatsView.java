package edu.miamioh.cse201.oceans14.ui.views;

import edu.miamioh.cse201.oceans14.ui.*;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;

import java.io.PrintWriter;
import java.util.List;

/**
 * The stats view, responsible for displaying player stats and inventory.
 */
public class StatsView implements View {
    private Rect viewport;

    private final OverflowText title = new OverflowText("Ocean's 14", 20);
    private final OverflowText cash = new OverflowText("Cash: $0 :(", 20);
    private final OverflowText inventory = new OverflowText("Inventory:", 20);
    private final List<OverflowText> inventoryList = List.of(
            new OverflowText("- 1 Screwdriver", 20),
            new OverflowText("- 1 Lucky Rock", 20)
    );

    @Override
    public void draw(Terminal term) {
        PrintWriter writer = term.writer();

        term.puts(Capability.cursor_address, this.viewport.y(), (this.viewport.width() / 2) - (title.toString().length() / 2) + this.viewport.x());
        writer.print(title);

        term.puts(Capability.cursor_address, this.viewport.y() + 2, this.viewport.x());
        writer.print(cash);

        term.puts(Capability.cursor_address, this.viewport.y() + 4, this.viewport.x());
        writer.print(inventory);

        int i = 0;
        for (int y = this.viewport.y() + 5; i < inventoryList.size() && y < this.viewport.y() + this.viewport.width(); i++, y++) {
            term.puts(Capability.cursor_address, y, this.viewport.x());
            writer.print(inventoryList.get(i));
        }
    }

    @Override
    public void computeViewport(Rect parentViewport) throws ViewportTooSmallException {
        this.viewport = parentViewport.splitHorizontal(2.0 / 3.0)[1]
                .splitVertical(parentViewport.height() - 3)[0]
                .marginHorizontal(3)
                .marginVertical(1);

        this.title.resize(this.viewport.width());
        this.cash.resize(this.viewport.width());
        this.inventory.resize(this.viewport.width());
        this.inventoryList.forEach(t -> t.resize(this.viewport.width()));
    }
}
