package edu.miamioh.cse201.oceans14.ui;

import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * The dialogue view, responsible for displaying current and past game dialogue.
 */
public class DialogueView implements View {
    private Rect viewport;
    private final List<WrappedText> entries = new ArrayList<>();

    DialogueView() {
        // TODO: Remove sample dialogue once actual dialogue can be emitted by the game.
        entries.add(new WrappedText("> talk to Anthony", 20));
        entries.add(new WrappedText("Anthony: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vestibulum at mauris elit. Vivamus at eleifend nisl. Curabitur viverra eget enim eget luctus. Aenean mollis lorem a hendrerit imperdiet. Ut venenatis ante non diam imperdiet laoreet. Suspendisse feugiat blandit ex. Donec vestibulum tortor ut ex auctor faucibus. Morbi egestas nulla nunc, ut rhoncus nunc porttitor ac. Donec sit amet justo venenatis, tristique eros vel, tempus urna.", 20));
    }

    @Override
    public void draw(Terminal term) {
        PrintWriter writer = term.writer();

        int y = this.viewport.y();

        // Draw dialogue entries.
        outer:
        for (WrappedText paragraph : this.entries) {
            for (int i = 0; i < paragraph.height(); i++) {
                // If there's no more room in viewport, don't draw any more lines or paragraphs.
                if (y >= this.viewport.y() + this.viewport.height()) {
                    break outer;
                }

                term.puts(InfoCmp.Capability.cursor_address, y, this.viewport.x());
                writer.print(paragraph.get(i));
                y++;
            }

            // Add blank line in between paragraphs.
            y++;
        }

        // Draw scrollbar.
        for (y = this.viewport.y(); y < this.viewport.y() + this.viewport.height(); y++) {
            term.puts(InfoCmp.Capability.cursor_address, y, this.viewport.x() + this.viewport.width() - 1);
            writer.print('@');
        }
    }

    @Override
    public void computeViewport(Rect parentViewport) {
        int x = parentViewport.x() + 3;
        int y = parentViewport.y() + 1;
        int width = parentViewport.width() * 2 / 3 - 5;
        int height = parentViewport.height() - 4;

        this.viewport = new Rect(x, y, width, height);

        // Resize dialogue entries.
        this.entries.forEach(t -> t.resize(width - 2));
    }
}
