package edu.miamioh.cse201.oceans14.ui.views;

import edu.miamioh.cse201.oceans14.ui.*;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;

/**
 * The command view, an input box where the player types commands.
 */
public class CommandView implements View {
    private Rect viewport;
    // TODO: Replace this sample input with actual user-typed input.
    private final OverflowText input = new OverflowText("> Enter a command here", 20);

    @Override
    public void draw(Terminal term) {
        term.puts(Capability.cursor_address, this.viewport.y(), this.viewport.x());
        term.writer().print(this.input);
    }

    @Override
    public void computeViewport(Rect parentViewport) throws ViewportTooSmallException {
        this.viewport = parentViewport.splitVertical(parentViewport.height() - 3)[1]
                .marginHorizontal(3)
                .marginVertical(1);

        this.input.resize(this.viewport.width());
    }
}
