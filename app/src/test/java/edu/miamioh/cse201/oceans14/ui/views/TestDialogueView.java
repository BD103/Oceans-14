package edu.miamioh.cse201.oceans14.ui.views;

import edu.miamioh.cse201.oceans14.engine.Dialogue;
import edu.miamioh.cse201.oceans14.ui.DialogueEntries;
import edu.miamioh.cse201.oceans14.ui.Rect;
import edu.miamioh.cse201.oceans14.ui.TermSnapshot;
import edu.miamioh.cse201.oceans14.ui.TermSnapshotExtension;
import org.jline.terminal.Size;
import org.jline.terminal.Terminal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@ExtendWith(TermSnapshotExtension.class)
public class TestDialogueView {
    private Terminal term;
    private Rect viewport;
    private DialogueEntries dialogue;
    private DialogueView view;

    @BeforeEach
    void beforeEach(@TermSnapshot(width = 50, height = 20) Terminal term) {
        this.term = term;

        Size size = this.term.getSize();
        this.viewport = new Rect(0, 0, size.getColumns(), size.getRows());

        this.dialogue = new DialogueEntries();

        this.view = new DialogueView(this.dialogue);
        this.view.resize(this.viewport);
    }

    @Test
    void drawEmpty() {
        this.view.draw(this.term);
    }

    @Test
    void draw() {
        this.dialogue.add(new Dialogue.System("System message"));
        this.dialogue.add(new Dialogue.Actor("Anthony", "Yo, what's up?"));
        this.dialogue.add(new Dialogue.Error("Uh oh!"));

        this.view.draw(this.term);
    }

    @Test
    void drawList() {
        this.dialogue.add(new Dialogue.List(List.of(
                new Dialogue.System("You walk over to the maintenance worker."),
                new Dialogue.Actor("Worker", "Hey, how can I help you?")
        )));

        this.view.draw(this.term);
    }

    @Test
    void drawWrapping() {
        this.term.setSize(Size.of(25, 20));
        this.viewport = new Rect(0, 0, 25, 20);
        this.view.resize(this.viewport);

        this.dialogue.add(new Dialogue.System("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Fusce fringilla lorem non dapibus dictum."));
        this.dialogue.add(new Dialogue.Actor("Nayrb", "Aenean pellentesque bibendum fringilla. Aenean quis fringilla lectus. Donec vestibulum fringilla mi, id scelerisque mauris accumsan id."));
        this.dialogue.add(new Dialogue.System("THISLINEISSOLONGTHATITNEEDSTOBEHARDWRAPPED"));

        this.view.draw(term);
    }

    @Test
    void drawOverflow() {
        this.term.setSize(Size.of(50, 8));
        this.viewport = new Rect(0, 0, 50, 8);
        this.view.resize(this.viewport);

        for (int i = 0; i < 10; i++) {
            this.dialogue.add(new Dialogue.System("This is line " + i + " of 9"));
        }

        this.view.draw(this.term);
    }
}
