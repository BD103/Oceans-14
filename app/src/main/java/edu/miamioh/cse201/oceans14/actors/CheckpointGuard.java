package edu.miamioh.cse201.oceans14.actors;

import edu.miamioh.cse201.oceans14.engine.*;

import java.util.List;

/**
 * Beard, the guard who stands in front of the vault and lets you in if you pass the checks.
 * <p>
 * Beard requires you to tell him the password (learned from the drunk guard) before entering. This
 * class also verifies you disabled power, as there is a retina scanner that would bar the player
 * entry.
 */
public class CheckpointGuard extends Actor {
    /** The password used to enter the vault. */
    private static final String PASSWORD = "password123";

    public CheckpointGuard() {
        super("checkpoint_guard", "Beard");
    }

    @Override
    public String getDescription() {
        return "A shockingly bald man guarding the vault door. His nametag reads: 'Hello, my name is Beard!', yet he has no facial hair.";
    }

    @Override
    public List<ActionOption> getAvailableActions() {
        // You may also type in an arbitrary string to tell Beard the password, if the flag
        // "password_told_to_checkpoint_guard" isn't active yet.
        return List.of(
                new ActionOption("enter_vault", "Enter Vault"),
                new ActionOption("why_beard", "Ask about Name"),
                new ActionOption("end_conversation", "End the conversation")
        );
    }

    @Override
    public boolean hasValidAction(String actionID) {
        if (GameState.flagIsActive("password_told_to_checkpoint_guard")) {
            return super.hasValidAction(actionID);
        } else {
            // If the correct password hasn't been entered yet, accept all possible inputs.
            return actionID != null;
        }
    }

    @Override
    public InteractionResult performAction(String actionID) {
        if (!this.hasValidAction(actionID)) {
            return new InteractionResult(
                    new Dialogue.Error("No Such Action Exists"),
                    InteractionTransition.NONE,
                    null
            );
        }

        return switch (actionID) {
            case "enter_vault" -> enterVault();
            case "why_beard" -> whyBeard();
            case "end_conversation" -> endConversation();
            default -> {
                if (GameState.flagIsActive("password_told_to_checkpoint_guard")) {
                    // It's no longer possible to guess the password, so this must be an
                    // unimplemented action.
                    throw new IllegalStateException(
                            "A valid action does not exist in the performAction for " + this.getID() +
                                    "\n Please make sure the list of available actions all have an action function."
                    );
                } else {
                    // Guess the password with an arbitrary string.
                    yield password(actionID);
                }
            }
        };
    }

    /**
     * Tells the player they need the password to enter the vault.
     */
    private InteractionResult enterVault() {
        if (GameState.flagIsActive("password_told_to_checkpoint_guard")) {
            if (GameState.flagIsActive("vault_security_disabled")) {
                return new InteractionResult(
                        new Dialogue.List(List.of(
                                new Dialogue.System("You walk up to the retina scanner, which has a red error light blinking. You scan your eye, and the device makes a distorted beeping noise. The vault door slowly opens."),
                                new Dialogue.Actor(this.getName(), "Huh, I've never heard that sound before! I'll need to call in maintenance to inspect it after my shift, if they're not tooling with that damn elevator again.")
                        )),
                        InteractionTransition.CHANGE_ROOM,
                        GameWorld.getRoom("vault_room")
                );
            } else {
                return new InteractionResult(
                        new Dialogue.System("You look past the guard and see a retina scanner with a glowing green light. You won't be able to enter the vault without disabling that scanner. Somehow..."),
                        InteractionTransition.NONE,
                        null
                );
            }
        } else {
            return new InteractionResult(
                    new Dialogue.Actor(this.getName(), "Sorry, I can't let you in if you don't tell me the password, which only specific guards know."),
                    InteractionTransition.NONE,
                    null
            );
        }
    }
    /**
     * Makes Beard tell you why he's named "Beard".
     */
    private InteractionResult whyBeard() {
        return new InteractionResult(
                new Dialogue.Actor(this.getName(), "Oh, one of my friends thought it was funny to call me 'Beard' even though I look like Mr. Clean himself! Heh, my real name is Aaron, but no one calls me that."),
                InteractionTransition.NONE,
                null
        );
    }

    /**
     * Ends the conversation.
     */
    private InteractionResult endConversation() {
        return new InteractionResult(
                new Dialogue.System("You wave goodbye to Beard."),
                InteractionTransition.POP,
                null
        );
    }

    private InteractionResult password(String password) {
        if (password.equals(PASSWORD)) {
            GameState.addFlag("password_told_to_checkpoint_guard");

            return new InteractionResult(
                    new Dialogue.Actor(this.getName(), "That's correct! You may enter the vault."),
                    InteractionTransition.NONE,
                    null
            );
        } else {
            GameState.incrementVaultPasswordGuesses();
            int incorrectGuesses = GameState.getVaultPasswordGuesses();

            if (incorrectGuesses < 3) {
                return new InteractionResult(
                        new Dialogue.Actor(this.getName(), "I'm sorry, that's not the correct password."),
                        InteractionTransition.NONE,
                        null
                );
            } else if (incorrectGuesses < 5) {
                return new InteractionResult(
                        new Dialogue.Actor(this.getName(), "That's not the correct password. (Are you just guessing it?)"),
                        InteractionTransition.NONE,
                        null
                );
            } else {
                return new InteractionResult(
                        new Dialogue.Actor(this.getName(), "That's it, stop trying to guess the password! Go find a guard who knows it and leave me alone."),
                        InteractionTransition.POP,
                        null
                );
            }
        }
    }
}
