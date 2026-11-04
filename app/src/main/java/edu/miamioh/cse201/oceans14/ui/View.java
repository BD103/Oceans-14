package edu.miamioh.cse201.oceans14.ui;

import org.jline.terminal.Terminal;

/**
 * A view represents a single UI element, which can optionally contain more views.
 * <p>
 * The main method of this interface is {@link draw}, which draws the view to the screen.
 * <p>
 * Each view has a viewport, a {@link Rect} that represents a portion of the screen the view will
 * draw to. Well-behaved views do not draw outside their viewport. Views should store their
 * viewport as a private instance variable that is set by {@link resize}.
 */
public interface View {
    /**
     * Draws this view to the terminal.
     *
     * @param term The terminal to draw to.
     */
    void draw(Terminal term);

    /**
     * Recalculates the size of child views, called when the view is first constructed and when the
     * terminal has resized.
     *
     * @param viewport The new viewport of this view.
     */
    void resize(Rect viewport);
}
