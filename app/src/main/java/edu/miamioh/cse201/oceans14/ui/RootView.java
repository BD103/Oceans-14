package edu.miamioh.cse201.oceans14.ui;

import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;

import java.io.PrintWriter;

/**
 * The root view of the program.
 * <p>
 * This view is a container for every other UI element in the application, and its viewport
 * stretches to encompass the entire screen.
 */
public class RootView implements View {
    private Rect viewport;

    public RootView() {}

    @Override
    public void draw(Terminal term) {
        term.puts(Capability.clear_screen);
        ViewDebugger.fillViewport(term, this.viewport);
    }

    @Override
    public void computeViewport(Rect screenViewport) {
        // This view expands to the entire screen, so we don't modify anything.
        this.viewport = screenViewport;
    }
}
