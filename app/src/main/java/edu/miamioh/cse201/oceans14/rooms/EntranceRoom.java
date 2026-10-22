package edu.miamioh.cse201.oceans14.rooms;

import edu.miamioh.cse201.oceans14.engine.ActionOption;
import edu.miamioh.cse201.oceans14.engine.InteractionResult;
import edu.miamioh.cse201.oceans14.engine.GameWorld;
import edu.miamioh.cse201.oceans14.engine.InteractionTransition;


import java.util.List;

public class Entrance extends Room {

    public Entrance

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public List<ActionOption> getAvailableActions() {
        return List.of();
    }

    @Override
    public Boolean hasValidAction(String proposedActionID) {
        return null;
    }

    @Override
    public InteractionResult performAction(String actionID) {
        return null;
    }
}
