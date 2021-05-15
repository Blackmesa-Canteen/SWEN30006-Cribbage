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

        final int score_for_flush4 = 4;
        final int score_for_flush5 = 5;

        Hand calculationHand = new Hand(deck);
        Hand handWithoutStarter = new Hand(deck);

        /* set up calculationHand for counting scores */
        /* cards in hand + starter */
        for(Card card : segmentHand.getCardList()) {
            calculationHand.insert(card.clone(), false);
            handWithoutStarter.insert(card.clone(), false);
        }

        for(Card card : starter.getCardList()) {
            calculationHand.insert(card.clone(), false);
        }

        calculationHand.sort(Hand.SortType.POINTPRIORITY, false);
        handWithoutStarter.sort(Hand.SortType.POINTPRIORITY, false);

        /* traverse all suit enum */
        for(CribbageCardInfoManager.Suit suit: CribbageCardInfoManager.Suit.values()) {
            Hand sameSuitHand = handWithoutStarter.extractCardsWithSuit(suit);

            if(sameSuitHand.getNumberOfCards() == 4) {

                if(starter.getFirst().getSuit().equals(suit)) {
                    /* flush 5: if starter card is also the same */
                    scoreManager.addScoreToPlayer(score_for_flush5, player,
                            "flush5," + cardInfoManager.canonical(calculationHand));
                }
                else
                {
                    /* flush 4: four card in HAND are of the same suit */
                    scoreManager.addScoreToPlayer(score_for_flush4, player,
                            "flush4," + cardInfoManager.canonical(handWithoutStarter));
                }
                break;
            }
        }
    }
}