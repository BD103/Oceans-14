package edu.miamioh.cse201.oceans14.ui;

import edu.miamioh.cse201.oceans14.engine.Dialogue;
import edu.miamioh.cse201.oceans14.ui.text.WrapParagraph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A list of dialogue entries, rendered for the user interface.
 */
public class DialogueEntries {
    private final List<WrapParagraph> entries = new ArrayList<>();
    private int maxWidth;

    /**
     * Adds a new dialogue entry to the list.
     *
     * @param dialogue The dialogue to add.
     */
    public void add(Dialogue dialogue) {
        if (dialogue instanceof  Dialogue.DialogueList list) {
            list.entries().forEach(this::add);
        } else {
            this.entries.add(new WrapParagraph(dialogue.display(), this.maxWidth));
        }
    }

    /**
     * @return The dialogue as a list of formatted paragraphs.
     */
    public List<WrapParagraph> paragraphs() {
        return Collections.unmodifiableList(this.entries);
    }

    /**
     * Resizes all dialogue entries to conform to a maximum width.
     *
     * @param maxWidth The new maximum width.
     */
    public void resize(int maxWidth) {
        this.maxWidth = maxWidth;
        this.entries.forEach(wp -> wp.resize(maxWidth));
    }

    /**
     * Calculates how many lines it would require to render all dialogue entries, including the
     * blank lines between entries.
     *
     * @return The number of lines.
     */
    public int height() {
        int height = 0;

        for (WrapParagraph wp : this.entries) {
            // Sum the number of lines of all paragraphs, including the blank lines in-between
            // entries.
            height += wp.getLines().size() + 1;
        }

        return height;
    }
}
