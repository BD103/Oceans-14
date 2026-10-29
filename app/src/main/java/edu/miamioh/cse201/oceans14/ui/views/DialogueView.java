package edu.miamioh.cse201.oceans14.ui.views;

import edu.miamioh.cse201.oceans14.ui.Rect;
import edu.miamioh.cse201.oceans14.ui.View;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import java.io.PrintWriter;

/**
 * The dialogue view, responsible for displaying current and past game dialogue.
 */
public class DialogueView implements View {
    private Rect viewport;

    @Override
    public void draw(Terminal term) {
        PrintWriter writer = term.writer();

        term.puts(InfoCmp.Capability.cursor_address, this.viewport.y(), this.viewport.x());
        writer.print("> talk to Anthony");

        term.puts(InfoCmp.Capability.cursor_address, this.viewport.y() + 2, this.viewport.x());
        writer.print("Anthony: Hey, how's it going?");

        for (int y = this.viewport.y(); y < this.viewport.y() + this.viewport.height(); y++) {
            term.puts(InfoCmp.Capability.cursor_address, y, this.viewport.x() + this.viewport.width() - 1);
            writer.print('@');
        }
    }

    @Override
    public void computeViewport(Rect parentViewport) {
        int x = parentViewport.x() + 3;
        int y = parentViewport.y() + 1;
        int width = parentViewport.width() * 2 / 3 - 5;
        int height = parentViewport.height() - 4;

        this.viewport = new Rect(x, y, width, height);
    }
}
