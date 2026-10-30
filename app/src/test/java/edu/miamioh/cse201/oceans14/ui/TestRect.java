package edu.miamioh.cse201.oceans14.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class TestRect {
    @Test
    void marginTop() {
        Rect r = new Rect(0, 0, 5, 5);
        Assertions.assertEquals(new Rect(0, 4, 5, 1), r.marginTop(4));
    }

    @ParameterizedTest
    @ValueSource(ints = {2, 3, 100})
    void marginTopOverflow(int margin) {
        Rect r = new Rect(2, 3, 1, 2);
        Assertions.assertEquals(new Rect(2, 5, 1, 0), r.marginTop(margin));
    }

    @Test
    void marginBottom() {
        Rect r = new Rect(0, 1, 20, 30);
        Assertions.assertEquals(new Rect(0, 1, 20, 10), r.marginBottom(20));
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 11, 200})
    void marginBottomOverflow(int margin) {
        Rect r = new Rect(5, 0, 1, 10);
        Assertions.assertEquals(new Rect(5, 0, 1, 0), r.marginBottom(margin));
    }

    @Test
    void marginLeft() {
        Rect r = new Rect(0, 0, 8, 2);
        Assertions.assertEquals(new Rect(3, 0, 5, 2), r.marginLeft(3));
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 50})
    void marginLeftOverflow(int margin) {
        Rect r = new Rect(10, 20, 1, 3);
        Assertions.assertEquals(new Rect(11, 20, 0, 3), r.marginLeft(margin));
    }

    @Test
    void marginRight() {
        Rect r = new Rect(40, 20, 100, 1);
        Assertions.assertEquals(new Rect(40, 20, 20, 1), r.marginRight(80));
    }

    @ParameterizedTest
    @ValueSource(ints = {24, 25, 1000})
    void marginRightOverflow(int margin) {
        Rect r = new Rect(1, 2, 24, 100);
        Assertions.assertEquals(new Rect(1, 2, 0, 100), r.marginRight(margin));
    }

    @Test
    void marginInvalid() {
        Rect r = new Rect(0, 0, 2, 2);

        Assertions.assertAll(
                () -> Assertions.assertThrows(IllegalArgumentException.class, () -> r.marginTop(-1)),
                () -> Assertions.assertThrows(IllegalArgumentException.class, () -> r.marginBottom(-1)),
                () -> Assertions.assertThrows(IllegalArgumentException.class, () -> r.marginLeft(-1)),
                () -> Assertions.assertThrows(IllegalArgumentException.class, () -> r.marginRight(-1)),
                () -> Assertions.assertThrows(IllegalArgumentException.class, () -> r.marginHorizontal(-1)),
                () -> Assertions.assertThrows(IllegalArgumentException.class, () -> r.marginVertical(-1)),
                () -> Assertions.assertThrows(IllegalArgumentException.class, () -> r.margin(-1))
        );
    }
}
