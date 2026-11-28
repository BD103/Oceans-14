package edu.miamioh.cse201.oceans14.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.ListIterator;

public class TestFixedCapacityList {
    @ParameterizedTest
    @ValueSource(ints = { -1, 0 })
    void illegalCapacity(int capacity) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new FixedCapacityList<>(capacity));
    }

    @Test
    void sizeStartsAtZero() {
        FixedCapacityList<Object> list = new FixedCapacityList<>(10);
        Assertions.assertEquals(0, list.size());
    }

    @Test
    void sizeIncreasesWithAdd() {
        FixedCapacityList<String> list = new FixedCapacityList<>(10);

        list.add("hi");
        list.add("there");

        Assertions.assertEquals(2, list.size());
    }

    @Test
    void sizeMaxesAtCapacity() {
        FixedCapacityList<String> list = new FixedCapacityList<>(3);

        list.add("first");
        list.add("second");
        list.add("third");
        list.add("fourth");

        Assertions.assertEquals(3, list.size());
    }

    @Test
    void add() {
        FixedCapacityList<String> list = new FixedCapacityList<>(15);

        list.add("Boo");
        list.add("yah!");

        Assertions.assertIterableEquals(List.of("yah!", "Boo"), list);
    }

    @Test
    void addMaxCapacity() {
        FixedCapacityList<Integer> list = new FixedCapacityList<>(3);

        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        Assertions.assertIterableEquals(List.of(5, 4, 3), list);
    }

    @Test
    void listIterator() {
        FixedCapacityList<String> list = new FixedCapacityList<>(20);

        list.add("Foo");
        list.add("Bar");
        list.add("Baz");

        ListIterator<String> it = list.listIterator();

        Assertions.assertEquals("Baz", it.next());
        Assertions.assertEquals("Bar", it.next());
        Assertions.assertEquals("Foo", it.next());
        Assertions.assertFalse(it.hasNext());
        Assertions.assertEquals("Foo", it.previous());
        Assertions.assertEquals("Bar", it.previous());
        Assertions.assertEquals("Baz", it.previous());
        Assertions.assertFalse(it.hasPrevious());
    }
}
