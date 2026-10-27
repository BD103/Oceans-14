package edu.miamioh.cse201.oceans14.ui;

import java.lang.annotation.*;

/**
 * Intended to be placed on a {@link org.jline.terminal.Terminal} parameter, creates a terminal
 * snapshot test.
 *
 * @see TermSnapshotExtension
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface TermSnapshot {
    /**
     * The width (number of columns) of the terminal.
     */
    int width() default 80;

    /**
     * The height (number of rows) of the terminal.
     */
    int height() default 25;
}
