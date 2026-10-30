package edu.miamioh.cse201.oceans14.ui;

/**
 * Represents a portion of the terminal screen.
 * <p>
 * Coordinates operate in increments of cells, where (0, 0) is the top-left cell of the terminal,
 * (1, 0) is one cell to the right, etc.
 *
 * @param x      The x-coordinate of the top-left corner of the rectangle.
 * @param y      The y-coordinate of the top-left corner of the rectangle.
 * @param width  The width of the rectangle.
 * @param height The height of the rectangle.
 */
public record Rect(int x, int y, int width, int height) {
    /**
     * Adds a margin to the top of the viewport, shrinking the viewport's height.
     *
     * @param margin The number of cells to shrink by.
     * @return The new viewport with the margin.
     * @throws IllegalArgumentException If margin is negative.
     */
    public Rect marginTop(int margin) throws IllegalArgumentException {
        if (margin < 0) {
            throw new IllegalArgumentException("Margin " + margin + " must not be negative");
        }

        return new Rect(this.x, Math.min(this.y + margin, this.y + this.height), this.width, Math.max(this.height - margin, 0));
    }

    /**
     * Adds a margin to the bottom of the viewport, shrinking the viewport's height.
     *
     * @param margin The number of cells to shrink by.
     * @return The new viewport with the margin.
     * @throws IllegalArgumentException If margin is negative.
     */
    public Rect marginBottom(int margin) throws IllegalArgumentException {
        if (margin < 0) {
            throw new IllegalArgumentException("Margin " + margin + "must not be negative");
        }

        return new Rect(this.x, this.y, this.width, Math.max(this.height - margin, 0));
    }

    /**
     * Adds a margin to the left of the viewport, shrinking the viewport's width.
     *
     * @param margin The number of cells to shrink by.
     * @return The new viewport with the margin.
     * @throws IllegalArgumentException If margin is negative.
     */
    public Rect marginLeft(int margin) throws IllegalArgumentException {
        if (margin < 0) {
            throw new IllegalArgumentException("Margin " + margin + "must not be negative");
        }

        return new Rect(Math.min(this.x + margin, this.x + this.width), this.y, Math.max(this.width - margin, 0), this.height);
    }

    /**
     * Adds a margin to the right of the viewport, shrinking the viewport's width.
     *
     * @param margin The number of cells to shrink by.
     * @return The new viewport with the margin.
     * @throws IllegalArgumentException If margin is negative.
     */
    public Rect marginRight(int margin) throws IllegalArgumentException {
        if (margin < 0) {
            throw new IllegalArgumentException("Margin " + margin + "must not be negative");
        }

        return new Rect(this.x, this.y, Math.max(this.width - margin, 0), this.height);
    }

    /**
     * Adds a margin to the top and bottom of the viewport, shrinking the viewport's height.
     *
     * @param margin The number of cells to shrink by on each side.
     * @return The new viewport with the margins.
     * @throws IllegalArgumentException If margin is negative.
     */
    public Rect marginVertical(int margin) throws IllegalArgumentException {
        // No need to check if margin is negative, the more specific margin methods do so already.
        return this.marginTop(margin).marginBottom(margin);
    }

    /**
     * Adds a margin to the left and right of the viewport, shrinking the viewport's width.
     *
     * @param margin The number of cells to shrink by on each side.
     * @return The new viewport with the margins.
     * @throws IllegalArgumentException If margin is negative.
     */
    public Rect marginHorizontal(int margin) throws IllegalArgumentException {
        // No need to check if margin is negative, the more specific margin methods do so already.
        return this.marginLeft(margin).marginRight(margin);
    }

    /**
     * Adds a margin to all four sides of the viewport, shrinking the viewport's width and height.
     *
     * @param margin The number of cells to shrink by on each side.
     * @return The new viewport with the margins.
     * @throws IllegalArgumentException If margin is negative.
     */
    public Rect margin(int margin) throws IllegalArgumentException {
        // No need to check if margin is negative, the more specific margin methods do so already.
        return this.marginHorizontal(margin).marginVertical(margin);
    }
}
