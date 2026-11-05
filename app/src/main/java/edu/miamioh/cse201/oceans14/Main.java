package edu.miamioh.cse201.oceans14;

import edu.miamioh.cse201.oceans14.engine.GameEngine;
import edu.miamioh.cse201.oceans14.engine.GameWorld;
import edu.miamioh.cse201.oceans14.ui.UI;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        GameEngine engine = new GameEngine(GameWorld.getRoom("entrance_room"));

        try (UI ui = new UI(engine)) {
            ui.run();
        }
    }
}
