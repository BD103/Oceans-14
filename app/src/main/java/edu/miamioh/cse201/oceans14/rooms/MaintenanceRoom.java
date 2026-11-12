package edu.miamioh.cse201.oceans14.rooms;

import edu.miamioh.cse201.oceans14.engine.*;

import java.util.ArrayList;
import java.util.List;

public class MaintenanceRoom extends Room {
    public MaintenanceRoom() {
        super("maintenance_room", "Maintenance Room");
    }

    @Override
    public String getDescription() {
        return "A dimly-lit room used by the maintenance workers. On the left is workbench, on the right is a shelving unit of slot machine parts, and on the back wall is a vent.";
    }

    @Override
    public List<ActionOption> getAvailableActions() {
        List<ActionOption> actions = new ArrayList<>();

        if (GameState.hasItem("screwdriver")) {
            actions.add(new ActionOption("enter_vent", "Unscrew vent panel using screwdriver and enter it"));
        } else {
            actions.add(new ActionOption("inspect_vent", "Inspect the vent in the back of the room"));
        }

        actions.add(new ActionOption("inspect_workbench", "Inspect the workbench"));
        actions.add(new ActionOption("inspect_shelves", "Inspect the shelving unit of slot machine parts"));
        actions.add(new ActionOption("exit_maintenance_room", "Return to the main casino floor"));

        return actions;
    }

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

        return switch (actionID) {
            case "enter_vent" -> enterVent();
            case "inspect_vent" -> inspectVent();
            case "inspect_workbench" -> inspectWorkbench();
            case "inspect_shelves" -> inspectShelves();
            case "enter_casino_room" -> enterCasinoRoom();
            // Detect action IDs that are available but lack an implementation.
            default -> throw new IllegalStateException(
                    "A valid action does not exist in the performAction for " + this.getID() +
                            "\n Please make sure the list of available actions all have an action function."
            );
        };
    }

    private InteractionResult enterVent() {
        return new InteractionResult(
                new Dialogue.System("You unscrew the vent cover and pull yourself inside"),
                InteractionTransition.CHANGE_ROOM,
                GameWorld.getRoom("vent_room")
        );
    }

    private InteractionResult inspectVent() {
        return new InteractionResult(
                new Dialogue.System("You walk up to the vent cover. It's securely fastened to the wall, too tight for you to unscrew with your fingers. Where could the vent lead..?"),
                InteractionTransition.NONE,
                null
        );
    }

    private InteractionResult inspectWorkbench() {
        GameState.addItem("screwdriver_checkout_note");
        return new InteractionResult(
                new Dialogue.System("You find a messy workbench with wires, bolts, and various electronics strewn about. Hanging on the wall behind the bench is an empty battery charger, a photo of one of the contractor's family, and a sticky note from Anthony saying he borrowed the screwdriver. You take the sticky note."),
                InteractionTransition.NONE,
                null
        );
    }

    private InteractionResult inspectShelves() {
        return new InteractionResult(
                new Dialogue.System("The shelving unit is filled with boxes of slot machine parts, as well as a manual on slot machine maintenance. The manual's page is turned to the chapter 'Ethics and Morals', however the page was ripped out."),
                InteractionTransition.NONE,
                null
        );
    }

    private InteractionResult enterCasinoRoom() {
        return new InteractionResult(
                new Dialogue.System("You leave the maintenance room."),
                InteractionTransition.CHANGE_ROOM,
                GameWorld.getRoom("casino_room")
        );
    }
}
