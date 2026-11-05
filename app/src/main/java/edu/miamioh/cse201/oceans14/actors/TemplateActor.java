package edu.miamioh.cse201.oceans14.actors;

import edu.miamioh.cse201.oceans14.engine.*;

import java.util.ArrayList;
import java.util.List;

/*
IMPORTANT: This is a template of an actor.
This class is designed to error if something has not been fully implemented
DO NOT register this template inside GameWorld
 */

/*
---------------------------------------------------------------------------------------------------
Actors and Rooms are fairly similar in construction and if you have implemented one implementing
the other should be fairly easy. Here is a step by step guide as well as some explanation for the
why's of the class.

Explanation:
An Actor is an object representing any person interacted with in the game world. Some examples are
the Casino Greeter, Anthony, and Chef.

Actors use the same naming scheme as Rooms. All ids should be in snake case while camel case is used
for actual functions. This is to aid in differentiating id from class and function names.
Actor ID: casino_greeter
Class: CasinoGreeter
Action ID: ask_about_casino
Function: askAboutCasino()

Actor ID: anthony
Class: Anthony
Action ID: give_screwdriver
Function: giveScrewdriver()

Tutorial:
In order to create a new actor you follow a fairly simple checklist.
1. Create a new java object in the actors folder and name it after the new actor
2. Copy paste this template into that new class.
3. Update the class name from TemplateActor
4. Update the constructor with the appropriate ID and display friendly name
5. Update the description. If you are creating the actor and just writing temporary text leave the TODO
6. Create the appropriate functions for whatever the actor needs to do such as talk, give item, etc.
7. Customize the dialogue text in endConversation(). The exit logic is already implemented and should remain unchanged.
8. Add all new interactions to getAvailableActions().
   8.1. Add conditional interactions when appropriate.
        These will generally check GameState for an item or flag.
   8.2. Every action ID must have a corresponding case in performAction().
9. Update performAction() to call the appropriate method for each action ID.
   Ensure that the action IDs match exactly between both methods.
10. Ensure you have proper comments / javadoc
11. Go to GameWorld and add the new actor in the registerActors functions.
12. Delete this instruction sheet and double check all TODO's have been completed
TODO DELETE ME
---------------------------------------------------------------------------------------------------
 */




// TODO change this class name
public class TemplateActor extends Actor {

    // TODO update constructor
    public TemplateActor() {
        super("template_actor", "Template Actor");
    }

    /**
     * Returns the description of the actor displayed to the player.
     *
     * @return String representing the actor description text.
     */
    @Override
    public String getDescription() {
        // TODO replace description text
        return "Replace the template text for " + this.id;
    }

    // TODO update getAvailableActions
    @Override
    public List<ActionOption> getAvailableActions() {
        List<ActionOption> actions = new ArrayList<>();

        //Area for always present interactions
        actions.add(new ActionOption("function_name", "Function Description"));

        // Area for conditional Interactions

        // Only offer function two if player has debug_item TODO delete me
        if (GameState.hasItem("debug_item")) {
            actions.add(new ActionOption("function_name_two", "Function Description Two"));
        }

        // End conversation
        actions.add(new ActionOption("end_conversation", "End the conversation"));

        return List.copyOf(actions);
    }

    /**
     * Performs the action ID requested
     * @param actionID The string ID of the interaction to run
     * @return InteractionResult representing the outcome of the action.
     *         In the case of an invalid action returns "No Such Action Exists" in the form
     *         of an interaction result.
     */
    @Override
    public InteractionResult performAction(String actionID) {
        // Check that the action requested exists. If not
        // Return a No Such Action Exists InteractionResult.
        if(!this.hasValidAction(actionID)) {
            return new InteractionResult(
                    new Dialogue.Error("No Such Action Exists"),
                    InteractionTransition.NONE,
                    null
            );
        }

        // This switch statement matches the action ID to the appropriate function
        // The convention for writing these functions is to use underscores in the ID and
        // camel case for the corresponding function.
        // Ex. end_conversation -> endConversation()
        // TODO update perform action
        return switch (actionID) {
            case "function_name" -> functionName();
            case "function_name_two" -> functionNameTwo();

            // End the conversation always exists
            case "end_conversation" -> endConversation();
            // Detect action IDs that are available but lack an implementation.
            default -> throw new IllegalStateException(
                    "A valid action does not exist in the performAction for " + this.id +
                            "\n Please make sure the list of available actions all have an action function."
            );
        };
    }

    /**
     * TODO edit the javadoc for this
     * Do the function
     * @return The interactionResult telling GameEngine to do the function
     */
    private InteractionResult functionName() {
        return new InteractionResult(
                // TODO change dialogue message / type
                new Dialogue.System("This is a system message for " + this.id),
                // TODO Make sure the InteractionTransition is correct
                InteractionTransition.CHANGE_ROOM,
                // TODO make sure we point to the right room / actor
                null
        );
    }
    /**
     * TODO edit the javadoc for this
     * Do the function
     * @return The interactionResult telling GameEngine to do the function
     */
    private InteractionResult functionNameTwo() {
        return new InteractionResult(
                // TODO change dialogue message / type
                new Dialogue.Actor(this.name, "This is an actor message for " + this.id),
                // TODO Make sure the InteractionTransition is correct
                InteractionTransition.CHANGE_ROOM,
                // TODO make sure we point to the right room / actor
                null
        );
    }

    /**
     * Ends the conversation
     *
     * @return a POP result returning to the previous interaction
     */
    private InteractionResult endConversation() {
        // TODO update end convo text
        return new InteractionResult(
                new Dialogue.System("REPLACE ME"),
                InteractionTransition.POP,
                null
        );
    }


}
