package edu.miamioh.cse201.oceans14.ui.views;

import edu.miamioh.cse201.oceans14.engine.GameEngine;
import edu.miamioh.cse201.oceans14.ui.Rect;
import edu.miamioh.cse201.oceans14.ui.View;
import edu.miamioh.cse201.oceans14.ui.input.CommandBuffer;
import edu.miamioh.cse201.oceans14.ui.input.Completions;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;

import java.io.PrintWriter;

/**
 * The command view, an input box where the player types commands.
 */
public class CommandView implements View {
    private Rect viewport;
    private final CommandBuffer buffer;
    private final GameEngine engine;

    public CommandView(CommandBuffer buffer, GameEngine engine) {
        this.buffer = buffer;
        this.engine = engine;
    }

    @Override
    public void draw(Terminal term) {
        PrintWriter writer = term.writer();

        // Try to find an auto-completion of buffer.
        String match = Completions.complete(this.buffer.getBuffer(), this.engine);

        term.puts(Capability.cursor_address, this.viewport.y(), this.viewport.x());

        writer.print("> ");
        writer.print(this.buffer.getBuffer());

        // If we can auto-complete, display the remaining characters in gray.
        if (match != null) {
            writer.print("\033[2m");
            writer.print(match);
            writer.print("\033[0m");
        }

        // Move the terminal cursor's position to where the command buffer's cursor is.
        term.puts(Capability.cursor_address, this.viewport.y(), this.viewport.x() + 2 + this.buffer.getCursor());
    }

    @Override
    public void resize(Rect viewport) {
        this.viewport = viewport;
    }
}
