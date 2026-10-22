package edu.miamioh.cse201.oceans14.ui;

/**
 * Represents a portion of the terminal screen.
 * <p>
 * Coordinates operate in increments of cells, where (0, 0) is the top-left cell of the terminal,
 * (1, 0) is one cell to the right, etc.
 *
 * @param x The x-coordinate of the top-left corner of the rectangle.
 * @param y The y-coordinate of the top-left corner of the rectangle.
 * @param width The width of the rectangle.
 * @param height The height of the rectangle.
 */
public record Rect(int x, int y, int width, int height) {}
