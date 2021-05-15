package scoreManager;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

import java.util.HashMap;

/**
 * @program Cribbage
 * @description CardHistoryManager is used to maintain Statistical records of cards dealt by
 * store collections of cards with specific names
 * @create 2021-05-14 17:39
 */
public class CardHistoryManager {

    Deck deck;
    HashMap<String, Hand> pile;

    public CardHistoryManager(Deck deck) {
        pile = new HashMap<>();
        this.deck = deck;
    }

    /**
     * create a card history pile with the name id
     * @param pileName card history pile's name
     * @return whether creation is successful
     */
    public boolean creatCardHistoryPile(String pileName) {

        if(!pile.containsKey(pileName)) {
            pile.put(pileName, new Hand(deck));
            return true;
        }
        System.out.println("Create Pile: This pile is existing!");
        return false;
    }

    /**
     * record a cloned card object into a specific history pile
     * @param pileName history pile name id
     * @param card card object
     * @return successful
     */
    public boolean recordCardToHistoryPile(String pileName, Card card) {

        if(pile.containsKey(pileName)) {
            Hand collection = pile.get(pileName);
            collection.insert(card.clone(), false);
            collection.sort(Hand.SortType.POINTPRIORITY, false);

            return true;
        }

        System.out.println("Put in Pile: This pile is not existing!");
        return false;
    }

    /**
     * get a history record Hand object with a specific history pile name
     * @param pileName pile name
     * @return Hand object
     */
    public Hand getCardHistoryPile(String pileName) {

        if(pile.containsKey(pileName)) {
            return pile.get(pileName);
        }

        System.out.println("Get history: This pile is not existing!");
        return null;
    }

    public boolean cloneHistoryPile(String originalPileName, String newPileName) {
        if(pile.containsKey(originalPileName) && !pile.containsKey(newPileName)) {
            Hand originalPile = pile.get(originalPileName);
            Hand newPile = new Hand(deck);

            for(Card card : originalPile.getCardList()){
                newPile.insert(card.clone(), false);
            }

            pile.put(newPileName, newPile);
            return true;
        }

        System.out.println("cloneHistoryPile: pile name error!");
        return false;
    }

    /**
     * clear a history pile with pile name
     * @param pileName pile name id
     * @return successful
     */
    public boolean clearHistoryPile(String pileName) {
        if(pile.containsKey(pileName)) {
            pile.remove(pileName);
            return true;
        }

        System.out.println("Clear history: This pile is not existing!");
        return false;
    }

    public boolean isPileExist(String pileName) {
        return pile.containsKey(pileName);
    }
}