package edu.miamioh.cse201.oceans14.ui.input;

/**
 * Represents a single input entered into the terminal.
 * <p>
 * An input tends to correspond to a single keypress. Inputs can either represent literal letters,
 * numbers, and symbols with {@link Character}, or they represent special actions with
 * {@link Special}.
 */
public sealed interface Input {
    /**
     * A literal character input, such as the letter 'A' or the number '3'.
     */
    final class Character implements Input {
        private final char c;

        public Character(char c) {
            this.c = c;
        }

        public char get() {
            return this.c;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Character) {
                return this.c == ((Character)obj).c;
            } else {
                return false;
            }
        }
    }

    /**
     * A special input that performs an action.
     */
    enum Special implements Input {
        ENTER,
        DELETE,
        LEFT,
        RIGHT,
        EXIT;
    }
}
