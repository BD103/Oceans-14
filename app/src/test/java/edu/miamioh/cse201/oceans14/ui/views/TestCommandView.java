package edu.miamioh.cse201.oceans14.ui.views;

import edu.miamioh.cse201.oceans14.ui.Rect;
import edu.miamioh.cse201.oceans14.ui.TermSnapshot;
import edu.miamioh.cse201.oceans14.ui.TermSnapshotExtension;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TermSnapshotExtension.class)
public class TestCommandView {
    @Test
    void draw(@TermSnapshot Terminal term) {
        Size size = term.getSize();
        Rect parentViewport = new Rect(0, 0, size.getColumns(), size.getRows());

        CommandView view = new CommandView();
        view.computeViewport(parentViewport);
        view.draw(term);
    }

    @Test
    void drawTooSmall(@TermSnapshot(width = 10, height = 2) Terminal term) {
        Size size = term.getSize();
        Rect parentViewport = new Rect(0, 0, size.getColumns(), size.getRows());

        CommandView view = new CommandView();
        view.computeViewport(parentViewport);
        view.draw(term);
    }
}
