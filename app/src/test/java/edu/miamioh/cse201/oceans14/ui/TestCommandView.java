package edu.miamioh.cse201.oceans14.ui;

import edu.miamioh.cse201.oceans14.ui.input.CommandBuffer;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;

@ExtendWith(TermSnapshotExtension.class)
public class TestCommandView {
    @Test
    void draw(@TermSnapshot Terminal term) {
        Size size = term.getSize();
        Rect parentViewport = new Rect(0, 0, size.getColumns(), size.getRows());

        CommandView view = new CommandView(new CommandBuffer());
        view.computeViewport(parentViewport);
        view.draw(term);
    }

    @Test
    void drawTooSmall(@TermSnapshot(width = 10, height = 2) Terminal term) {
        Size size = term.getSize();
        Rect parentViewport = new Rect(0, 0, size.getColumns(), size.getRows());

        CommandView view = new CommandView(new CommandBuffer());
        view.computeViewport(parentViewport);
        view.draw(term);
    }
}
