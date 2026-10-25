package edu.miamioh.cse201.oceans14.ui;

/**
 * A viewport, represents a portion of the terminal screen.
 * <p>
 * Coordinates operate in increments of cells, where (0, 0) is the top-left cell of the terminal,
 * (1, 0) is one cell to the right, etc.
 *
 * @param x The x-coordinate of the top-left corner of the rectangle.
 * @param y The y-coordinate of the top-left corner of the rectangle.
 * @param width The width of the rectangle.
 * @param height The height of the rectangle.
 */
public record Rect(int x, int y, int width, int height) {
    /**
     * Splits the viewport horizontally by a certain ratio.
     *
     * @param ratio A value between 0 and 1 representing where to split, where 0 is the far left,
     *              0.5 is the middle, and 1 is the far right.
     * @return An array of two viewports, the first being the left viewport and the second being the right.
     * @throws IllegalArgumentException If ratio is less than 0 or greater than 1.
     */
    public Rect[] splitHorizontal(double ratio) throws IllegalArgumentException {
        if (ratio < 0.0 || ratio > 1.0) {
            throw new IllegalArgumentException("Ratio " + ratio + " is not between 0 and 1");
        }

        int leftWidth = (int)((double)this.width * ratio);
        int rightWidth = this.width - leftWidth;

        return new Rect[] {
                new Rect(this.x, this.y, leftWidth, this.height),
                new Rect(this.x + leftWidth, this.y, rightWidth, this.height),
        };
    }
}
