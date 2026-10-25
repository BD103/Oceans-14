package edu.miamioh.cse201.oceans14.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class TestRect {
    @Nested
    class SplitHorizontalRatio {
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

    @Nested
    class SplitVerticalHeight {
        @Test
        void half() throws ViewportTooSmallException {
            Rect input = new Rect(0, 1, 4, 4);
            Assertions.assertArrayEquals(
                    new Rect[]{
                            new Rect(0, 1, 4, 2),
                            new Rect(0, 3, 4, 2),
                    },
                    input.splitVertical(2)
            );
        }

        @Test
        void secondIsTwoTall() throws ViewportTooSmallException {
            Rect input = new Rect(0, 0, 2, 10);
            Assertions.assertArrayEquals(
                    new Rect[]{
                            new Rect(0, 0, 2, 8),
                            new Rect(0, 8, 2, 2),
                    },
                    input.splitVertical(input.height() - 2)
            );
        }

        @Test
        void onlyTop() throws ViewportTooSmallException {
            Rect input = new Rect(5, 4, 5, 4);
            Assertions.assertArrayEquals(
                    new Rect[]{
                            new Rect(5, 4, 5, 4),
                            new Rect(5, 8, 5, 0),
                    },
                    input.splitVertical(4)
            );
        }

        @Test
        void onlyBottom() throws ViewportTooSmallException {
            Rect input = new Rect(5, 4, 5, 4);
            Assertions.assertArrayEquals(
                    new Rect[]{
                            new Rect(5, 4, 5, 0),
                            new Rect(5, 4, 5, 4),
                    },
                    input.splitVertical(0)
            );
        }

        @Test
        void invalidHeight() {
            Rect input = new Rect(0, 0, 2, 2);
            Assertions.assertAll(
                    () -> Assertions.assertThrows(ViewportTooSmallException.class, () -> input.splitVertical(-1)),
                    () -> Assertions.assertThrows(ViewportTooSmallException.class, () -> input.splitVertical(3))
            );
        }
    }

    @Nested
    class MarginHorizontal {
        @Test
        void basic() throws ViewportTooSmallException {
            Rect input = new Rect(5, 2, 10, 1);
            Assertions.assertEquals(new Rect(6, 2, 8, 1), input.marginHorizontal(1));
        }

        @Test
        void widthBecomesZero() throws ViewportTooSmallException {
            Rect input = new Rect(0, 0, 8, 1);
            Assertions.assertEquals(new Rect(4, 0, 0, 1), input.marginHorizontal(4));
        }

        @Test
        void marginTooLarge() {
            Rect input = new Rect(0, 0, 4, 1);
            Assertions.assertThrows(ViewportTooSmallException.class, () -> input.marginHorizontal(3));
        }
    }

    @Nested
    class MarginVertical {
        @Test
        void basic() throws ViewportTooSmallException {
            Rect input = new Rect(0, 1, 1, 5);
            Assertions.assertEquals(new Rect(0, 3, 1, 1), input.marginVertical(2));
        }

        @Test
        void heightBecomesZero() throws ViewportTooSmallException {
            Rect input = new Rect(10, 0, 5, 2);
            Assertions.assertEquals(new Rect(10, 1, 5, 0), input.marginVertical(1));
        }

        @Test
        void marginTooLarge() {
            Rect input = new Rect(0, 0, 20, 1);
            Assertions.assertThrows(ViewportTooSmallException.class, () -> input.marginVertical(3));
        }
    }
}
