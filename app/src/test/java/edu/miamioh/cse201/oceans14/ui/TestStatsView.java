package edu.miamioh.cse201.oceans14.ui;

import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TermSnapshotExtension.class)
public class TestStatsView {
    @Test
    void draw(@TermSnapshot Terminal term) {
        Size size = term.getSize();
        Rect parentViewport = new Rect(0, 0, size.getColumns(), size.getRows());

        StatsView view = new StatsView();
        view.computeViewport(parentViewport);
        view.draw(term);
    }

    @Test
    void drawTooSmall(@TermSnapshot(width = 30, height = 8) Terminal term) {
        Size size = term.getSize();
        Rect parentViewport = new Rect(0, 0, size.getColumns(), size.getRows());

        StatsView view = new StatsView();
        view.computeViewport(parentViewport);
        view.draw(term);
    }
}
