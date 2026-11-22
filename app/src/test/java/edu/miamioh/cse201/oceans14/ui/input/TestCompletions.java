package edu.miamioh.cse201.oceans14.ui.input;

import edu.miamioh.cse201.oceans14.engine.GameEngine;
import edu.miamioh.cse201.oceans14.engine.GameWorld;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TestCompletions {
    private GameEngine engine;

    @BeforeEach
    void beforeEach() {
        this.engine = new GameEngine();
    }

    @Test
    void empty() {
        String match = Completions.complete("", this.engine);
        Assertions.assertNull(match);
    }

    @Test
    void invalid() {
        String match = Completions.complete("askjdhfakh hjkasdsad", this.engine);
        Assertions.assertNull(match);
    }

    @Test
    void singleMatch() {
        String match = Completions.complete("enter_ca", this.engine);
        Assertions.assertEquals("sino", match);
    }

    @Test
    void twoMatches() {
        String match = Completions.complete("talk_to", this.engine);
        Assertions.assertNull(match);
    }

    @Test
    void alreadyComplete() {
        String match = Completions.complete("enter_casino", this.engine);
        Assertions.assertEquals("", match);
    }
}
