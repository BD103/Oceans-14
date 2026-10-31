package edu.miamioh.cse201.oceans14.ui.views;

import edu.miamioh.cse201.oceans14.ui.Rect;
import edu.miamioh.cse201.oceans14.ui.TermSnapshot;
import edu.miamioh.cse201.oceans14.ui.TermSnapshotExtension;
import edu.miamioh.cse201.oceans14.ui.input.CommandBuffer;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TermSnapshotExtension.class)
public class TestCommandView {
    private Terminal term;
    private Rect viewport;
    private CommandBuffer buffer;

    @BeforeEach
    void beforeEach(@TermSnapshot(height = 1) Terminal term) {
        this.term = term;

        Size size = term.getSize();
        this.viewport = new Rect(0, 0, size.getColumns(), size.getRows());

        this.buffer = new CommandBuffer();
    }

    @Test
    void drawEmpty() {
        CommandView view = new CommandView(this.buffer);
        view.resize(this.viewport);
        view.draw(this.term);
    }

    @Test
    void drawTooSmall() {
        this.term.setSize(Size.of(10, 2));
        this.viewport = new Rect(0, 0, 10, 2);

        CommandView view = new CommandView(this.buffer);
        view.resize(this.viewport);

        for (char c : "Line toooooo long".toCharArray()) {
            this.buffer.insert(c);
        }

        view.draw(this.term);
    }

    @Test
    void drawText() {
        CommandView view = new CommandView(this.buffer);
        view.resize(this.viewport);

        for (char c : "Hello, world!".toCharArray()) {
            this.buffer.insert(c);
        }

        view.draw(this.term);
    }

    @Test
    void drawMoveCursor() {
        CommandView view = new CommandView(this.buffer);
        view.resize(this.viewport);

        for (char c : "Colourful".toCharArray()) {
            this.buffer.insert(c);
        }

        for (int i = 0; i < 4; i++) {
            this.buffer.left();
        }

        this.buffer.delete();

        view.draw(this.term);
    }
}
