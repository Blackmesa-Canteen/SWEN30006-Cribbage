package gameHelper;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @program Cribbage
 * @description A pure Fabrication Class that defines the customized Card information
 * @create 2021-05-11 21:44
 */
public class CribbageCardInfoManager {

    Deck deck;

    public enum Suit {
        CLUBS, DIAMONDS, HEARTS, SPADES
    }

    public enum Rank {
        // Order of cards is tied to card images
        ACE(1, 1), KING(13, 10), QUEEN(12, 10), JACK(11, 10), TEN(10, 10), NINE(9, 9), EIGHT(8, 8), SEVEN(7, 7), SIX(6, 6), FIVE(5, 5), FOUR(4, 4), THREE(3, 3), TWO(2, 2);
        public final int order;
        public final int value;

        Rank(int order, int value) {
            this.order = order;
            this.value = value;
        }
    }

    public CribbageCardInfoManager(Deck deck) {
        this.deck = deck;
    }

    /*
        Canonical String representations of Suit, Rank, Card, and Hand
        */
    public String canonical(Suit s) {
        return s.toString().substring(0, 1);
    }

    public String canonical(Rank r) {
        switch (r) {
            case ACE:
            case KING:
            case QUEEN:
            case JACK:
            case TEN:
                return r.toString().substring(0, 1);
            default:
                return String.valueOf(r.value);
        }
    }

    public String canonical(Card c) {
        return canonical((Rank) c.getRank()) + canonical((Suit) c.getSuit());
    }

    public String canonical(Hand h) {
        Hand h1 = new Hand(deck); // Clone to sort without changing the original hand
        for (Card C : h.getCardList()) h1.insert(C.getSuit(), C.getRank(), false);
        h1.sort(Hand.SortType.POINTPRIORITY, false);
        return "[" + h1.getCardList().stream().map(this::canonical).collect(Collectors.joining(",")) + "]";
    }

    public static class MyCardValues implements Deck.CardValues { // Need to generate a unique value for every card
        public int[] values(Enum suit) {  // Returns the value for each card in the suit
            return Stream.of(Rank.values()).mapToInt(r -> (((Rank) r).order - 1) * (Suit.values().length) + suit.ordinal()).toArray();
        }
    }

    public static int cardValue(Card c) {
        return ((Rank) c.getRank()).value;
    }

    public int getCardOrder(Card c) {
        Rank rank = (Rank) c.getRank();
        return rank.order;
    }
}