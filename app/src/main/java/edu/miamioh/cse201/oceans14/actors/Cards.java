package edu.miamioh.cse201.oceans14.actors;

import java.util.Random;

public record Cards(Suit suit, Rank rank) {

    private static final Random RANDOM = new Random();

    public enum Suit {
        HEARTS, DIAMONDS, CLUBS, SPADES
    }

    public enum Rank {
        ACE(11), TWO(2), THREE(3), FOUR(4), FIVE(5), SIX(6), SEVEN(7),
        EIGHT(8), NINE(9), TEN(10), JACK(10), QUEEN(10), KING(10);

        private final int value;

        Rank(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    // Draw a random card (with replacement)
    public static Cards draw() {
        Suit[] suits = Suit.values();
        Rank[] ranks = Rank.values();
        return new Cards(
                suits[RANDOM.nextInt(suits.length)],
                ranks[RANDOM.nextInt(ranks.length)]);
    }

    // The numeric value of the card (suit doesn't matter)
    public int getValue() {
        return rank.getValue();
    }

    // The full name, e.g. "KING of SPADES"
    public String getName() {
        return rank + " of " + suit;
    }

    public int blackjackValue() {
        return switch (rank) {
            case ACE -> 11;
            case JACK, QUEEN, KING -> 10;
            default -> rank.getValue();
        };
    }
}