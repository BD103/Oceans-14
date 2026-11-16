package edu.miamioh.cse201.oceans14.ui.views;

import edu.miamioh.cse201.oceans14.actors.Actor;
import edu.miamioh.cse201.oceans14.engine.*;
import edu.miamioh.cse201.oceans14.rooms.Room;
import edu.miamioh.cse201.oceans14.ui.Rect;
import edu.miamioh.cse201.oceans14.ui.View;
import edu.miamioh.cse201.oceans14.ui.text.WrapParagraph;
import org.jline.terminal.Terminal;
import org.jline.utils.InfoCmp.Capability;

import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Set;

/**
 * The stats view, responsible for displaying the current interactable, player inventory, and
 * available actions.
 */
public class StatsView implements View {
    private Rect viewport;
    private final GameEngine engine;

    private final WrapParagraph title = new WrapParagraph("Ocean's 14", 80);
    private final WrapParagraph commands = new WrapParagraph("Commands:", 80);

    public StatsView(GameEngine engine) {
        this.engine = engine;
    }

    @Override
    public void draw(Terminal term) {
        int y = this.viewport.y();

        y = this.drawTitle(term, y) + 1;

        y = this.drawCurrentInteractable(term, y);
        y = this.drawDescription(term, y) + 1;

        y = this.drawChips(term, y);
        y = this.drawInventory(term, y) + 1;

        y = this.drawCommands(term, y);
    }

    /**
     * Draws the title of the game, horizontally centered to the viewport.
     *
     * @param term The terminal to draw to.
     * @param y The y coordinate of the line this will draw to.
     * @return The next available y coordinate to draw to.
     */
    private int drawTitle(Terminal term, int y) {
        PrintWriter writer = term.writer();

        for (String line : this.title.getLines()) {
            // If there's no room left to print, exit.
            if (y >= this.viewport.y() + this.viewport.height()) {
                break;
            }

            // Center the line
            term.puts(Capability.cursor_address, y, (this.viewport.width() / 2) - (line.length() / 2) + this.viewport.x());
            writer.print(line);
            y++;
        }

        return y;
    }

    /**
     * Draws the name of the current room / actor.
     *
     * @param term The terminal to draw to.
     * @param y The y coordinate of the line this will draw to.
     * @return The next available y coordinate to draw to.
     */
    private int drawCurrentInteractable(Terminal term, int y) {
        Interactable interactable = this.engine.getCurrentInteractable();
        String prefix;

        if (interactable instanceof Actor) {
            prefix = "Talking to ";
        } else if (interactable instanceof Room) {
            prefix = "Standing in ";
        } else {
            prefix = "Interacting with ";
        }

        // This will render as "Talking to Anthony", "Standing in Casino Floor", etc.
        WrapParagraph p = new WrapParagraph(prefix + interactable.getName() + ":", this.viewport.width());

        return drawParagraph(term, y, p);
    }

    /**
     * Draws the description of the current room / actor.
     *
     * @param term The terminal to draw to.
     * @param y The y coordinate of the line this will draw to.
     * @return The next available y coordinate to draw to.
     */
    private int drawDescription(Terminal term, int y) {
        WrapParagraph description = new WrapParagraph(this.engine.getCurrentDescription(), this.viewport.width());
        return drawParagraph(term, y, description);
    }

    /**
     * Draws the list of items in the player's inventory.
     *
     * @param term The terminal to draw to.
     * @param y The y coordinate of the line this will draw to.
     * @return The next available y coordinate to draw to.
     */
    private int drawChips(Terminal term, int y) {
        int chips = GameState.getChipCount();
        WrapParagraph p = new WrapParagraph("Chips: " + chips, this.viewport.width());
        return this.drawParagraph(term, y, p);
    }

    /**
     * Draws the list of items in the player's inventory.
     *
     * @param term The terminal to draw to.
     * @param y The y coordinate of the line this will draw to.
     * @return The next available y coordinate to draw to.
     */
    private int drawInventory(Terminal term, int y) {
        Set<String> inventory = GameState.getInventory();
        String text;

        if (inventory.isEmpty()) {
            text = "Inventory: empty :(";
        } else {
            Iterator<String> it = inventory.iterator();

            // Format the text to become "Inventory: Item 1, Item 2, Item 3, etc."
            StringBuilder builder = new StringBuilder("Inventory: ");
            builder.append(GameWorld.getItem(it.next()).name());

            while (it.hasNext()) {
                builder.append(", ");
                builder.append(GameWorld.getItem(it.next()).name());
            }

            text = builder.toString();
        }

        return this.drawParagraph(term, y, new WrapParagraph(text, this.viewport.width()));
    }

    /**
     * Draws the list of available actions the player can take.
     *
     * @param term The terminal to draw to.
     * @param y The y coordinate of the line this will draw to.
     * @return The next available y coordinate to draw to.
     */
    private int drawCommands(Terminal term, int y) {
        // Draw "Commands:"
        y = this.drawParagraph(term, y, this.commands);

        // Draw list of commands
        for (ActionOption option : this.engine.getAvailableActions()) {
            // TODO: improve bullet list formatting by indenting line breaks by 2 characters
            y = this.drawParagraph(term, y, new WrapParagraph("- " + option.id(), this.viewport.width()));
        }

        return y;
    }

    /**
     * A helper function that prints a {@link WrapParagraph} to the terminal.
     *
     * @param term The terminal to draw to.
     * @param y The y coordinate of the line this will draw to.
     * @param p The paragraph being drawn.
     * @return The next available y coordinate to draw to.
     */
    private int drawParagraph(Terminal term, int y, WrapParagraph p) {
        PrintWriter writer = term.writer();

        for (String line : p.getLines()) {
            // If there's no room left to print, exit.
            if (y >= this.viewport.y() + this.viewport.height()) {
                break;
            }

            term.puts(Capability.cursor_address, y, this.viewport.x());
            writer.print(line);
            y++;
        }

        return y;
    }

    @Override
    public void resize(Rect viewport) {
        this.viewport = viewport;
        this.title.resize(this.viewport.width());
        this.commands.resize(this.viewport.width());
    }
}
