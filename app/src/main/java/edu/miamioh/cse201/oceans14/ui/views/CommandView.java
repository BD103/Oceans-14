package edu.miamioh.cse201.oceans14.ui.views;

import edu.miamioh.cse201.oceans14.ui.Rect;
import edu.miamioh.cse201.oceans14.ui.View;
import edu.miamioh.cse201.oceans14.ui.input.CommandBuffer;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;

import java.io.PrintWriter;

/**
 * The command view, an input box where the player types commands.
 */
public class CommandView implements View {
    private Rect viewport;
    private final CommandBuffer buffer;

    public CommandView(CommandBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void draw(Terminal term) {
        PrintWriter writer = term.writer();

        term.puts(Capability.cursor_address, this.viewport.y(), this.viewport.x());

        writer.print("> ");
        writer.print(this.buffer.getBuffer());

        // Move the terminal cursor's position to where the command buffer's cursor is.
        term.puts(Capability.cursor_address, this.viewport.y(), this.viewport.x() + 2 + this.buffer.getCursor());
    }

    @Override
    public void resize(Rect viewport) {
        this.viewport = viewport;
    }
}
