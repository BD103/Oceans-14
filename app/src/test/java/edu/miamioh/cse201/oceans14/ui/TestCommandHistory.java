package edu.miamioh.cse201.oceans14.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestCommandHistory {
    @Test
    void scrollUpEmpty() {
        CommandHistory history = new CommandHistory();
        Assertions.assertNull(history.scrollUp());
    }

    @Test
    void scrollDownEmpty() {
        CommandHistory history = new CommandHistory();
        Assertions.assertNull(history.scrollDown());
    }

    @Test
    void scrollCompleteHistory() {
        CommandHistory history = new CommandHistory();

        history.add("Old command");
        history.add("New command");

        Assertions.assertEquals("New command", history.scrollUp());
        Assertions.assertEquals("Old command", history.scrollUp());
        Assertions.assertNull(history.scrollUp()); // End of history
        Assertions.assertEquals("New command", history.scrollDown());
        Assertions.assertEquals("", history.scrollDown()); // Beginning of history
        Assertions.assertNull(history.scrollDown());
    }

    @Test
    void scrollSwitchDirections() {
        CommandHistory history = new CommandHistory();

        history.add("Red");
        history.add("Green");
        history.add("Blue");

        Assertions.assertEquals("Blue", history.scrollUp());
        Assertions.assertEquals("Green", history.scrollUp());
        Assertions.assertEquals("Blue", history.scrollDown());
        Assertions.assertEquals("Green", history.scrollUp());
    }

    @Test
    void clearScroll() {
        CommandHistory history = new CommandHistory();

        history.add("enter_casino");
        history.add("talk_to_contractors");

        Assertions.assertEquals("talk_to_contractors", history.scrollUp());
        Assertions.assertEquals("enter_casino", history.scrollUp());

        history.clearScroll();

        Assertions.assertEquals("talk_to_contractors", history.scrollUp());
    }
}
