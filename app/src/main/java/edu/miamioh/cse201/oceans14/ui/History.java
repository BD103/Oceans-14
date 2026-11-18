package edu.miamioh.cse201.oceans14.ui;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * A list of entries with a maximum capacity.
 * <p>
 * This data structure is intended to track the "history" of an action, such as a list of actions
 * previously taken or a list of dialogue recently seen. This list has a maximum capacity; adding
 * a new entry when it is full will cause the oldest entry to be removed.
 *
 * @param <T> The entry type.
 */
public class History<T> implements Iterable<T> {
    // A linked list is an unusual choice, but it fits really well here. It is O(1) to add and
    // remove elements from the ends of the list, which is a very frequent operation here. Compared
    // to ArrayList, which has to shift elements every time you remove from the end, this is much
    // more efficient. In an ideal world I would use a ring buffer for better iteration
    // performance, but Java doesn't provide one in the standard library and I don't want to
    // implement one myself.
    private final LinkedList<T> data;
    private final int capacity;

    /**
     * Creates a new history list with a given maximum capacity.
     *
     * @param capacity The maximum number of items this list will store.
     * @throws IllegalArgumentException If capacity is less than 1.
     */
    public History(int capacity) {
        if (capacity < 1) {
            throw new IllegalArgumentException("History capacity must be at least 1, but received capacity of " + capacity);
        }

        this.data = new LinkedList<>();
        this.capacity = capacity;
    }

    /**
     * Adds a new entry to the list.
     * <p>
     * If the list is full, this will remove the oldest entry.
     *
     * @param item The entry to be added.
     */
    public void add(T item) {
        // If we're at max capacity, remove the oldest entry.
        if (this.data.size() >= this.capacity) {
            this.data.removeLast();
        }

        this.data.addFirst(item);
    }

    /**
     * @return An iterator over all entries in the history, ordered newest to oldest.
     */
    @Override
    public Iterator<T> iterator() {
        return this.data.iterator();
    }

    /**
     * @return A list iterator over the history, ordered newest to oldest.
     */
    public ListIterator<T> listIterator() {
        return this.data.listIterator(0);
    }
}
