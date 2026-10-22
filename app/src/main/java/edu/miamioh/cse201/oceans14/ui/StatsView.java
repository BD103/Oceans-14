package edu.miamioh.cse201.oceans14.ui;

import org.jline.terminal.Terminal;

public class StatsView implements View {
    private Rect viewport;

    public StatsView() {}

    @Override
    public void draw(Terminal term) {
        ViewDebugger.fillViewport(term, this.viewport);
    }

    @Override
    public void computeViewport(Rect parentViewport) {
        int x = parentViewport.width() * 2 / 3 + 3;
        int y = parentViewport.y() + 1;
        int width = parentViewport.width() - x - 3;
        int height = parentViewport.height() - 4;

        this.viewport = new Rect(x, y, width, height);
    }
}
