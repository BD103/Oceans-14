package edu.miamioh.cse201.oceans14.ui;

import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TermSnapshotExtension.class)
public class TestViewDebugger {
    @Test
    void fullscreen(@TermSnapshot Terminal term) {
        Size size = term.getSize();
        Rect viewport = new Rect(0, 0, size.getColumns(), size.getRows());
        ViewDebugger.fillViewport(term, viewport);
    }

    @Test
    void dividedScreen(@TermSnapshot(width = 100, height = 30) Terminal term) {
        ViewDebugger.fillViewport(term, new Rect(0, 0, 50, 50));
        ViewDebugger.fillViewport(term, new Rect(50, 0, 50, 20));
        ViewDebugger.fillViewport(term, new Rect(50, 20, 50, 10));
    }
}
