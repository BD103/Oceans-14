package edu.miamioh.cse201.oceans14;

import edu.miamioh.cse201.oceans14.ui.UI;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        try (UI ui = new UI()) {
            ui.run();
        }
    }
}
