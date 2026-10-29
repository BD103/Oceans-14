package edu.miamioh.cse201.oceans14.ui.views;

import edu.miamioh.cse201.oceans14.ui.Rect;
import edu.miamioh.cse201.oceans14.ui.View;
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
    public void resize(Rect viewport) {
        this.viewport = viewport;
    }
}
