package edu.miamioh.cse201.oceans14.ui;

import org.jline.terminal.Terminal;

public class DialogueView implements View {
    private Rect viewport;

    public DialogueView() {}

    @Override
    public void draw(Terminal term) {
        ViewDebugger.fillViewport(term, this.viewport);
    }

    @Override
    public void computeViewport(Rect parentViewport) {
        int x = parentViewport.x() + 3;
        int y = parentViewport.y() + 1;
        int width = parentViewport.width() * 2 / 3 - 5;
        int height = parentViewport.height() - 4;

        this.viewport = new Rect(x, y, width, height);
    }
}
