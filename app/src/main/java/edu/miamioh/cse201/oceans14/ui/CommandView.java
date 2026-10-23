package edu.miamioh.cse201.oceans14.ui;

import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;

/**
 * The command view, an input box where the player types commands.
 */
public class CommandView implements View {
    private Rect viewport;

    @Override
    public void draw(Terminal term) {
        term.puts(Capability.cursor_address, this.viewport.y(), this.viewport.x());
        term.writer().print("> Command...");
    }

    @Override
    public void computeViewport(Rect parentViewport) {
        int x = parentViewport.x() + 3;
        int y = parentViewport.y() + parentViewport.height() - 2;
        int width = parentViewport.width() - 6;
        int height = 1;

        this.viewport = new Rect(x, y, width, height);
    }
}
