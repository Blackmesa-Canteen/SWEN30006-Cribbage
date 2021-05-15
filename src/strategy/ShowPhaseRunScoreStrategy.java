package strategy;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import gameHelper.CribbageCardInfoManager;
import scoreManager.CardGameScoreManager;

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

        int handSize = calculationHand.getNumberOfCards();

        /* cards in hand should more than 3, then can count runs*/
        if(handSize < 3) return;

        for(int runNum = 3; runNum <= handSize; runNum++) {
            Hand[] runHands = calculationHand.extractSequences(runNum);

            // if there are any runs
            for(Hand hand: runHands) {
                scoreManager.addScoreToPlayer(runNum, player,
                        "run" + runNum + "," + cardInfoManager.canonical(hand));
            }

            /* only 5 cards, so only one run */
            /* if one run occurs, then no any other runs */
            if(runHands.length > 0) break;
        }

    }
}