package edu.miamioh.cse201.oceans14.ui.input;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestCommandBuffer {
    @Test
    void startsEmpty() {
        CommandBuffer buffer = new CommandBuffer();

        Assertions.assertEquals("", buffer.getBuffer());
        Assertions.assertEquals(0, buffer.getCursor());
    }

    @Test
    void insertChar() {
        CommandBuffer buffer = new CommandBuffer();

        buffer.insert('H');
        buffer.insert('i');
        buffer.insert('!');

        Assertions.assertEquals("Hi!", buffer.getBuffer());
        Assertions.assertEquals(3, buffer.getCursor());
    }

    @Test
    void insertString() {
        CommandBuffer buffer = new CommandBuffer();

        buffer.insert("Boo!");

        Assertions.assertEquals("Boo!", buffer.getBuffer());
        Assertions.assertEquals(4, buffer.getCursor());
    }

    @Test
    void clear() {
        CommandBuffer buffer = new CommandBuffer();

        buffer.insert("No");
        buffer.clear();

        Assertions.assertEquals("", buffer.getBuffer());
        Assertions.assertEquals(0, buffer.getCursor());
    }

    @Test
    void delete() {
        CommandBuffer buffer = new CommandBuffer();

        buffer.insert("Yes");
        buffer.delete();

        Assertions.assertEquals("Ye", buffer.getBuffer());
        Assertions.assertEquals(2, buffer.getCursor());
    }

    @Test
    void left() {
        CommandBuffer buffer = new CommandBuffer();

        buffer.left();

        Assertions.assertEquals(0, buffer.getCursor());

        buffer.insert("MOO");
        buffer.left();
        buffer.left();

        Assertions.assertEquals(1, buffer.getCursor());
    }

    @Test
    void right() {
        CommandBuffer buffer = new CommandBuffer();

        buffer.right();

        Assertions.assertEquals(0, buffer.getCursor());

        buffer.insert('1');
        buffer.left();
        buffer.right();

        Assertions.assertEquals(1, buffer.getCursor());

        buffer.right();

        Assertions.assertEquals(1, buffer.getCursor());
    }

    @Test
    void toEnd() {
        CommandBuffer buffer = new CommandBuffer();

        buffer.insert("Some text");

        for (int i = 0; i < 5; i++) {
            buffer.left();
        }

        buffer.toEnd();

        Assertions.assertEquals(9, buffer.getCursor());
    }
}
