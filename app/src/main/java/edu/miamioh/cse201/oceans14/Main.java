package edu.miamioh.cse201.oceans14;


import edu.miamioh.cse201.oceans14.ui.UI;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        //try (UI ui = new UI()) {
            //ui.run();
    	Deque<T> items = new ArrayDeque<T>(); 
    	Room entrance = new EntranceRoom();
    	items.push(entrance);
    	List<ActionOption> entranceOptions = entranceget.AvailableActions();
    	for (ActionOption a: entranceOptions) {
    		System.out.println(a);
    	}
    	
        }
    }
}
