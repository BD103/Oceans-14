package edu.miamioh.cse201.oceans14.ui;

import org.jline.terminal.Terminal;

public class CommandView implements View {
    private Rect viewport;

    public CommandView() {}

    @Override
    public void draw(Terminal term) {
        ViewDebugger.fillViewport(term, this.viewport);
    }

    @Override
    public void computeViewport(Rect parentViewport) {
        int x = parentViewport.x() + 3;
        int y = parentViewport.y() + parentViewport.height() - 2;
        int width = parentViewport.width() - 6;
        int height = 1;

        this.viewport = new Rect(x, y, width, height);
    }
}
