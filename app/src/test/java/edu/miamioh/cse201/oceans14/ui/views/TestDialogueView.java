package edu.miamioh.cse201.oceans14.ui.views;

import edu.miamioh.cse201.oceans14.ui.Rect;
import edu.miamioh.cse201.oceans14.ui.TermSnapshot;
import edu.miamioh.cse201.oceans14.ui.TermSnapshotExtension;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(TermSnapshotExtension.class)
public class TestDialogueView {
    @Test
    void draw(@TermSnapshot(width = 50, height = 20) Terminal term) {
        Size size = term.getSize();
        Rect viewport = new Rect(0, 0, size.getColumns(), size.getRows());

        DialogueView view = new DialogueView();
        view.resize(viewport);
        view.draw(term);
    }

    @Test
    void drawTooSmall(@TermSnapshot(width = 25, height = 8) Terminal term) {
        Size size = term.getSize();
        Rect viewport = new Rect(0, 0, size.getColumns(), size.getRows());

        DialogueView view = new DialogueView();
        view.resize(viewport);
        view.draw(term);
    }
}
