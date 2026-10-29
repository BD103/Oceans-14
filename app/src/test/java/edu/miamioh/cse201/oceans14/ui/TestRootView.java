package edu.miamioh.cse201.oceans14.ui;

import edu.miamioh.cse201.oceans14.ui.input.CommandBuffer;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TermSnapshotExtension.class)
public class TestRootView {
    @Test
    void draw(@TermSnapshot Terminal term) {
        Size size = term.getSize();
        Rect parentViewport = new Rect(0, 0, size.getColumns(), size.getRows());

        RootView view = new RootView(new CommandBuffer());
        view.computeViewport(parentViewport);
        view.draw(term);
    }

    @Test
    void drawTooSmall(@TermSnapshot(width = 46, height = 10) Terminal term) {
        Size size = term.getSize();
        Rect parentViewport = new Rect(0, 0, size.getColumns(), size.getRows());

        RootView view = new RootView(new CommandBuffer());
        view.computeViewport(parentViewport);
        view.draw(term);
    }
}
