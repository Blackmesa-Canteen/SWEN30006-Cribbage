package strategy;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;
import scoreManager.CardGameScoreManager;
import gameHelper.CribbageCardInfoManager;

/**
 * @program Cribbage
 * @description
 * @create 2021-05-15 09:35
 */
public class FlushScoreStrategy implements CribbageScoreStrategy {

    CribbageCardInfoManager cardInfoManager = null;
    CardGameScoreManager scoreManager = null;
    Deck deck = null;

    public FlushScoreStrategy(CribbageCardInfoManager cardInfoManager, CardGameScoreManager scoreManager, Deck deck) {
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
        //TODO: flush score calc for Show phase
    }
}