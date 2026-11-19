package edu.miamioh.cse201.oceans14.ui;

public class CommandHistory {
    private final FixedCapacityList<String> entries = new FixedCapacityList<>(250);

    public void add(String command) {
        this.entries.add(command);
    }
}
