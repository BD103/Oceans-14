package edu.miamioh.cse201.oceans14.ui.views;

import edu.miamioh.cse201.oceans14.engine.GameEngine;
import edu.miamioh.cse201.oceans14.engine.GameWorld;
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
    private CommandView commandView;

    @BeforeEach
    void beforeEach(@TermSnapshot(height = 1) Terminal term) {
        this.term = term;

        Size size = term.getSize();
        this.viewport = new Rect(0, 0, size.getColumns(), size.getRows());

        this.buffer = new CommandBuffer();

        this.commandView = new CommandView(this.buffer, new GameEngine(GameWorld.getRoom("entrance_room")));
        this.commandView.resize(this.viewport);
    }

    @Test
    void drawEmpty() {
        this.commandView.draw(this.term);
    }

    @Test
    void drawTooSmall() {
        this.term.setSize(Size.of(10, 2));
        this.viewport = new Rect(0, 0, 10, 2);
        this.commandView.resize(this.viewport);

        this.buffer.insert("Line toooooo long");

        this.commandView.draw(this.term);
    }

    @Test
    void drawText() {
        this.buffer.insert("Hello, world!");
        this.commandView.draw(this.term);
    }

    @Test
    void drawMoveCursor() {
        this.buffer.insert("Colourful");

        for (int i = 0; i < 4; i++) {
            this.buffer.left();
        }

        this.buffer.delete();

        this.commandView.draw(this.term);
    }

    @Test
    void drawAutoComplete() {
        // Should auto-complete to "enter_casino".
        this.buffer.insert("enter");
        this.commandView.draw(this.term);
    }
}
