package edu.miamioh.cse201.oceans14.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestRect {
    @Nested
    class SplitHorizontal {
        @Test
        void half() {
            Rect input = new Rect(1, 1, 4, 2);
            Assertions.assertArrayEquals(
                    new Rect[]{
                            new Rect(1, 1, 2, 2),
                            new Rect(3, 1, 2, 2),
                    },
                    input.splitHorizontal(0.5)
            );
        }

        @Test
        void twoThirds() {
            Rect input = new Rect(0, 0, 10, 4);
            Assertions.assertArrayEquals(
                    new Rect[]{
                            new Rect(0, 0, 6, 4),
                            new Rect(6, 0, 4, 4),
                    },
                    input.splitHorizontal(2.0 / 3.0)
            );
        }

        @Test
        void zeroWidth() {
            Rect input = new Rect(3, 5, 0, 0);
            Assertions.assertArrayEquals(
                    new Rect[]{
                            new Rect(3, 5, 0, 0),
                            new Rect(3, 5, 0, 0),
                    },
                    input.splitHorizontal(0.25)
            );
        }

        @Test
        void invalidRatio() {
            Rect input = new Rect(0, 0, 10, 10);

            Assertions.assertAll(
                    () -> Assertions.assertThrows(IllegalArgumentException.class, () -> input.splitHorizontal(-1)),
                    () -> Assertions.assertThrows(IllegalArgumentException.class, () -> input.splitHorizontal(2))
            );
        }
    }
}
