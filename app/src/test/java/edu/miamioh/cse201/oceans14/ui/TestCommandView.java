package edu.miamioh.cse201.oceans14.ui;

import edu.miamioh.cse201.oceans14.ui.input.CommandBuffer;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TermSnapshotExtension.class)
public class TestCommandView {
    @Test
    void drawEmpty(@TermSnapshot Terminal term) {
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

    @Test
    void drawText(@TermSnapshot Terminal term) {
        Size size = term.getSize();
        Rect parentViewport = new Rect(0, 0, size.getColumns(), size.getRows());
        CommandBuffer buffer = new CommandBuffer();

        CommandView view = new CommandView(buffer);
        view.computeViewport(parentViewport);

        for (char c : "Hello, world!".toCharArray()) {
            buffer.insert(c);
        }

        view.draw(term);
    }

    @Test
    void drawMoveCursor(@TermSnapshot Terminal term) {
        Size size = term.getSize();
        Rect parentViewport = new Rect(0, 0, size.getColumns(), size.getRows());
        CommandBuffer buffer = new CommandBuffer();

        CommandView view = new CommandView(buffer);
        view.computeViewport(parentViewport);

        for (char c : "Colourful".toCharArray()) {
            buffer.insert(c);
        }

        for (int i = 0; i < 4; i++) {
            buffer.left();
        }

        buffer.delete();

        view.draw(term);
    }
}
