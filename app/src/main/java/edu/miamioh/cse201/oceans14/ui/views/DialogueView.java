package edu.miamioh.cse201.oceans14.ui.views;

import edu.miamioh.cse201.oceans14.ui.Rect;
import edu.miamioh.cse201.oceans14.ui.View;
import edu.miamioh.cse201.oceans14.ui.text.WrapParagraph;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import java.io.PrintWriter;
import java.util.List;

/**
 * The dialogue view, responsible for displaying current and past game dialogue.
 */
public class DialogueView implements View {
    private Rect viewport;

    /** A sample list of dialogue entries. This will be replaced with a dynamic list eventually. */
    private final List<WrapParagraph> dialogueEntries = List.of(
            new WrapParagraph("Using the cloned keycard, you sneak into the staff room. Inside is a kitchenette, several tables and chairs, a wall of lockers, and a lone man sitting in the corner.", 40),
            new WrapParagraph("> inspect lockers", 40),
            new WrapParagraph("On the east wall lies dozens of lockers, all but two with locks on them.", 40),
            new WrapParagraph("> talk to man", 40),
            new WrapParagraph("Lone Man: Yo, are you new here? The name's Anthony, nice to meet-cha.", 40),
            new WrapParagraph("Anthony reaches out his hand, and you give him a handshake.", 40)
    );

    private int dialogueHeight;

    @Override
    public void draw(Terminal term) {
        // Do not draw anything if the viewport cannot display at least one line of content.
        if (this.viewport.height() < 1) {
            return;
        }

        drawParagraphs(term);
        drawScrollbar(term);
    }

    private void drawParagraphs(Terminal term) {
        PrintWriter writer = term.writer();

        // Tracks which line in the terminal we're writing to.
        int y = this.viewport.y() + this.viewport.height() - 1;

        // Loop over each dialogue entry backwards.
        outer:
        for (int p = dialogueEntries.size() - 1; p >= 0; p--) {
            List<String> lines = dialogueEntries.get(p).getLines();

            // Loop over each line in the paragraph backwards.
            for (int l = lines.size() - 1; l >= 0; l--) {
                // If there's no more room print lines, stop altogether.
                if (y < this.viewport.y()) {
                    break outer;
                }

                // Write the line to the screen.
                term.puts(InfoCmp.Capability.cursor_address, y, this.viewport.x());
                writer.print(lines.get(l));

                y--;
            }

            // Add an example blank line in between paragraphs.
            y--;
        }
    }

    private void drawScrollbar(Terminal term) {
        PrintWriter writer = term.writer();

        // Calculate at what y-coordinate the scrollbar should switch from indicating hidden
        // content to visible content.
        double percentHidden = Math.max(this.dialogueHeight - this.viewport.height(), 0) / (double)this.dialogueHeight;
        int scrollbarHiddenY = (int)(this.viewport.height() * percentHidden) + this.viewport.y();

        int y = this.viewport.y();

        // Draw the scrollbar portion representing hidden content.
        while (y < scrollbarHiddenY) {
            term.puts(InfoCmp.Capability.cursor_address, y, this.viewport.x() + this.viewport.width() - 1);
            writer.print(':');

            y++;
        }

        // Draw the scrollbar portion representing visible content.
        while (y < this.viewport.y() + this.viewport.height()) {
            term.puts(InfoCmp.Capability.cursor_address, y, this.viewport.x() + this.viewport.width() - 1);
            writer.print('@');

            y++;
        }
    }

    @Override
    public void resize(Rect viewport) {
        this.viewport = viewport;

        // Rewrap paragraphs to fit new max width.
        int maxWidth = Math.max(this.viewport.width() - 3, 2);
        this.dialogueEntries.forEach(p -> p.resize(maxWidth));

        // Sum the number of lines of all paragraphs, including the blank lines in between entries.
        this.dialogueHeight = 0;
        for (WrapParagraph p : this.dialogueEntries) {
            this.dialogueHeight += p.getLines().size() + 1;
        }
    }
}
