package strategy;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import gameHelper.CribbageCardInfoManager;
import scoreManager.CardGameScoreManager;

import java.util.ArrayList;

/**
 * @program Cribbage
 * @description
 * @create 2021-05-15 10:13
 */
public class ShowPhaseTotalPointsStrategy implements CribbageScoreStrategy {

    CribbageCardInfoManager cardInfoManager = null;
    CardGameScoreManager scoreManager = null;
    Deck deck = null;

    public ShowPhaseTotalPointsStrategy(CribbageCardInfoManager cardInfoManager, CardGameScoreManager scoreManager, Deck deck) {
        this.cardInfoManager = cardInfoManager;
        this.scoreManager = scoreManager;
        this.deck = deck;
    }

    @Override
    public void calcScore(int player, Hand segmentHand, Hand starter) {


        final int score_for_fifteen = 2;

        Hand calculationHand = new Hand(deck);
        Hand handWithoutStarter = new Hand(deck);

        /* set up calculationHand for counting scores */
        /* cards in hand + starter */
        for (Card card : segmentHand.getCardList()) {
            calculationHand.insert(card.clone(), false);
            handWithoutStarter.insert(card.clone(), false);
        }

        /* add starter card to the calculationHand */
        for (Card card : starter.getCardList()) {
            calculationHand.insert(card.clone(), false);
        }

        calculationHand.sort(Hand.SortType.POINTPRIORITY, false);
        handWithoutStarter.sort(Hand.SortType.POINTPRIORITY, false);

        /* Determine whether the starter Cards is Jack*/
        ArrayList<Hand> hands = totalFifteen(calculationHand);
        for (Hand hand: hands){
            scoreManager.addScoreToPlayer(score_for_fifteen, player, "fifteen," + cardInfoManager.canonical(hand));
        }
    }

    private int total(Hand hand) {
        int total = 0;
        for (Card c : hand.getCardList()) total += CribbageCardInfoManager.cardValue(c);
        return total;
    }

    private ArrayList<Hand> totalFifteen(Hand calculationHand) {
        ArrayList<Card> cards = calculationHand.getCardList();
        int cardsSize = cards.size();
        ArrayList<Hand> hands = new ArrayList<>();
        ArrayList<Card> newCards = new ArrayList<>();
        for (int r$ = 0; r$ < cardsSize; r$++) {
            combinations(cards, hands, newCards, 0, cardsSize - 1, 0, r$);
        }

        if (total(calculationHand)==15){
            hands.add(calculationHand);
        }
        return hands;
    }

    private void combinations(ArrayList<Card> cards, ArrayList<Hand> hands, ArrayList<Card> newCards, int start, int end, int index, int r) {

        if (index == r){
            Hand hand = new Hand(deck);
            for (int $j = 0; $j<r;$j++) {
                hand.insert(newCards.get($j), false);
            }
            if (total(hand) == 15) {
                hands.add(hand);
            }
        }
        for (int i = start; i <= end && ((end - i + 1) >= (r - index)); i++)
        {
            newCards.add(index,cards.get(i));
            combinations(cards, hands, newCards, i + 1, end, index + 1, r);
        }
    }
}