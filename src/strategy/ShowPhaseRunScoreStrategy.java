package strategy;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import gameHelper.CribbageCardInfoManager;
import scoreManager.CardGameScoreManager;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * @program Cribbage
 * @description
 * @create 2021-05-15 09:34
 */
public class ShowPhaseRunScoreStrategy implements CribbageScoreStrategy {

    CribbageCardInfoManager cardInfoManager = null;
    CardGameScoreManager scoreManager = null;
    Deck deck = null;

    public ShowPhaseRunScoreStrategy(CribbageCardInfoManager cardInfoManager, CardGameScoreManager scoreManager, Deck deck) {
        this.cardInfoManager = cardInfoManager;
        this.scoreManager = scoreManager;
        this.deck = deck;
    }

    @Override
    public void calcScore(int player, Hand segmentHand, Hand starter) {
        final int run3Score = 3;
        final int run4Score = 4;
        final int run5Score = 5;
        int i;
        int scores;

        Hand calculationHand = new Hand(deck);

        /* set up calculationHand for counting scores */
        /* cards in hand + starter */
        for(Card card : segmentHand.getCardList()) {
            calculationHand.insert(card.clone(), false);
        }

        for(Card card : starter.getCardList()) {
            calculationHand.insert(card.clone(), false);
        }

        calculationHand.sort(Hand.SortType.POINTPRIORITY, false);

        // check if run5 works
        boolean run5 = true;
        for (i=0; i < calculationHand.getCardList().size() - 1; i++) {
            Card currentCard = calculationHand.getCardList().get(i);
            Card nextCard = calculationHand.getCardList().get(i+1);
            int currentCardNum = cardInfoManager.getCardOrder(currentCard);
            int nextCardNum = cardInfoManager.getCardOrder(nextCard);
            if (nextCardNum - currentCardNum != 1 && nextCardNum - currentCardNum != 12){
                run5 = false;
            }
        }
        if (run5) {
            scores = run5Score;
            scoreManager.addScoreToPlayer(scores, player, "run5," + cardInfoManager.canonical(calculationHand));
        }

        // check if run3 and run4 works
        // get all card combination with size 3 & 4
        ArrayList cards = calculationHand.getCardList();
        int cardsSize = cards.size();
        ArrayList<Hand> hands = new ArrayList<>();
        ArrayList<Card> newCards = new ArrayList<>();
        for (int r$ = 0; r$ < cardsSize; r$++) {
            combinations(cards, hands, newCards, 0, cardsSize - 1, 0, r$);
        }
        for (Hand hand: hands){
            if (hand.getCardList().size() == 3){
                boolean run3 = false;
                Card card1 = hand.getCardList().get(0);
                Card card2 = hand.getCardList().get(1);
                Card card3 = hand.getCardList().get(2);
                int card1Num = cardInfoManager.getCardOrder(card1);
                int card2Num = cardInfoManager.getCardOrder(card2);
                int card3Num = cardInfoManager.getCardOrder(card3);
                System.out.println(hand.getCardList());
                if ((card3Num - card2Num == 1 && card2Num - card1Num == 1)  || // J Q K
                        (card3Num - card2Num == 1 && card2Num - card1Num == 11) || // A Q K
                        (card3Num - card2Num == 11 && card2Num - card1Num == 1)) { // A 2 K
                    run3 = true;
                }
                if (run3) {
                    scores = run3Score;
                    scoreManager.addScoreToPlayer(scores, player, "run3," + cardInfoManager.canonical(hand));
                }
            }
            else if (hand.getCardList().size() == 4){
                boolean run4 = false;
                Card card1 = hand.getCardList().get(0);
                Card card2 = hand.getCardList().get(1);
                Card card3 = hand.getCardList().get(2);
                Card card4 = hand.getCardList().get(3);
                int card1Num = cardInfoManager.getCardOrder(card1);
                int card2Num = cardInfoManager.getCardOrder(card2);
                int card3Num = cardInfoManager.getCardOrder(card3);
                int card4Num = cardInfoManager.getCardOrder(card4);
                if ((card4Num - card3Num == 1 && card3Num - card2Num == 1 && card2Num - card1Num == 1)  ||      // 10 J Q K
                        (card4Num - card3Num == 1 && card3Num - card2Num == 1 && card2Num - card1Num == 10) ||  // A J Q K
                        (card4Num - card3Num == 1 && card3Num - card2Num == 10 && card2Num - card1Num == 1) ||  // A 2 Q K
                        (card4Num - card3Num == 10 && card3Num - card2Num == 1 && card2Num - card1Num == 1)) {  // A 2 3 K
                    run4 = true;
                }

                if (run4) {
                    scores = run4Score;
                    scoreManager.addScoreToPlayer(scores, player, "run4," + cardInfoManager.canonical(hand));
                }
            }
        }
    }

    private void combinations(ArrayList<Card> cards, ArrayList<Hand> hands, ArrayList<Card> newCards, int start, int end, int index, int r) {
        if (index == r){
            Hand hand = new Hand(deck);
            for (int $j = 0; $j<r;$j++) {
                hand.insert(newCards.get($j), false);
            }
            if (hand.getCardList().size() >= 3) {
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