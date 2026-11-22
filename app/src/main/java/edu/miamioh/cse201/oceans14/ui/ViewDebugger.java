package edu.miamioh.cse201.oceans14.ui;

import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;

import java.io.PrintWriter;

/**
 * A helper class that assists with debugging views.
 * <p>
 * This class should not be used outside of development.
 */
public class ViewDebugger {
    /**
     * Draws a filled rectangle of a semi-random color to the entire area of a viewport.
     * <p>
     * This can be used to visually verify the dimensions of a viewport are calculated correctly.
     *
     * @param term The terminal to draw to.
     * @param viewport The viewport to draw.
     */
    public static void fillViewport(Terminal term, Rect viewport) {
        PrintWriter writer = term.writer();

        // Set cursor color to a random 8-bit color.
        // <https://en.wikipedia.org/wiki/ANSI_escape_code#8-bit>
        writer.print("\033[38;5;" + Math.abs(viewport.hashCode()) % 256 + "m");

        for (int y = viewport.y(); y < viewport.y() + viewport.height(); y++) {
            term.puts(Capability.cursor_address, y, viewport.x());

            for (int x = viewport.x(); x < viewport.x() + viewport.width(); x++) {
                writer.print('X');
            }
        }

        // Reset cursor color.
        writer.print("\033[0m");
    }
}
