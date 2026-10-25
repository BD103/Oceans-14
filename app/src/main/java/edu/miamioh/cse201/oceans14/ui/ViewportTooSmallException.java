package edu.miamioh.cse201.oceans14.ui;

/**
 * An exception thrown when a viewport is too small to be usable.
 * <p>
 * This exception is used to signal that the user should increase the size of their screen.
 */
public class ViewportTooSmallException extends Exception {
    public ViewportTooSmallException() {
        super();
    }
}
