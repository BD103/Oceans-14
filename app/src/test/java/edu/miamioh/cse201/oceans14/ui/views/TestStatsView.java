package edu.miamioh.cse201.oceans14.ui.views;

import edu.miamioh.cse201.oceans14.engine.GameEngine;
import edu.miamioh.cse201.oceans14.engine.GameWorld;
import edu.miamioh.cse201.oceans14.ui.DialogueEntries;
import edu.miamioh.cse201.oceans14.ui.Rect;
import edu.miamioh.cse201.oceans14.ui.TermSnapshot;
import edu.miamioh.cse201.oceans14.ui.TermSnapshotExtension;
import edu.miamioh.cse201.oceans14.ui.input.CommandBuffer;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TermSnapshotExtension.class)
public class TestStatsView {
    @Test
    void draw(@TermSnapshot(width = 30, height = 20) Terminal term) {
        Size size = term.getSize();
        Rect viewport = new Rect(0, 0, size.getColumns(), size.getRows());

        StatsView view = new StatsView(new GameEngine(GameWorld.getRoom("entrance_room")));
        view.resize(viewport);
        view.draw(term);
    }

    @Test
    void drawTooSmall(@TermSnapshot(width = 10, height = 8) Terminal term) {
        Size size = term.getSize();
        Rect viewport = new Rect(0, 0, size.getColumns(), size.getRows());

        StatsView view = new StatsView(new GameEngine(GameWorld.getRoom("entrance_room")));
        view.resize(viewport);
        view.draw(term);
    }
}
