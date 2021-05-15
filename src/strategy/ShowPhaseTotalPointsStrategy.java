package strategy;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import gameHelper.CribbageCardInfoManager;
import scoreManager.CardGameScoreManager;

/**
 * @author Xiaotian
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

        /* set up calculationHand for counting scores */
        /* cards in hand + starter */
        for(Card card : segmentHand.getCardList()) {
            calculationHand.insert(card.clone(), false);
        }

        for(Card card : starter.getCardList()) {
            calculationHand.insert(card.clone(), false);
        }

        calculationHand.sort(Hand.SortType.POINTPRIORITY, false);

        //TODO: total calc for Show phase



    }

    private int total(Hand hand) {
        int total = 0;
        for (Card c : hand.getCardList()) total += CribbageCardInfoManager.cardValue(c);
        return total;
    }
}