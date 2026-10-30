package edu.miamioh.cse201.oceans14.interpreter;

import java.util.List;

import edu.miamioh.cse201.oceans14.engine.ActionOption;
import edu.miamioh.cse201.oceans14.engine.Interactable;

public class Dialogue { 

	// Interprets user inputs into game and returns commands based off that input.
	public static String interpret (String userInput, Interactable interactable) {
		userInput.toLowerCase();
		if (userInput.equals("help")) { // help action wont change across rooms.
			return "output_help"; 
		} else if (userInput.equals("description")) { // description action wont change across rooms.
			return "output_description"; 
		} else {
			List<ActionOption> actions = interactable.getAvailableActions();
			String label = "";
				for (ActionOption a: actions) { // looks at each available action and looks for one 
												// for one that has a label that matches input
					label = a.label().toLowerCase();
					if (label.equals(userInput)) {
						return a.id();
					}
				}
				return "action_not_found";
		}
	}
}
